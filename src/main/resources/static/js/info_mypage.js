const password = document.querySelector(".password");
const pwIconBox = document.querySelector(".pwIconBox"); 
const changePwBtn = document.querySelector(".change-pw-btn");
const newPasswordSection = document.querySelector(".new-password-section");

const newPW = document.querySelector("#newPW");
const newPWCheck = document.querySelector("#newPWCheck");
const nickName = document.querySelector("#nickName");
const changeInfoBtn = document.querySelector(".changeInfoBtn");

const modal = document.getElementById("myModal");
const closeModal = document.getElementById("closeModal");
const modalButton = document.getElementById("modalButton");

// 서버로부터 받은 비밀번호 확인 상태를 추적
let isPasswordCorrect = false;

// 새 비밀번호 입력란 숨기기
newPasswordSection.style.display = "none";

// Debounce 함수 정의
function debounce(func, delay) {
    let timer;
    return function (...args) {
        clearTimeout(timer); // 기존 타이머 제거
        timer = setTimeout(() => func.apply(this, args), delay); 
    };
}

// 비밀번호 확인
const checkPw = debounce(async function () {
    if (password.value.length !== 0) {
        try {
            const response = await fetch("/mypage/checkPassword", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ currentPassword: password.value })
            });
            const data = await response.json(); 
            isPasswordCorrect = data;
            if (isPasswordCorrect) {
                changePwBtn.disabled = false;
                pwIconBox.innerHTML = `<i class="fa-solid fa-circle-check pwCheckIcon" style="color: #5cd85a;"></i>`;
            } else {
                changePwBtn.disabled = true;
                pwIconBox.innerHTML = `<i class="fa-solid fa-circle-xmark pwCheckIcon" style="color: #f55735;"></i>`;
            }
            validateForm();
        } catch (error) {
            console.error('Error:', error);
        }
    } else {
        isPasswordCorrect = false;
        pwIconBox.innerHTML = "";
        changePwBtn.disabled = true;
        validateForm();
    }
}, 300);

// 새 비밀번호 확인 및 길이 체크
const checkNewPw = debounce(function () {
    const newPassword = newPW.value.trim();
    const newPasswordCheck = newPWCheck.value.trim();
    const passwordLengthMessage = document.getElementById("passwordLengthMessage");

    // 비밀번호 길이 확인 (8~20자리)
    const isLengthValid = newPassword.length >= 8 && newPassword.length <= 20;

    // 새 비밀번호와 비밀번호 확인 일치 여부
    const isPasswordMatch = newPassword === newPasswordCheck;

    if (!isLengthValid) {
        passwordLengthMessage.style.display = "block"; 
    } else {
        passwordLengthMessage.style.display = "none";
    }

    if (isLengthValid && isPasswordMatch) {
        newPWCheck.style.borderBottom = "2px solid #5cd85a";
    } else {
        newPWCheck.style.borderBottom = "2px solid #f55735";
    }

    validateForm();
}, 300); 

// 변경하기 버튼 클릭 시 새 비밀번호 입력란 표시/숨김
changePwBtn.addEventListener("click", function () {
    if (newPasswordSection.style.display === "none") {
        newPasswordSection.style.display = "block";
        changePwBtn.innerText = "취소";
        changePwBtn.style.backgroundColor = "#f55735";
        password.readOnly = true;
        passwordLengthMessage.style.display = "none";
    } else {
        newPW.value = "";
        newPWCheck.value = "";
        newPWCheck.style.borderBottom = "none"; 
        newPasswordSection.style.display = "none";
        changePwBtn.innerText = "변경하기";
        changePwBtn.style.backgroundColor = "#5cd85a";
        password.readOnly = false;
        changeInfoBtn.disabled = true;
        passwordLengthMessage.style.display = "none";
    }
    validateForm();
});

// 정보 변경 버튼 활성화 조건 확인
function validateForm() {
    const isNickNameValid = nickName.value.trim().length >= 2 && nickName.value.trim().length <= 11;
    const isNewPwValid = newPW.value.length >= 8 && newPW.value === newPWCheck.value;

    // 닉네임 길이 조건을 체크하고 안내 메시지 표시/숨기기
    const nickNameLengthMessage = document.getElementById("nickNameLengthMessage");
    nickNameLengthMessage.style.display = (nickName.value.trim().length < 2 || nickName.value.trim().length > 11) ? "block" : "none";

    const isPasswordSectionValid = isPasswordCorrect && (newPasswordSection.style.display === "none" || isNewPwValid);
    changeInfoBtn.disabled = !(isNickNameValid && isPasswordSectionValid);

    // 비밀번호 변경 버튼 유효성 체크
    changePwBtn.disabled = !(isPasswordCorrect && isNickNameValid);
}

// 비밀번호 입력 확인
document.addEventListener("keyup", validateForm);
password.addEventListener("keyup", checkPw);
newPW.addEventListener("keyup", checkNewPw);
newPWCheck.addEventListener("keyup", checkNewPw);
nickName.addEventListener("keyup", validateForm);
changeInfoBtn.addEventListener('click', changeInfo);

async function changeInfo() {
    const newNickName = nickName.value.trim();
    const newPassword = newPW.value.trim();

    const requestData = {
        newNickName,
        newPassword: newPassword.length > 0 ? newPassword : null
    };

    try {
        const response = await fetch("/mypage/updateInfo", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(requestData)
        });

        const data = await response.json();

        if (data.success) {
            modal.style.display = "block";
        } else {
            alert("정보 변경에 실패했습니다.");
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

// 모달 창 닫기
closeModal.addEventListener('click', function () {
    modal.style.display = "none";
    location.reload();
});

// 모달 창 내 확인 버튼 클릭 시 모달 닫기
modalButton.addEventListener('click', function () {
    modal.style.display = "none";
    location.reload();
});

// 모달 외부 클릭 시 모달 닫기
window.addEventListener('click', function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
        location.reload();
    }
});