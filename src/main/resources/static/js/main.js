// URL에서 특정 파라미터 값을 가져오는 함수
// ✅ 1. 로그아웃 버튼 클릭 시 sessionStorage에 "logout" 저장
$('#logoutBtn').on('click', function () {
    sessionStorage.setItem("logout", "true"); // 로그아웃 정보 저장
});

displayLogOutSucessAlert();

function displayLogOutSucessAlert() {
    // ✅ 2. 페이지 로드 시 "logout" 값 확인
    if (sessionStorage.getItem("logout") === "true") {
        Swal.fire({
            icon: 'success',
            title: '로그아웃 완료',
            text: '성공적으로 로그아웃되었습니다.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인'
        });

        // ✅ 3. SweetAlert이 표시된 후, sessionStorage에서 로그아웃 정보 삭제
        sessionStorage.removeItem("logout");
    }
}

