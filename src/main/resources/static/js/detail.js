$(document).ready(function () {
    // 현재 페이지 URL에서 boardSeq 값을 가져옴 (예: ?boardSeq=1234)
    const boardSeq = new URLSearchParams(window.location.search).get("boardSeq");
    let currentPage = 0;
    let isLoggedIn = false;

    if (!boardSeq) {
        return; // 게시글 번호가 없으면 함수 종료
    }

    // 공감 상태 및 개수 불러오기
    function fetchHeartStatus() {
        $.ajax({
            url: `/heart/status?boardSeq=${boardSeq}`,
            type: "GET",
            success: function (resp) {
                updateHeartUI(resp.isHearted, resp.heartCount);
                isLoggedIn = resp.isLoggedIn; // 로그인 여부 저장

                if (!resp.isLoggedIn) {
                    $("#like-btn").prop("disabled", true); // 로그인하지 않은 유저는 버튼 비활성화
                }
            },
            error: function () {
                console.error("공감 상태를 불러오지 못했습니다.");
            }
        });
    }

    // 공감 버튼 클릭 이벤트 (사용자가 버튼을 클릭하면 공감 상태 변경)
    $("#like-btn").click(function () {
        if ($(this).prop("disabled")) {
            return;
        }

        $.ajax({
            url: `/heart/toggle?boardSeq=${boardSeq}`,
            type: "POST",
            success: function (resp) {
                updateHeartUI(resp.isHearted, resp.heartCount);
            },
            error: function () {
                console.error("공감 요청에 실패했습니다.");
            }
        });
    });

    // UI 업데이트 함수 (공감 버튼 상태 변경)
    function updateHeartUI(isHearted, heartCount) {
        if (isHearted) {
            $("#like-btn").addClass("liked").text("❤️ 취소 " + heartCount).css("font-family", "NPSfontBold, sans-serif");
            
        } else {
            $("#like-btn").removeClass("liked").text("🤍 공감 " + heartCount).css("font-family", "NPSfontBold, sans-serif");
        }
    }

    // 페이지 로딩 시 공감 상태 가져오기
    fetchHeartStatus();

    // 댓글 초기화( 댓글 전체 조회 )
    initReplies();
});

// 댓글 초기화 (페이지네이션 포함)
function initReplies(page = 0) {
    let boardSeq = $('#boardSeq').val();
    let loginId = $('#loginId').val();

    $.ajax({
        url: '/reply/getReplies',
        method: 'GET',
        data: { "boardSeq": boardSeq, "page": page },
        success: function (resp) {
            currentPage = page;
            let tag = ``;
            $.each(resp.content, function (index, item) {
                tag += renderComment(item, loginId, item.parentReplySeq !== null);
            });
            
            $('#comment-list').html(tag);
            generatePagination(resp);
        }
    })
}

function renderComment(item, loginId, isChild) {
    let indentStyle = item.parentReplySeq ? 'style="margin-left: 30px;"' : '';
    let fullText = escapeHTML(item.replyContent);
    let hasMore = fullText.length > 50; // 50자 이상일 때만 "더보기" 버튼 생성

    let tag = `
        <div class="comment" ${indentStyle} data-reply-seq="${item.replySeq}">
            <div class="user-info">${escapeHTML(item.replyWriter)}</div>
            <div class="user-text">
                <span class="full-text">${fullText}</span>
                ${hasMore ? '<button class="more-btn" onclick="toggleExpand(this)">더보기</button>' : ''}
            </div>
    `;

    if (!isChild && loginId) {
        tag += `<button class="reply-btn" onclick="showReplyForm(${item.replySeq})">답글</button>`;
    }

    if (loginId === item.userId) {
        tag += `
            <div>
                <button class="edit-input-btn" onclick="deleteReply(${item.replySeq})">삭제</button>
                <button class="edit-cancel-btn" onclick="editReply(${item.replySeq}, '${escapeHTML(
            item.replyContent
        )}')">수정</button>
            </div>
        `;
    }

    tag += `</div>`;
    tag += `<div id="reply-form-${item.replySeq}" class="reply-form" style="display: none; margin-left: 30px;"></div>`;

    return tag;
}

function toggleExpand(button) {
    let textElement = button.previousElementSibling;
    textElement.classList.toggle('expanded');
    button.innerText = textElement.classList.contains('expanded') ? '접기' : '더보기';
}

// 답글 입력 폼 표시
function showReplyForm(parentReplySeq) {
    let form = `
    <div class="comment">
        <div class="reply-form">
            <input type="text" class="reply-input" data-parent-reply="${parentReplySeq}" placeholder="답글을 입력하세요">
            <button class="reply-submit-btn" data-parent-reply="${parentReplySeq}">등록</button>
            <button class="reply-cancel-btn" onclick="cancelReplyForm(${parentReplySeq})">취소</button>
        </div>
    </div>
    `;
    $(`#reply-form-${parentReplySeq}`).html(form).toggle();
    $(`.comment[data-reply-seq="${parentReplySeq}"] .reply-btn`).hide(); // 답글 버튼 숨김
}

// 답글 폼 취소 함수
function cancelReplyForm(parentReplySeq) {
    $(`#reply-form-${parentReplySeq}`).html("").hide();
    $(`.comment[data-reply-seq="${parentReplySeq}"] .reply-btn`).show();
}

