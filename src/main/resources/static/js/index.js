$(document).ready(function () {
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
    }
});
