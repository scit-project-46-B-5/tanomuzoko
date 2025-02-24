$(document).ready(function () {
    let currentPage = 0;
    let currentFilter = "weekly"; // 기본값: 최신 게시글

    // 초기 로드 (최신 게시글 + 인기 게시글 + 공감 수 상위 게시글)
    initRecentPosts(currentPage);
    initPopularPosts(currentFilter); // 기본값: 주간 인기글
    loadTopLikedPosts();

    // "더보기" 버튼 클릭 시 추가 최신 게시글 로드
    $('#show-more-button').click(function () {
        currentPage++;
        initRecentPosts(currentPage);
    });

    // "주간 인기글" 버튼 클릭 시
    $('#weekly-button').click(function () {
        currentFilter = "weekly";
        initPopularPosts(currentFilter);
    });

    // "월간 인기글" 버튼 클릭 시
    $('#monthly-button').click(function () {
        currentFilter = "monthly";
        initPopularPosts(currentFilter);
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
            }
        });
    }

    // 주간/월간 인기 게시글 불러오기
    function initPopularPosts(filter) {
        $.ajax({
            url: `/top-posts?filter=${filter}`,
            method: 'GET',
            success: function (resp) {
                $('.popularPost').html(generatePostHTML(resp));
            }
        });
    }

    function generatePostHTML(posts) {
        let tag = ``;
        $.each(posts, function (index, item) {
            tag += `
            <div class="gallery-item">
                <a class="gallery-link" href="/board/boardDetail?boardSeq=${item['boardSeq']}">
                    <img src="" alt="레시피">
                    <div class="gallery-text">
                        <h4>${item['boardTitle']}</h4>
                        <span>❤️ ${item['heartCount']}</span>
                    </div>
                </a>
            </div>
        `;
        });
        return tag;
    }

    function loadTopLikedPosts() {
        $.ajax({
            url: "/top-liked",
            method: "GET",
            success: function (resp) {
                $('.slider').html(generateSlideHTML(resp));

                // 슬라이드 초기화
                if ($('.slider').hasClass('slick-initialized') === false) {
                    $('.slider').slick({
                        slidesToShow: 1, // 한 번에 1개의 슬라이드 표시
                        slidesToScroll: 1, // 한 번에 1개의 슬라이드씩 스크롤
                        vertical: true, // 수직 슬라이드 활성화
                        infinite: true, // 무한 반복
                        arrows: true, // 화살표 네비게이션 활성화
                        dots: true, // 하단 페이지네이션 활성화
                        autoplay: true, // 자동 슬라이드
                        autoplaySpeed: 3000, // 슬라이드 전환 시간 (3초)
                    });
                } else {
                    $('.slider').slick('slickRemove', null, null, true);
                    $('.slider').slick('unslick');
                    $('.slider').html(generateSlideHTML(resp));
                    $('.slider').slick({
                        slidesToShow: 1,
                        slidesToScroll: 1,
                        vertical: true,
                        infinite: true,
                        arrows: true,
                        dots: true,
                        autoplay: true,
                        autoplaySpeed: 3000,
                    });
                }
            }
        });
    }

    function generateSlideHTML(posts) {
        let tag = ``;
        $.each(posts, function (index, item) {
            tag += `
                <div>
                    <div class="footer">
                        <div class="badge">랭킹 ${index + 1}위</div>
                        <span>${item['boardTitle']}</span>
                        <a href="/board/boardDetail?boardSeq=${item['boardSeq']}">게시글 바로가기 →</a>
                    </div>
                </div>
            `;
        });
        return tag;
    }
});