// 답글 등록 (동적 이벤트 핸들링)
$(document).on("click", ".reply-submit-btn", function () {
    let parentReplySeq = $(this).data("parent-reply");
    let commentInput = $(`.reply-input[data-parent-reply="${parentReplySeq}"]`).val();
    let boardSeq = $('#boardSeq').val();

    if (commentInput.trim() == '' || commentInput.trim().length > maxContentLength) {
        Swal.fire({
            icon: 'warning',
            title: '등록할 수 없습니다',
            text: '1~300자 이내로 작성해주세요',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }

    $.ajax({
        url: '/reply/addReply',
        method: 'POST',
        data: {
            "boardSeq": boardSeq,
            "replyContent": commentInput,
            "parentReplySeq": parentReplySeq || null
        },
        success: function () {
            initReplies(currentPage);
        }
    });
});

// 댓글 내용 토글 (자세히 보기 / 간략히 보기)
function toggleText(button) {
    let commentDiv = $(button).closest(".user-text");
    let shortText = commentDiv.find(".short-text");
    let fullText = commentDiv.find(".full-text");

    if (shortText.is(":visible")) {
        shortText.hide();
        fullText.show();
        $(button).text("간략히 보기");
    } else {
        shortText.show();
        fullText.hide();
        $(button).text("자세히 보기");
    }
}

// 페이지네이션 버튼 생성 함수
function generatePagination(resp) {
    let pagination = '';
    let currentPage = resp.number;
    let totalPages = resp.totalPages;
    let groupSize = 10;
    let startPage = Math.floor(currentPage / groupSize) * groupSize;
    let endPage = Math.min(startPage + groupSize, totalPages);

    if (startPage > 0) {
        pagination += `<button onclick="initReplies(${startPage - 1})">◀ 이전</button>`;
    }

    for (let i = startPage; i < endPage; i++) {
        pagination += `<button onclick="initReplies(${i})" class="${i === currentPage ? 'active' : ''}">${i + 1}</button>`;
    }

    if (endPage < totalPages) {
        pagination += `<button onclick="initReplies(${endPage})">다음 ▶</button>`;
    }

    $('#pagination').html(pagination);
}

// 댓글 최대 글자 수 설정
const maxContentLength = 300;

// 댓글 추가 함수
function addReply() {
    let commentInput = $("#comment-input").val();
    let boardSeq = $('#boardSeq').val();

    if (commentInput.trim() == '' || commentInput.trim().length > maxContentLength) {
        Swal.fire({
            icon: 'warning',
            title: '등록할 수 없습니다',
            text: '1~300자 이내로 작성해주세요',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }

    $.ajax({
        url: '/reply/addReply',
        method: 'POST',
        data: { "boardSeq": boardSeq, "replyContent": commentInput },
        success: function () {
            initReplies();
            $("#comment-input").val('');
        }
    })
}

// 댓글 삭제 함수
function deleteReply(replySeq) {
    Swal.fire({
        title: '정말로 삭제하시겠습니까?',
        text: "다시 되돌릴 수 없습니다. 신중하세요.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '승인',
        cancelButtonText: '취소',
        reverseButtons: false, // 버튼 순서 거꾸로
        
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire(
                '삭제 되었습니다.',
                '댓글이 삭제되었습니다.',
                'success'
            )
            $.ajax({
                url: '/reply/deleteReply',
                method: 'POST',
                data: { "replySeq": replySeq },
                success: function () {
                    initReplies();
                }
            });
        }

    });

}

// 댓글 수정 함수
function editReply(replySeq, replyContent) {
    let $commentDiv = $(`.comment[data-reply-seq="${replySeq}"]`);
    
    let editForm = `
        <div class="edit-form">
            <input class="edit-input" type="text" id="edit-input-${replySeq}" value="${replyContent}">
            <button class = "edit-input-btn "onclick="updateReply(${replySeq})">저장</button>
            <button class = "edit-cancel-btn" onclick="cancelEdit(${replySeq}, '${replyContent}')">취소</button>
        </div>
    `;

    $commentDiv.find('.user-text').hide();
    $commentDiv.find('div:last-child').hide();
    $commentDiv.find('.reply-btn').hide();
    $commentDiv.append(editForm);
}

// 댓글 수정 요청
function updateReply(replySeq) {
    let newContent = $(`#edit-input-${replySeq}`).val();

    if (newContent.trim() === '') {
        alert('댓글 내용을 입력해주세요.');
        return;
    }

    $.ajax({
        url: '/reply/updateReply',
        method: 'POST',
        data: { "replySeq": replySeq, "replyContent": newContent },
        success: function () {
            initReplies(currentPage);
        }
    });
}

// 댓글 수정 취소 함수
function cancelEdit(replySeq, originalContent) {
    let $commentDiv = $(`.comment[data-reply-seq="${replySeq}"]`);

    $commentDiv.find('.edit-form').remove();
    $commentDiv.find('.user-text').text(originalContent).show();
    $commentDiv.find('div:last-child').show();
    $commentDiv.find('.reply-btn').show();
}

// XSS 방지용 문자열 이스케이프 함수
function escapeHTML(str) {
    return str.replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}