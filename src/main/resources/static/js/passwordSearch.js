$('#password-find').on('click', function (event) {
    event.preventDefault(); // 기본 제출 방지

    let userEmail = $("#userEmail").val().trim();
    let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    let userId = $('#userId').val();

    // 아이디 유효성 체크
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
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '아이디는 3자 이상, 12자 이하이어야 합니다.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }

    // 이메일 유효성 검사
    if (!userEmail) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '이메일을 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }
    if (!emailPattern.test(userEmail)) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '유효한 이메일을 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }

    // 버튼 비활성화 (중복 클릭 방지)
    $('#password-find').prop("disabled", true);

    // 조건을 만족하면 바로 메일 발송 성공 메시지 표시
    Swal.fire({
        icon: 'success',
        title: '메일 발송 완료',
        text: '입력하신 이메일로 인증번호가 발송되었습니다.',
        confirmButtonColor: '#ff7f50',
        confirmButtonText: '확인',
    }).then(() => {
        // 인증번호 발송 후 페이지 리다이렉트 (필요한 경우)
        window.location.href = "/user/verify-email"; // 이메일 인증을 위한 페이지로 이동
    });

    // 이메일 발송 요청을 보내기 위한 AJAX
    $.ajax({
        type: "POST",
        url: "/user/find-password",
        contentType: "application/json",
        data: JSON.stringify({ userEmail: userEmail, userId: userId }),
        dataType: "json",
        success: function (response) {
            // 여기서는 서버로부터 성공적인 응답을 받을 경우 처리
            console.log("메일 발송 요청 성공:", response);
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
            $('#password-find').prop("disabled", false); // 버튼 활성화
        }
    });
});
