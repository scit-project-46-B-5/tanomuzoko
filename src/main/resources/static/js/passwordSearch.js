// 이메일 유효성 검사

$('#password-find').on('click', function (event) {
    event.preventDefault(); // 기본 제출 방지
    
    let userEmail = $("#userEmail").val().trim();
    let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    let userId = $('#userId').val();

    if (!userId) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '아이디를 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }
    if (userId.trim().length < 3 || userId.trim().length > 12) {
        
        if (!userEmail) {
            Swal.fire({
                icon: 'warning',
                title: '인증 실패',
                text: '아이디를 입력해주세요.',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
            return;
        }
    }
    if (!userEmail) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '이메일을 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
    }
    if (!emailPattern.test(userEmail)) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '이메일을 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }
    //  버튼 비활성화 (중복 클릭 방지)
    $('#password-find').prop("disabled", true);
    
    //  AJAX 요청 (폼 제출 대신 서버로 요청을 보냄)
    $.ajax({
        type: "POST",
        url: "/user/find-password",
        contentType: "application/json",
        data: JSON.stringify({ userEmail: userEmail, userId: userId }),
        dataType : "json",
        success: function (response) {
            Swal.fire({
                icon: 'success',
                title: '성공',
                text: response.message,  // 서버에서 받은 메시지 표시
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            }).then(() => {
                window.location.href = "/user/login"; // 로그인 페이지로 이동
            });
        },
        error: function (xhr) {
            let errorMessage = xhr.responseJSON?.message || "서버 오류가 발생했습니다.";
            Swal.fire({
                icon: 'error',
                title: '오류 발생',
                text: errorMessage,
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
            $('#password-find').prop("disabled", false);
        }
    });
});
