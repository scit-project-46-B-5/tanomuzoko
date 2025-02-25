// 로그인시 필요한 검증 작업
$(function () {
    $('#loginBtn').on('click', function (event) {
        event.preventDefault(); // 기본 제출 동작 방지

        let userId = $('#userId').val().trim();
        let userPassword = $('#userPassword').val().trim();

        // 아이디 및 비밀번호 길이 체크
        if (userId.length < 3 || userId.length > 12) {
            Swal.fire({
                icon: 'warning',
                title: '로그인 실패',
                text: '아이디 비밀번호를 확인하고 다시 입력해주세요.',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
            return;
        }
        if (userPassword.length < 8 || userPassword.length > 20) {
            Swal.fire({
                icon: 'warning',
                title: '로그인 실패',
                text: '아이디 비밀번호를 확인하고 다시 입력해주세요.',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
            return;
        }

        console.log("로그인 요청 실행");
        $('form').submit();  // 폼 제출 실행
    });
    const params = new URLSearchParams(window.location.search);
    if (params.has("error")) {
        Swal.fire({
            icon: "warning",
            title: "로그인 실패",
            text: "아이디 또는 비밀번호가 올바르지 않습니다.",
            confirmButtonColor: "#ff7f50",
            confirmButtonText: "확인",
        });
    }
});
