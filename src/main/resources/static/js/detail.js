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
});

// 댓글 추가 함수
function addComment() {
    let commentInput = $("#comment-input");
    let commentList = $("#comment-list");

    if (commentInput.val().trim() !== '') {
        let newComment = `<div class="comment">
                            <div class="user-info">익명</div>
                            <div class="user-text">${commentInput.val()}</div>
                          </div>`;
        commentList.append(newComment);
        commentInput.val('');
    }
}

function addComment() {
    let commentInput = document.getElementById('comment-input');
    let commentList = document.getElementById('comment-list');

    if (commentInput.value.trim() !== '') {
        let newComment = document.createElement('div');
        newComment.classList.add('comment');
        newComment.innerHTML = `<div class="user-info">익명</div>
                                        <div class="text">${commentInput.value}</div>`;
        commentList.appendChild(newComment);
        commentInput.value = '';
    }
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
