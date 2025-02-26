const password = document.querySelector(".password");
const changeInfoBtn = document.querySelector(".changeInfoBtn");

const modal = document.querySelector("#myModal");
const closeModal = document.querySelector("#closeModal");
const modalButton = document.querySelector("#modalButton");

const correctPw = '1234';

password.addEventListener('keyup', checkPw);

function checkPw() {
    if(password.value === correctPw) {
        changeInfoBtn.disabled = false;
    } else {
        changeInfoBtn.disabled = true;
    }
}

changeInfoBtn.addEventListener('click', alertModal);

function alertModal() {
    modal.style.display = "block";
}

// 모달 창 닫기
closeModal.addEventListener('click', function () {
    modal.style.display = "none";
});

// 모달 창 내 확인 버튼 클릭 시 모달 닫기
modalButton.addEventListener('click', function () {
    modal.style.display = "none";
    // 회원탈퇴 process
    // 로그아웃 메인화면으로 이동
    window.location.href = "http://127.0.0.1:5500/main.html";
});

// 모달 외부 클릭 시 모달 닫기
window.addEventListener('click', function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
});