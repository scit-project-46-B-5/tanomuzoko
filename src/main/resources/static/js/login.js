// 로그인시 필요한 검증 작업
$(function () {
    $('#loginBtn').on('click', function (event) {
        event.preventDefault(); // 기본 제출 동작 방지

        let userId = $('#userId').val().trim();
        let userPassword = $('#userPassword').val().trim();

        // 아이디 및 비밀번호 길이 체크
        if (userId.length < 3 || userId.length > 12) {
            alert('아이디는 3~12자 사이로 입력해주세요.');
            return;
        }
        if (userPassword.length < 8 || userPassword.length > 20) {
            alert('비밀번호는 8~20자 사이로 입력해주세요.');
            return;
        }

        console.log("로그인 요청 실행");
        $('form').submit();  // 폼 제출 실행
    });
});
