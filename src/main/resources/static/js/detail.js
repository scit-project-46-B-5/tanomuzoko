$(document).ready(function () {
    const boardSeq = new URLSearchParams(window.location.search).get("boardSeq");

    if (!boardSeq) return;

    // 공감 상태 및 개수 불러오기
    function fetchHeartStatus() {
        $.ajax({
            url: `/heart/status?boardSeq=${boardSeq}`,
            type: "GET",
            success: function (response) {
                updateHeartUI(response.isHearted, response.heartCount);
            },
            error: function () {
                console.error("공감 상태를 불러오지 못했습니다.");
            }
        });
    }

    // 공감 버튼 클릭 이벤트
    $("#like-btn").click(function () {
        $.ajax({
            url: `/heart/toggle?boardSeq=${boardSeq}`,
            type: "POST",
            success: function (response) {
                updateHeartUI(response.isHearted, response.heartCount);
            },
            error: function () {
                console.error("공감 요청에 실패했습니다.");
            }
        });
    });

    // UI 업데이트 함수
    function updateHeartUI(isHearted, heartCount) {
        if (isHearted) {
            $("#like-btn").addClass("liked").text("❤️ 취소 " + heartCount);
            
        } else {
            $("#like-btn").removeClass("liked").text("🤍 공감 " + heartCount);
        }
    }

    // 페이지 로딩 시 공감 상태 가져오기
    fetchHeartStatus();

    // 댓글 초기화( 댓글 전체 조회 )
    initReplies();
});

// 댓글 초기화
function initReplies() {
    let boardSeq = $('#boardSeq').val();
    let loginId = $('#loginId').val();

    $.ajax({
        url: '/reply/getReply',
        method: 'GET',
        data: { "boardSeq": boardSeq },
        success: function (resp) {
            let tag = ``;
            $.each(resp, function (index, item) { 
                tag += `
                <div class="comment" data-reply-seq="${item['replySeq']}">
                    <div class="user-info">${item['replyWriter']}</div>
                    <div class="user-text">${item['replyContent']}</div>
                `;

                if (loginId === item['userId']) {
                    tag += `
                        <div>
                            <button onclick="deleteReply(${item['replySeq']})">삭제</button>
                            <button onclick="editReply(${item['replySeq']}, '${item['replyContent']}')">수정</button>
                        </div>
                    `;
                }

                tag += `</div>`;              
            })
            $('#comment-list').html(tag);
        }
    })
}

// 댓글 추가 함수
function addReply() {
    let commentInput = $("#comment-input").val();
    let boardSeq = $('#boardSeq').val();

    if (commentInput.trim() == '') {
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
    let answer = confirm('삭제하시겠습니까?');

    if (!answer) {
        return;
    }
    $.ajax({
        url: '/reply/deleteReply',
        method: 'POST',
        data: { "replySeq": replySeq },
        success: initReplies
    });
}

// 댓글 수정 함수
function editReply(replySeq, replyContent) {
    let $commentDiv = $(`.comment[data-reply-seq="${replySeq}"]`);
    
    let editForm = `
        <div class="edit-form">
            <input type="text" id="edit-input-${replySeq}" value="${replyContent}">
            <button onclick="updateReply(${replySeq})">저장</button>
            <button onclick="cancelEdit(${replySeq}, '${replyContent}')">취소</button>
        </div>
    `;

    $commentDiv.find('.user-text').hide();
    $commentDiv.find('div:last-child').hide();
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
            initReplies();
        }
    });
}

// 댓글 수정 취소 함수
function cancelEdit(replySeq, originalContent) {
    let $commentDiv = $(`.comment[data-reply-seq="${replySeq}"]`);

    $commentDiv.find('.edit-form').remove();
    $commentDiv.find('.user-text').text(originalContent).show();
    $commentDiv.find('div:last-child').show();
}

// 작성자의 다른 글을 동적으로 추가하는 함수
function loadAuthorPosts(author) {
    let authorPosts = document.getElementById('author-posts');
    authorPosts.innerHTML = `
                <li><a href="#">${author}의 초간단 오므라이스</a></li>
                <li><a href="#">${author}의 감바스 만들기</a></li>
                <li><a href="#">${author}의 특별한 샐러드</a></li>
            `;
}

// 작성자 정보 가져와서 적용
let authorName = document.getElementById('author-name').textContent;
loadAuthorPosts(authorName);
