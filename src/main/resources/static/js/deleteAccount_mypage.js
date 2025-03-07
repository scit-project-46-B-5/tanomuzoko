const password = document.querySelector(".password");
const pwIconBox = document.querySelector(".pwIconBox"); 
const changeInfoBtn = document.querySelector(".changeInfoBtn");

const modal = document.querySelector("#myModal");
const closeModal = document.querySelector("#closeModal");
const modalButton = document.querySelector("#modalButton");

// 서버로부터 받은 비밀번호 확인 상태를 추적
let isPasswordCorrect = false;

// Debounce 함수 정의
function debounce(func, delay) {
    let timer;
    return function (...args) {
        clearTimeout(timer); // 기존 타이머 제거
        timer = setTimeout(() => func.apply(this, args), delay); 
    };
}

const checkPw = debounce(async function () {
    if (password.value.length !== 0) {
        try {
            const response = await fetch("/mypage/checkPassword", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ currentPassword: password.value })
            });

            if (!response.ok) {
                throw new Error(`서버 오류: ${response.status}`);
            }

            const data = await response.json();
            isPasswordCorrect = data.isCorrect !== undefined ? data.isCorrect : data;

            if (isPasswordCorrect) {
                changeInfoBtn.disabled = false;
                pwIconBox.innerHTML = `<i class="fa-solid fa-circle-check pwCheckIcon" style="color: #5cd85a;"></i>`;
            } else {
                changeInfoBtn.disabled = true;
                pwIconBox.innerHTML = `<i class="fa-solid fa-circle-xmark pwCheckIcon" style="color: #f55735;"></i>`;
            }
        } catch (error) {
            console.error("비밀번호 확인 중 오류 발생:", error);
        }
    } else {
        isPasswordCorrect = false;
        pwIconBox.innerHTML = "";
    }
}, 300);

password.addEventListener("input", checkPw);

changeInfoBtn.addEventListener('click', alertModal);

function alertModal() {
    modal.style.display = "block";
}

// 모달 창 닫기
closeModal.addEventListener('click', function () {
    modal.style.display = "none";
});

// 모달 창 내 확인 버튼 클릭 시 모달 닫기
modalButton.addEventListener('click', async function () {
    modal.style.display = "none";
     try {
        // 회원 탈퇴 API 호출
        const response = await fetch("/mypage/deleteAccountProc", {
            method: "POST",
        });      
         if (response.ok) { 
            window.location.href = "http://localhost:9005/"; 
        } else { 
            throw new Error(`회원 탈퇴 실패: ${response.status}`); 
         }    
    } catch (error) {
        console.error("회원 탈퇴 중 오류 발생:", error);
    }
});

// 모달 외부 클릭 시 모달 닫기
window.addEventListener('click', function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
});