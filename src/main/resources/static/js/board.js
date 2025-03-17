let currentPage = 0;
let currentSearch = "";

// 최초 게시글 로드
initRecentPosts(currentPage, currentSearch);

// "더보기" 버튼 클릭 시 추가 게시글 로드
$('#show-more-button').click(function () {
    currentPage++;
    initRecentPosts(currentPage, currentSearch);
});

// 🔍 검색 버튼 클릭 시 검색 실행
$('.search-btn').click(function () {
    performSearch();
});

// 🔍 검색창에서 Enter 키 입력 시 검색 실행
$('#search-input').keypress(function (event) {
    if (event.which === 13) { // Enter 키 (13)
        performSearch();
    }
});

function performSearch() {
    let searchQuery = $('#search-input').val().trim(); // 검색어 가져오기
    if (searchQuery === currentSearch) { // 이전 검색어와 같으면 실행 X
        return;
    }

    currentSearch = searchQuery; // 현재 검색어 업데이트
    currentPage = 0; // 페이지 초기화
    $('.recentPost').empty(); // 기존 게시글 초기화
    $('#show-more-button').show(); // "더보기" 버튼 다시 보이게 설정
    initRecentPosts(currentPage, currentSearch); // 검색 결과 불러오기
}

function initRecentPosts(page, search) {
    $.ajax({
        url: `/posts?page=${page}&search=${encodeURIComponent(search)}`,
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
                <a class="gallery-link" href="/board/boardDetail?boardSeq=${escapeHTML(item.boardSeq.toString())}">
                    <img src="${escapeHTML(item.boardImageOriginalFileName)}" alt="레시피">
                    <div class="gallery-text">
                        <h4>${escapeHTML(item.boardTitle)}</h4>
                        <span>❤️ ${escapeHTML(item.heartCount.toString())}</span>
                    </div>
                </a>
            </div>
            `;
    });
    return tag;
}

// XSS 방지용 문자열 이스케이프 함수
function escapeHTML(str) {
    return str.replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
