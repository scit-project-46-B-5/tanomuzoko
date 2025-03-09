// 이메일 유효성 검사

$('#id-find').on('click', function (event) {
    event.preventDefault(); // 기본 제출 방지
    
    let userEmail = $("#userEmail").val().trim();
    let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    
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
            text: '올바른 형식의 이메일을 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }
    // 모든 조건이 통과되면 폼을 제출
    $('#findForm').submit();
})