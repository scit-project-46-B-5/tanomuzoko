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

const correctPw = '1234'; //*** 비밀번호로 변경하기

// 새 비밀번호 입력란 숨기기
newPasswordSection.style.display = "none";

// 비밀번호 입력 확인
document.addEventListener("keyup", validateForm);

function checkPw() {
    if(password.value.length !== 0) {
        if(password.value === correctPw) { 
            changePwBtn.disabled = false;
            pwIconBox.innerHTML = `<i class="fa-solid fa-circle-check pwCheckIcon" style="color: #5cd85a;"></i>`;
        } else {
            changePwBtn.disabled = true;
            pwIconBox.innerHTML = `<i class="fa-solid fa-circle-xmark pwCheckIcon" style="color: #f55735;"></i>`;
        }
    } else {
        pwIconBox.innerHTML= "";
        changePwBtn.disabled = true; 
    }
}

// 새 비밀번호 확인
function checkNewPw() {
    if (newPW.value.length !== 0 && newPW.value === newPWCheck.value) {
        newPWCheck.style.borderBottom = "2px solid #5cd85a";
    } else {
        newPWCheck.style.borderBottom = "2px solid #f55735";
    }
}

// 변경하기 버튼 클릭 시 새 비밀번호 입력란 표시/숨김
changePwBtn.addEventListener("click", function () {
    if (newPasswordSection.style.display === "none") {
        newPasswordSection.style.display = "block";
        changePwBtn.innerText = "취소";
        changePwBtn.style.backgroundColor = "#f55735";
        password.disabled = true;
    } else {
        newPW.value = "";
        newPWCheck.value = "";
        newPWCheck.style.borderBottom = "none"; 
        newPasswordSection.style.display = "none";
        changePwBtn.innerText = "변경하기";
        changePwBtn.style.backgroundColor = "#5cd85a";
        password.disabled = false;
        changeInfoBtn.disabled = true;
    }
    validateForm();
});


// 정보 변경 버튼 활성화 조건 확인
function validateForm() {
    const isPwCorrect = password.value === correctPw;
    const isNewPwValid = newPW.value.length > 0 && newPW.value === newPWCheck.value;
    const isNickNameValid = nickName.value.trim().length > 0;

    if (newPasswordSection.style.display === "none") {
        // 비밀번호 변경 없이 닉네임만 변경하는 경우
        changeInfoBtn.disabled = !(isPwCorrect && isNickNameValid);
    } else {
        // 비밀번호도 변경하는 경우
        changeInfoBtn.disabled = !(isNickNameValid && isNewPwValid);
    }
}

password.addEventListener("keyup", checkPw);
newPW.addEventListener("keyup", checkNewPw);
newPWCheck.addEventListener("keyup", checkNewPw);
nickName.addEventListener("keyup", validateForm);
changeInfoBtn.addEventListener('click', changeInfo); 

function changeInfo() {
     showProgress();
}

// 모달 창 닫기
closeModal.addEventListener('click', function () {
    modal.style.display = "none";
});

// 모달 창 내 확인 버튼 클릭 시 모달 닫기
modalButton.addEventListener('click', function () {
    modal.style.display = "none";
});

// 모달 외부 클릭 시 모달 닫기
window.addEventListener('click', function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
});

function showProgress() {
    const progressContainer = document.querySelector('.progress-container');
    const progressBar = document.querySelector('.progress-bar');
    const progressText = document.querySelector('.progress-text');

    // Progress Bar 및 텍스트 보이게 설정
    progressContainer.style.display = 'block';
    progressText.textContent = "변경 중...";

    // 0.1초 후 애니메이션 시작
    setTimeout(() => {
        progressBar.style.width = '100%';
    }, 100);

    // 1초 후 "정보 변경 완료!" 텍스트 변경
    setTimeout(() => {
        progressText.textContent = "변경 완료";
    }, 3000);

    // 2초 후 Progress Bar 및 텍스트 숨기기
    setTimeout(() => {
        progressContainer.style.display = 'none';
        progressBar.style.width = '0';
    }, 5000);
}
