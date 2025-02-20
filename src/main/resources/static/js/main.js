$(function () {
    $('#logoutBtn').on('click', logout);
});
function logout() {
    $.ajax({
        url: "/user/logout", // Spring Security 설정된 로그아웃 URL
        type: "POST",
        success: function () {
            alert("로그아웃 되었습니다.");
            window.location.href = "/"; // ✅ 로그아웃 후 메인 페이지로 이동
        },
        error: function () {
            alert("로그아웃 실패! 다시 시도해주세요.");
        }
    });
};