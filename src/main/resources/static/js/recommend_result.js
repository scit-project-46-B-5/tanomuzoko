// ⭐ 즐겨찾기 기능
function toggleBookmark() {
    let bookmarkBtn = document.querySelector('.bookmark-btn');
    let bookmarkIcon = document.getElementById('bookmarkIcon');

    // 즐겨찾기 상태 토글
    if (bookmarkBtn.classList.contains('bookmarked')) {
        bookmarkBtn.classList.remove('bookmarked');
        bookmarkIcon.src = '../../static/image/star1.png'; // 기본 이미지
    } else {
        bookmarkBtn.classList.add('bookmarked');
        bookmarkIcon.src = '../../static/image/star2.png'; // 활성화된 이미지
    }
}
