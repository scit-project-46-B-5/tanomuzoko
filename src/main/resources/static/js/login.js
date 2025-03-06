// 로그인시 필요한 검증 작업

// URL에서 특정 파라미터 값을 가져오는 함수
function getParameterByName(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

// 로그인 페이지가 로드될 때 실행 (로그인 길이값은 일치하지만 아이디 및 비번이 다른경우 알림창)
document.addEventListener("DOMContentLoaded", function () {
    const error = getParameterByName("error"); // 'error' 파라미터 확인
    if (error === "true") {
        Swal.fire({
            icon: 'warning',
            title: '로그인 실패',
            text: '아이디 또는 비밀번호가 올바르지 않습니다.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인'
        });
    }
});

$('#loginBtn').on('click', function (event) {
    event.preventDefault(); // 기본 제출 동작 방지

    let userId = $('#userId').val().trim();
    let userPassword = $('#userPassword').val().trim();

    // 아이디 및 비밀번호 길이 체크
    if (userId.length < 3 || userId.length > 12) {
        Swal.fire({
            icon: 'warning',
            title: '로그인 실패.',
            text: '아이디 비밀번호가 틀렸습니다.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }
    if (userPassword.length < 8 || userPassword.length > 20) {
        Swal.fire({
            icon: 'warning',
            title: '로그인 실패',
            text: '아이디 비밀번호가 틀렸습니다.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }

    console.log("로그인 요청 실행");
    $('form').submit();  // 폼 제출 실행
});

