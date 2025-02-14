let likeCount = 0;
document.getElementById('like-btn').addEventListener('click', () => {
    likeCount++;
    document.getElementById('like-count').textContent = likeCount;
});

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
