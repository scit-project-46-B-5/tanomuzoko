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
        Swal.fire({
            icon: 'warning',
            title: '인증 실패.',
            text: '인증번호를 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }
    $.ajax({
        url: "/api/v1/email/verify",
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({ userEmail: email, verifyCode: code }),
        success: function (resp) {
            if (resp) {
                Swal.fire({
                    icon: 'success',
                    title: '인증 성공!',
                    text: '이메일 인증 성공!',
                    confirmButtonColor: '#ff7f50',
                    confirmButtonText: '확인',
                });
                emailCodeCheck = true;  // 인증 성공 시 회원가입 가능
                $("#verificationCode").prop("disabled", true); // 입력 칸 비활성화
                $("#verifyButton").prop("disabled", true); // 인증 버튼 비활성화
                $('#requestButton').prop("disabled", true); // 메일 인증받기 버튼 비활성화
                $('#userEmail').prop("disabled", true); // 메일 주소를 변경 불가능 하게끔 비활성화 처리
            } else {
                Swal.fire({
                    icon: 'warning',
                    title: '인증 실패',
                    text: '인증번호가 올바르지 않습니다.',
                    confirmButtonColor: '#ff7f50',
                    confirmButtonText: '확인',
                });
                emailCodeCheck = false;
                $('#verificationCode').val(''); // 인증 실패 시 입력 필드 초기화
            }
        },
        error: function (xhr, status, error) {
            console.error("인증 실패 응답: ", xhr.responseText);
            Swal.fire({
                icon: 'warning',
                title: '인증 실패',
                text: '인증번호가 올바르지 않습니다.',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
            emailCheck = false;  // 인증 실패 시 회원가입 불가
            $('#verificationCode').val(''); // 인증 실패 시 입력 필드 초기화
        }
    });
}
// 이메일 인증 요청
function mailAuthentication() {
    if (!emailCheck) {
        return;
    }

    // 이메일 입력값 가져오기
    let email = $("#userEmail").val().trim();
    let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    // ✅ 이메일 입력 여부 확인
    if (!email) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '이메일을 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }

    // ✅ 이메일 형식 확인
    if (!emailPattern.test(email)) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '올바른 이메일 형식을 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }

    // ✅ 이메일 중복 검사 요청
    $.ajax({
        url: "/user/emailCheck",  // 이메일 중복 체크 API
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({ "userEmail": email }),
        success: function (isDuplicate) {
            if (isDuplicate) {
                Swal.fire({
                    icon: 'warning',
                    title: '이메일 중복',
                    text: '이미 사용 중인 이메일입니다.',
                    confirmButtonColor: '#ff7f50',
                    confirmButtonText: '확인',
                });
                emailCheck = false;
            } else {
                emailCheck = true; // 중복 없음

                // ✅ 이메일 중복 체크 성공 후, 사용자에게 바로 알림 표시
                Swal.fire({
                    icon: 'success',
                    title: '이메일 인증 요청 완료!',
                    text: '이메일로 인증번호가 발송됩니다.',
                    confirmButtonColor: '#ff7f50',
                    confirmButtonText: '확인',
                });

                // ✅ 이메일 인증번호 요청 실행 (하지만 알림은 띄우지 않음)
                sendEmailVerification(email);
            }
        },
        error: function (xhr) {
            console.error("이메일 중복 확인 실패 응답: ", xhr.responseText);
            Swal.fire({
                icon: 'warning',
                title: '이메일 확인 실패',
                text: '이메일 중복 확인 중 오류가 발생했습니다.\n오류 코드: ' + xhr.status,
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
        }
    });
}

// ✅ 이메일 인증번호 요청 함수 (별도 실행, 알림 X)
function sendEmailVerification(email) {
    // ✅ 버튼 비활성화 (중복 클릭 방지)
    $('#requestButton').prop("disabled", true);

    // ✅ 2초 후 버튼 다시 활성화
    setTimeout(() => {
        $('#requestButton').prop("disabled", false);
    }, 2000);

    $('#verificationBox').css('display', 'block');

    $.ajax({
        url: "/api/v1/email/send",  // 이메일 전송 API
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({ userEmail: email }),
        success: function (data) {
            console.log("📩 이메일이 정상적으로 발송되었습니다.");
            $("#Confirm").attr("value", data);
        },
        error: function (xhr) {
            console.error("이메일 전송 실패 응답: ", xhr.responseText);
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
                $('#confirmName').css('color', '#4CAF50');
                $('#confirmName').html("사용가능한 닉네임");
                userNameCheck = true;
            } else {
                $('#confirmName').css('color', '#F55735');
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
                $('#confirmId').css('color', '#4CAF50');
                $('#confirmId').html("사용가능한 아이디");
                idCheck = true;
            } else {
                $('#confirmId').css('color', '#F55735');
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
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '이메일을 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
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
                Swal.fire({
                    icon: 'warning',
                    title: '인증 실패',
                    text: '중복된 이메일 입니다.',
                    confirmButtonColor: '#ff7f50',
                    confirmButtonText: '확인',
                });
                emailCheck = false;  // 이메일이 중복된 경우
            } else {
                emailCheck = true;   // 이메일이 중복되지 않은 경우
                mailAuthentication();
            }
        },
        error: function (xhr) {
            console.error("이메일 중복 확인 실패 응답: ", xhr.responseText);
            Swal.fire({
                icon: 'warning',
                title: '이메일 확인 실패',
                text: '이메일 중복 확인 중 오류가 발생했습니다. 다시 시도해주세요.\n오류 코드: ' + xhr.status,
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
        }
    });
}
// 회원가입 유효성 검사
function join() {
    // ✅ 이메일 중복 체크가 완료되어야 회원가입을 진행
    if (!emailCheck) {
        Swal.fire({
            icon: 'warning',
            title: '메일 인증 실패',
            text: '이메일 중복을 먼저 확인해 주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return false;
    }
    let userId = $('#userId').val();
    // 아이디 체크
    if (userId.trim().length < 3 || userId.trim().length > 12) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '아이디는 3~12자 사이로 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return false;
    }
    // 비밀번호 체크
    let userPassword = $('#userPassword').val();
    if (userPassword.trim().length < 8 || userPassword.trim().length > 20) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '비밀번호는 8~20자 사이로 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });

        return false;
    }
    let userPwdCheck = $('#userPwdCheck').val();
    if (userPassword !== userPwdCheck) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '비밀번호가 일치하지 않습니다.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return false;
    }
    // 필수 체크박스 검사
    if (!$('#terms').prop('checked') || !$('#privacy').prop('checked') || !$('#age').prop('checked')) {
        Swal.fire({
            icon: 'warning',
            title: '체크 확인',
            text: '필수 항목에 동의해야 가입이 가능합니다.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return false;
    }
    // 이메일 유효성 검사
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
        return false;
    }
    if (!emailPattern.test(userEmail)) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '올바른 형식의 이메일을 입력해주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return false;
    }
    let userName = $('#userName').val();
    if (userName.trim().length < 2 || userName.trim().length > 11) {
        Swal.fire({
            icon: 'warning',
            title: '인증 실패',
            text: '닉네임은 2~11자 사이로 입력해 주세요.',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
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
                // Swal.fire({
                //     icon: 'success',
                //     title: '가입 성공!',
                //     text: '회원가입이 완료되었습니다.',
                //     confirmButtonColor: '#ff7f50',
                //     confirmButtonText: '확인',
                // });
                window.location.href = "/"; // ✅ 메인 페이지로 이동
            },
            error: function (xhr) {
                console.error("회원가입 실패 응답: ", xhr.responseText);
                Swal.fire({
                    icon: 'error', // ❗ 에러 알림 스타일 적용
                    title: '회원가입 실패',
                    text: '회원가입 중 오류가 발생했습니다. 다시 시도해주세요.\n오류 코드: ' + xhr.status,
                    confirmButtonColor: '#ff7f50',
                    confirmButtonText: '확인',
                });
            }
        });
    }
}
