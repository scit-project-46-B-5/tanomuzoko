let idCheck = false;
let userNameCheck = false;
$(function () {
    $('#userId').on('blur', confirmId);
    $('#check_all').on('change', toggleAllCheckboxes);
    $('.check-item').on('change', handleCheckItemChange);
    $('#userPwd').on('focus', clearPwdCheck);
    $('#joinBtn').on('click', join);
    $('#userName').on('blur', nickNameCheck);
    // 주석부분은 이메일 인증이 완료되면 다시 push 할 예정정
    // $('#requestButton').on('click', mailAuthentication);
});
// // email인증 , 버튼뒤집기기
// function mailAuthentication() {
//     alert('asdasdasd');

//     // 이메일 유효성 검사
//     let email = $("#email").val().trim();
//     let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

//     if (email === "") {
//         alert("이메일을 입력해주세요.");
//         return;
//     }
//     if (!emailPattern.test(email)) {
//         alert("올바른 이메일 형식을 입력해주세요.");
//         return;
//     }
//     // 버튼 중복클릭 방지
//     $('#requestButton').prop("disabled", true);
//     // 버튼 뒤집기
//     $('#verificationBox').css('display', 'block');
//     // $('#emailAuthentication').css('display', 'none');

//     $.ajax({
//         url: "/api/v1/email/send", // 서버에서 제공한 URL 사용
//         type: "post",
//         dataType: "json",
//         data: { "email": email },
//         success: function (data) {
//             alert("인증번호가 이메일로 발송되었습니다.");
//             // 발송된 인증번호 자동 등록
//             $("#Confirm").attr("value", data);
//         },
//         error: function (xhr, status, error) {
//             alert("이메일 전송 실패: " + xhr.responseText);

//             // 🚨 실패 시 버튼 다시 활성화
//             $('#requestButton').prop("disabled", false);
//         }
//     });
// }

// 닉네임 중복체크
function nickNameCheck() {
    let userName = $('#userName').val();
    if (userName === "") {
        $('#confirmName').html("");
        userNameCheck = false;
        return;
    }
    if (userName.trim().length < 2 || userName.trim().length > 11) {
        $('#confirmName').css('color', 'red');
        $('#confirmName').html("닉네임은 2~11자리 사이로 입력");
        return;
    }
    $.ajax({
        url: '/user/nameCheck'
        , method: 'POST'
        , data: { "userName": userName }
        , success: function (resp) {
            if (resp) {
                $('#confirmName').css('color', 'blue');
                $('#confirmName').html("사용가능한 닉네임");
                userNameCheck = true;
            } else {
                $('#confirmName').css('color', 'red');
                $('#confirmName').html("사용 불가능한 닉네임");
                userNameCheck = false;
            }
        }
    });
}
// "전체 동의" 체크 시 모든 체크박스 선택/해제
function toggleAllCheckboxes() {
    $(".check-item").prop("checked", $(this).prop("checked"));
}
// 개별 체크박스 변경 시 "전체 동의" 상태 업데이트
function handleCheckItemChange() {
    let total = $('.check-item').length;
    let checked = $('.check-item:checked').length;
    $('#check_all').prop('checked', total === checked);
}
// 아이디 중복체크
function confirmId() {
    //  아이디 길이체크, 아이디를 적지않았을경우 메세지가 안보이게 설정
    let userId = $('#userId').val();

    if (userId === "") {
        $('#confirmId').html("");
        idCheck = false;
        return;
    }
    if (userId.trim().length < 3 || userId.trim().length > 12) {
        $('#confirmId').css('color', 'red');
        $('#confirmId').html("아이디는 3~12자리 사이로 입력해주세요.");
        return;
    }
    // 아이디 중복체크 
    $.ajax({
        url: '/user/idCheck'
        , method: 'POST'
        , data: { "userId": userId }
        , success: function (resp) {
            if (resp) {
                $('#confirmId').css('color', 'blue');
                $('#confirmId').html("사용가능한 아이디");
                idCheck = true;
            } else {
                $('#confirmId').css('color', 'red');
                $('#confirmId').html("사용 불가능한 아이디");
                idCheck = false;
            }
        }
    });
}
// 비밀번호 입력 시 확인란 초기화
function clearPwdCheck() {
    $('#userPwdCheck').val('');
}
// 회원가입 유효성 검사
function join() {
    let userId = $('#userId').val();
    // 아이디 체크
    if (userId.trim().length < 3 || userId.trim().length > 12) {
        alert('아이디는 3~12자 사이로 입력해주세요.');
        return false;
    }
    // 비밀번호 체크
    let userPassword = $('#userPassword').val();
    if (userPassword.trim().length < 8 || userPassword.trim().length > 20) {
        alert('비밀번호는 8~20자 사이로 입력해주세요.');
        return false;
    }
    let userPwdCheck = $('#userPwdCheck').val();
    if (userPassword !== userPwdCheck) {
        alert('비밀번호가 일치하지 않습니다.');
        return false;
    }
    // 필수 체크박스 검사
    if (!$('#terms').prop('checked') || !$('#privacy').prop('checked') || !$('#age').prop('checked')) {
        alert('필수 항목에 동의해야 가입이 가능합니다.');
        return false;
    }
    // 이메일 유효성 검사
    let email = $("#email").val().trim();
    let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (email === "") {
        alert("이메일을 입력해주세요.");
        return false;
    }
    if (!emailPattern.test(email)) {
        alert("올바른 이메일 형식을 입력해주세요.");
        return false;
    }
    let userName = $('#userName').val();
    if (userName.trim().length < 2 || userName.trim().length > 11) {
        alert('닉네임은 2~11자 사이로 입력해 주세요.')
        return false;
    }
    // 회원가입 요청부분
    if (idCheck || pwdCheck || userName) {

        // 회원가입을 위한 처리요청
        $.ajax({
            url: '/user/joinProc'
            , method: 'POST'
            , data: { "userId": userId, "userPassword": userPassword, "userName": userName }
            , success: function (resp) {
                if (resp) {
                    alert('회원가입 완료')
                } else {
                    alert('회원가입 실패')
                }
            }
        });
    }
}
