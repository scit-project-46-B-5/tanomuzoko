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

// 새 비밀번호 입력란 숨기기
newPasswordSection.style.display = "none";

const createPasswordChecker = () => {
    let isPasswordCorrect = false;

    async function checkPw() {
        if (password.value.length === 0) {
            isPasswordCorrect = false;
            pwIconBox.innerHTML = "";
            changePwBtn.disabled = true;
            validateForm();
            return false; // Resolve immediately with false if input is empty
        }

        try {
            const response = await fetch("/mypage/checkPassword", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ currentPassword: password.value })
            });
            const data = await response.json();
            isPasswordCorrect = data;

            // Update UI
            pwIconBox.innerHTML = isPasswordCorrect
                ? `<i class="fa-solid fa-circle-check pwCheckIcon" style="color: #5cd85a;"></i>`
                : `<i class="fa-solid fa-circle-xmark pwCheckIcon" style="color: #f55735;"></i>`;

            changePwBtn.disabled = !isPasswordCorrect;
            validateForm();

            return isPasswordCorrect;
        } catch (error) {
            console.error("Error:", error);
            return false;
        }
    }

    const debouncedCheckPw = debounce(checkPw, 300); // Apply debounce here

    function isPasswordValid() {
        return isPasswordCorrect;
    }

    return { checkPw: debouncedCheckPw, isPasswordValid };
};

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


function debounce(func, delay) {
    let timer;
    return function (...args) {
        return new Promise((resolve) => {
            clearTimeout(timer);
            timer = setTimeout(async () => {
                const result = await func.apply(this, args);
                resolve(result);
            }, delay);
        });
    };
}


// 정보 변경 버튼 활성화 조건 확인
function validateForm() {
    const isNickNameValid = nickName.value.trim().length >= 2 && nickName.value.trim().length <= 11;
    const isNewPwValid = newPW.value.length >= 8 && newPW.value === newPWCheck.value;

    const nickNameLengthMessage = document.getElementById("nickNameLengthMessage");
    nickNameLengthMessage.style.display = isNickNameValid ? "none" : "block";

    if (newPasswordSection.style.display === "none") {
        changeInfoBtn.disabled = !(passwordChecker.isPasswordValid() && isNickNameValid);
    } else {
        changeInfoBtn.disabled = !(isNickNameValid && isNewPwValid);
    }

    changePwBtn.disabled = !(passwordChecker.isPasswordValid() && isNickNameValid);
}

const passwordChecker = createPasswordChecker();

// 비밀번호 입력 확인
document.addEventListener("keyup", validateForm);
password.addEventListener("keyup", async () => {
    await passwordChecker.checkPw();
});
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