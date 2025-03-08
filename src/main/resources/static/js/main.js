$(function () {
    $('#logoutBtn').on('click', logout);
});
function logout() {
    $.ajax({
        url: "/user/logout", // Spring Security 설정된 로그아웃 URL
        type: "POST",
        success: function () {
            Swal.fire({
                icon: 'success',
                title: '성공!',
                text: '로그아웃이 완료되었습니다다.',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
            window.location.href = "/"; // ✅ 로그아웃 후 메인 페이지로 이동
        },
        error: function () {
            Swal.fire({
                icon: 'warning',
                title: '실패',
                text: '로그아웃 실패! 다시 시도해주세요.',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
        }
    });
};