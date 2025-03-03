$(document).ready(function () {
    let currentPage = 0;

    // 최초 게시글 로드
    initRecentPosts(currentPage);

    // "더보기" 버튼 클릭 시 추가 게시글 로드
    $('#show-more-button').click(function () {
        currentPage++;
        initRecentPosts(currentPage);
    });

    function initRecentPosts(page) {
        $.ajax({
            url: '/posts?page=' + page,
            method: 'GET',
            success: function (resp) {
                $('.recentPost').append(generatePostHTML(resp.posts));
                if (resp.isLastPage) {
                    $('#show-more-button').hide(); // 마지막 페이지면 버튼 숨기기
                }
            },
            error: function (error) {
                console.error("게시글을 불러오는 중 에러 발생: ", error);
            }
        });
    }

    function generatePostHTML(posts) {
        let tag = ``;
        $.each(posts, function (index, item) {
            tag += `
            <div class="gallery-item">
                <a class="gallery-link" href="/board/boardDetail?boardSeq=${item.boardSeq}">
                    <img src="${item.boardImageOriginalFileName}" alt="레시피">
                    <div class="gallery-text">
                        <h4>${item.boardTitle}</h4>
                        <span>❤️ ${item.heartCount}</span>
                    </div>
                </a>
            </div>
            `;
        });
        return tag;
    }
});
