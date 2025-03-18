$("#close-btn").click(function () {
    window.location.href = "/";
});

$('#restore-btn').on('click', function () {
    let userId = $('#userId').val().trim();

    if (!userId) {
        Swal.fire({
            icon: 'warning',
            title: '입력 오류',
            text: '아이디를 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }

    // ✅ 사용자에게 확인 메시지 표시
    Swal.fire({
        title: "계정 복원",
        text: "정말 계정을 복원하시겠습니까?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "예, 복원하겠습니다!",
        cancelButtonText: "아니요"
    }).then((result) => {
        if (result.isConfirmed) {
            // ✅ 사용자가 "예"를 눌렀을 때만 복구 요청 실행
            $.ajax({
                type: "POST",
                url: "/user/restore",
                data: { userId: userId },
                dataType: "json",
                success: function (response) {
                    Swal.fire({
                        icon: 'success',
                        title: '복구 완료',
                        text: response.message,
                        confirmButtonColor: '#ff7f50',
                        confirmButtonText: '확인',
                    }).then(() => {
                        window.location.href = "/user/login";  // 로그인 페이지로 이동
                    });
                },
                error: function (xhr) {
                    let errorMessage = xhr.responseJSON?.message || "서버 오류가 발생했습니다.";
                    Swal.fire({
                        icon: 'error',
                        title: '복구 실패',
                        text: errorMessage,
                        confirmButtonColor: '#ff7f50',
                        confirmButtonText: '확인',
                    });
                }
            });
        } else if (result.isDismissed) {
            // "아니요" 버튼을 클릭한 경우
            return;
            // window.location.href = "/"; // 홈 화면으로 이동
        }
    });
});