// 로그인시 필요한 검증 작업
// ✅ URL에서 특정 파라미터 값을 가져오는 함수
function getParameterByName(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

function init() {
    // ✅ 로그인 페이지가 로드될 때 실행
    let errorType = $("#errorType").val() || getParameterByName("errorType");
    let userId = $("#hiddenUserId").val() || getParameterByName("userId");

    if (!userId || userId.trim() === "undefined") {
        userId = "";
    }

    if (errorType) {
        // 🔹 로그인 실패 알림창 처리
        if (errorType === "badCredentials") {
            Swal.fire({
                icon: 'warning',
                title: '로그인 실패',
                text: '아이디 또는 비밀번호가 올바르지 않습니다.',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인'
            });
        } else if (errorType === "disabled") {
            Swal.fire({
                icon: 'error',
                title: '계정 비활성화',
                text: '해당 계정은 비활성화되었습니다. 복구 페이지로 이동합니다...',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인'
            }).then(() => {
                if (userId) {
                    window.location.href = "/user/restore?userId=" + encodeURIComponent(userId);
                } else {
                    window.location.href = "/user/restore";
                }
            });
        }
    }
}



// ✅ 로그인 버튼 클릭 이벤트 (아이디 & 비밀번호 길이 검증)
$("#loginBtn").on("click", function (event) {
    event.preventDefault(); // 기본 제출 동작 방지

    let userId = $("#userId").val().trim();
    let userPassword = $("#userPassword").val().trim();

    // 🔹 아이디 및 비밀번호 길이 체크
    if (userId.length < 3 || userId.length > 12) {
        Swal.fire({
            icon: 'warning',
            title: '로그인 실패',
            text: '아이디는 3~12자여야 합니다.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인'
        });
        return;
    }
    if (userPassword.length < 8 || userPassword.length > 20) {
        Swal.fire({
            icon: 'warning',
            title: '로그인 실패',
            text: '비밀번호는 8~20자여야 합니다.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인'
        });
        return;
    }

    $("form").submit(); // 폼 제출 실행
});

// ✅ Enter 키 입력 시 로그인 처리
$("#userPassword").on("keypress", function (event) {
    if (event.which === 13) { // 엔터 키 (키 코드 13)
        event.preventDefault();
        $("#loginBtn").click();
    }
});

init();