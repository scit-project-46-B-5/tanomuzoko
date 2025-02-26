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

    $.ajax({
        url: '/reply/getReply',
        method: 'GET',
        data: { "boardSeq": boardSeq },
        success: function (resp) {
            let tag = ``;
            $.each(resp, function (index, item) { 
                tag += `
                <div class="comment">
                    <div class="user-info">${item['replyWriter']}</div>
                    <div class="user-text">${item['replyContent']}</div>
                    <div><button onclick="deleteReply(${item['replySeq']})">삭제</button></div>
                </div>
                `
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
        method: 'GET',
        data: { "replySeq": replySeq },
        success: initReplies
    });
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
