let idCheck = false;
let userNameCheck = false;
let emailCheck = false;
let emailCodeCheck = false;

$('#userId').on('keyup', confirmId);
$('#check_all').on('change', toggleAllCheckboxes);
$('.check-item').on('change', handleCheckItemChange);
$('#userPassword').on('focus', clearPwdCheck);
$('#requestButton').on('click', emailDuplication);
$('#joinBtn').on('click', join);
$('#userName').on('keyup', nickNameCheck);
$('#verifyButton').on('click', verifyCode);

// 입력값과 인증코드가 같은지 확인
function verifyCode() {
    let email = $('#userEmail').val().trim();
    let code = $('#verificationCode').val().trim();

    if (!code) {
        alert('인증번호를 입력해주세요.')
        return;
    }
    $.ajax({
        url: "/api/v1/email/verify",
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({ email: email, verifyCode: code }),
        success: function (resp) {
            if (resp) {
                alert("이메일 인증 성공!");
                emailCodeCheck = true;  // 인증 성공 시 회원가입 가능
                $("#verificationCode").prop("disabled", true); // 입력 칸 비활성화
                $("#verifyButton").prop("disabled", true); // 인증 버튼 비활성화
            } else {
                alert("인증 실패: 인증번호가 올바르지 않습니다.");
                emailCodeCheck = false;
                $('#verificationCode').val(''); // 인증 실패 시 입력 필드 초기화
            }
        },
        error: function (xhr, status, error) {
            console.error("인증 실패 응답: ", xhr.responseTexts)
            alert("인증 실패: 인증번호가 올바르지 않습니다.");
            emailCheck = false;  // 인증 실패 시 회원가입 불가
            $('#verificationCode').val(''); // 인증 실패 시 입력 필드 초기화
        }
    });
}

// email인증 , 버튼뒤집기기
function mailAuthentication() {
    if (!emailCheck) {
        return;
    }
    // 이메일 유효성 검사
    let email = $("#userEmail").val().trim();
    let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!email) {
        alert("이메일을 입력해주세요.");
        return;
    }
    if (!emailPattern.test(email)) {
        alert("올바른 이메일 형식을 입력해주세요.");
        return;
    }
    // ✅ 버튼 비활성화 (중복 클릭 방지)
    $('#requestButton').prop("disabled", true);

    // ✅ 1초 후 버튼 다시 활성화
    setTimeout(() => {
        $('#requestButton').prop("disabled", false);
    }, 2000);
    $('#verificationBox').css('display', 'block');

    $.ajax({
        url: "/api/v1/email/send",
        type: "POST",
        contentType: "application/json",
        dataType: "json",  // ✅ JSON 응답을 받을 수 있도록 설정
        xhrFields: {
            withCredentials: true  // ✅ 인증 정보를 포함하여 요청
        },
        data: JSON.stringify({ email: email }),
        success: function (data) {
            alert("인증번호가 이메일로 발송되었습니다.");
            $("#Confirm").attr("value", data);
            // alert(data.message); // ✅ JSON 응답에서 메시지 출력
        },
        error: function (xhr, status, error) {
            alert("이메일 전송 실패: " + xhr.responseText + "\n상태 코드: " + xhr.status);
        }
    });
}

// 닉네임 중복체크
function nickNameCheck() {
    let userName = $('#userName').val();
    if (!userName) {
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

    if (!userId) {
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
// ✅ 이메일 중복 체크 후 진행
function emailDuplication() {
    let userEmail = $("#userEmail").val().trim();

    if (!userEmail) {
        alert("이메일을 입력해주세요.");
        return;
    }

    // 이메일 중복 체크 요청
    $.ajax({
        url: "/user/emailCheck", // 이메일 중복 체크를 위한 API
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({ "userEmail": userEmail }),  // 이메일 데이터를 JSON 형식으로 전송
        success: function (isDuplicate) {
            if (isDuplicate) {
                alert("이미 사용 중인 이메일입니다. 다른 이메일을 입력해주세요.");
                emailCheck = false;  // 이메일이 중복된 경우
            } else {
                emailCheck = true;   // 이메일이 중복되지 않은 경우
                mailAuthentication();
            }
        },
        error: function (xhr) {
            alert("이메일 중복 확인 실패: " + xhr.responseText);
        }
    });
}
// 회원가입 유효성 검사
function join() {
    // ✅ 이메일 중복 체크가 완료되어야 회원가입을 진행
    if (!emailCheck) {
        alert("이메일 중복을 먼저 확인해 주세요.");
        return false;
    }
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
    let userEmail = $("#userEmail").val().trim();
    let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!userEmail) {
        alert("이메일을 입력해주세요.");
        return false;
    }
    if (!emailPattern.test(userEmail)) {
        alert("올바른 이메일 형식을 입력해주세요.");
        return false;
    }
    let userName = $('#userName').val();
    if (userName.trim().length < 2 || userName.trim().length > 11) {
        alert('닉네임은 2~11자 사이로 입력해 주세요.')
        return false;
    }
    // 회원가입을 위한 처리요청
    if (idCheck && userNameCheck && emailCodeCheck) {
        $.ajax({
            url: '/user/joinProc',
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify({
                "userId": userId,
                "userPassword": userPassword,
                "userName": userName,
                "userEmail": userEmail
            }),
            success: function (resp) {
                alert(resp); // "회원가입이 완료되었습니다." 메시지 출력
                window.location.href = "/"; // ✅ 메인 페이지로 이동
            },
            error: function (xhr) {
                alert("회원가입 실패: " + xhr.responseText);
            }
        });
    }
}
