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

    $.ajax({
        url: '/user/find-id',
        method: 'POST',
        data: { userEmail: userEmail },
        success: function (response) {
            if (response.status === 'success') {
                // 아이디 찾기 성공 시 모달 띄우기
                openModal('아이디: ' + response.userId);
            } else {
                // 아이디 찾기 실패 시 모달 띄우기
                openModal(response.message);
            }
        },
        error: function () {
            // 서버 오류 처리
            openModal('서버에 문제가 발생했습니다. 다시 시도해주세요.');
        }
    });
})

// 모달 및 닫기 버튼 참조
const modal = document.getElementById("myModal");
const span = document.getElementsByClassName("close")[0];

// 모달을 여는 함수
function openModal(message) {
    document.getElementById("modalMessage").innerText = message;  // 메시지 설정
    modal.style.display = "block";  // 모달 열기
}

// 모달을 닫는 함수
span.onclick = function() {
    modal.style.display = "none";  // 모달 닫기
}

// 모달 외부 클릭 시 모달 닫기
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";  // 모달 닫기
    }
}