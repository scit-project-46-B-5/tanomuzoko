$(document).ready(function () {
    loadTopLikedPosts();

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