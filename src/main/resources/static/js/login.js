// 로그인시 필요한 검증 작업
$(function () {
    $('#loginBtn').on('click', login)
});

// 로그인을 위한 검증 작업
function login() {
    let userId = $('#userId').val();
    // 아이디 체크
    if (userId.trim().length < 3 || userId.trim().length > 12) {
        alert('아이디는 3~12자 사이로 입력해주세요.');
        return false;
    }
    // 비밀번호 체크
    let userPwd = $('#userPwd').val();
    if (userPwd.trim().length < 8 || userPwd.trim().length > 20) {
        alert('비밀번호는 8~20자 사이로 입력해주세요.');
        return false;
    }
    console.log("sad");
    $('form').submit();
};