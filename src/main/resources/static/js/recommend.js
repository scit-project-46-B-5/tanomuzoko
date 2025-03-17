$('#recommend-form').on('submit', function (e) {
    e.preventDefault(); // 폼 제출 방지
    if (!$('#ingredients-input').val().trim()) {
        Swal.fire({
            icon: 'error',
            title: '재료를 선택해주세요',
            text: '최소 1개의 재료를 입력해주세요',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인',
        });
        return;
    }

    const ingredients = document.querySelector("input[name='ingredients']").value;
    const usage = document.querySelector("input[name='usage']:checked").value;
    const menu = document.querySelector("input[name='menu']:checked").value;
    const taste = document.querySelector("input[name='taste']:checked").value;
    const level = document.querySelector("input[name='level']:checked").value;
    const sendData = {
        ingredients,
        recipeConditionDTO: {
            usage,
            menu,
            taste,
            level
        }
    }
    showLoader();
    fetch('/recipe/chatGPT', {
        body:JSON.stringify(sendData),
        headers:{
            'content-type':'application/json'
        },
        method:'post'
    }).then((response) => {
        hideLoader();
        if(!response.ok) {
            Swal.fire({
                icon: 'error',
                title: '오류가 발생하였습니다.',
                text: '네트워크가 연결되어있는지 확인해주세요',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
            return;
        }

        location.href='/recipe/recommend/output';
    })
    .catch((error)=> hideLoader());
});

const ingredients = [
    '가다랑어포',
    '가리비',
    '가자미',
    '가지',
    '간장',
    '간장게장',
    '갈치',
    '감자',
    '갓김치',
    '강냉이',
    '강황',
    '건과일',
    '건어물',
    '게',
    '겨자',
    '견과류',
    '계란',
    '고구마',
    '고기 육수',
    '고사리',
    '고수',
    '고추',
    '고추기름',
    '고추냉이',
    '고추장',
    '고춧가루',
    '곤드레',
    '곤약',
    '골뱅이',
    '공심채',
    '곶감',
    '과메기',
    '과일',
    '과자',
    '광어',
    '국수 면',
    '굴',
    '굴소스',
    '귤',
    '귤잼',
    '그라나파다노 치즈',
    '그래놀라',
    '기름',
    '김',
    '김치',
    '깍두기',
    '깔라만시',
    '깨',
    '깻잎',
    '꼬막',
    '꽁치',
    '꿀',
    '꿀떡',
    '나물',
    '낙지',
    '날치알',
    '낫또',
    '냉면',
    '냉면 육수',
    '냉이',
    '넙치',
    '넛맥 가루',
    '녹말',
    '녹차',
    '누룽지',
    '누텔라',
    '느타리버섯',
    '다슬기',
    '다시마',
    '단무지',
    '달래',
    '닭',
    '닭모래집',
    '당귀',
    '당근',
    '당면',
    '대구',
    '대추',
    '더덕',
    '데리야키 소스',
    '데미그라스 소스',
    '도넛 가루',
    '도다리',
    '도라지',
    '도미',
    '돈가스',
    '돈가스 소스',
    '동치미',
    '동태',
    '돼지갈비',
    '돼지고기',
    '돼지 곱창',
    '돼지 껍질',
    '돼지 다리살',
    '돼지 등뼈',
    '돼지 등심살',
    '돼지 목살',
    '돼지 삼겹살',
    '돼지 차돌박이',
    '된장',
    '두릅',
    '두반장',
    '두부',
    '두유',
    '드레싱',
    '들기름',
    '딸기',
    '딸기잼',
    '딸기 파우더',
    '땅콩버터',
    '땅콩소스',
    '땅콩잼',
    '떡',
    '떡갈비',
    '떡볶이',
    '떡볶이 소스',
    '또띠아',
    '라면',
    '라이스페이퍼',
    '레모나',
    '레몬',
    '리코타 치즈',
    '마',
    '마가린',
    '마늘',
    '마늘종',
    '마늘 파우더',
    '마라',
    '마스카포네 치즈',
    '마시멜로',
    '마요네즈',
    '마장면 소스',
    '마카로니',
    '막걸리',
    '막창',
    '만두',
    '맛살',
    '맛술',
    '망고',
    '망고잼',
    '매생이',
    '매실',
    '매실액',
    '맥주',
    '머스타드 소스',
    '머위',
    '먹태',
    '멍게',
    '메이플 시럽',
    '메추리알',
    '멜론',
    '멸치',
    '명란',
    '명이나물',
    '모짜렐라 치즈',
    '목이버섯',
    '무',
    '무순',
    '무화과',
    '묵',
    '문어',
    '물',
    '물엿',
    '미나리',
    '미더덕',
    '미림',
    '미숫가루',
    '미역',
    '미트볼',
    '민트',
    '밀가루',
    '밀크티 파우더',
    '바게트',
    '바나나',
    '바나나 파우더',
    '바닐라',
    '바베큐 소스',
    '바지락',
    '발사믹',
    '밤',
    '밥',
    '배',
    '배 음료',
    '배추',
    '백년초',
    '뱅어포',
    '버섯',
    '버터',
    '베이글',
    '베이컨',
    '베이킹파우더',
    '병어',
    '복숭아',
    '복숭아 음료',
    '복숭아잼',
    '봄동',
    '부추',
    '부침가루',
    '북어',
    '분모자',
    '불고기 양념',
    '불닭볶음면',
    '불닭볶음면 소스',
    '브로콜리',
    '블루베리',
    '블루베리잼',
    '비빔장',
    '비트',
    '빵',
    '빵가루',
    '사과',
    '사과잼',
    '사이다',
    '산초',
    '살구',
    '삼치',
    '상추',
    '새송이버섯',
    '새우',
    '새우 가루',
    '색소',
    '샐러드',
    '샐러리',
    '생강',
    '생강 파우더',
    '생선',
    '석류',
    '설탕',
    '소고기',
    '소금',
    '소다',
    '소라',
    '소시지',
    '소주',
    '수박',
    '수제비',
    '수프',
    '숙주',
    '순대',
    '술',
    '숭어',
    '스리라차 소스',
    '스키야키 소스',
    '스테이크 소스',
    '스트링 치즈',
    '스파게티면',
    '스프링클',
    '시금치',
    '시나몬',
    '시래기',
    '시리얼',
    '식빵',
    '식초',
    '쌀',
    '쌀가루',
    '쌈무',
    '쌈장',
    '쌍화탕',
    '쑥',
    '쑥갓',
    '아로니아 파우더',
    '아보카도',
    '아스파라거스',
    '아욱',
    '아이스크림',
    '아이스티 파우더',
    '앙금',
    '애호박',
    '야관문',
    '약재',
    '양념 소스',
    '양배추',
    '양상추',
    '양송이버섯',
    '양파',
    '양파즙',
    '어묵',
    '얼갈이',
    '얼음',
    '연근',
    '연어',
    '연유',
    '열무',
    '열무김치',
    '엿',
    '오렌지',
    '오리',
    '오미자',
    '오이',
    '오징어',
    '옥수수',
    '옥수수가루',
    '올리고당',
    '올리브',
    '와인',
    '요구르트',
    '우동면',
    '우럭',
    '우스터 소스',
    '우엉',
    '우유',
    '월계수',
    '유부',
    '유자',
    '은행',
    '이스트',
    '인삼',
    '인절미',
    '자두',
    '자몽',
    '잡채',
    '잣',
    '장조림 소스',
    '잼',
    '적채',
    '전',
    '전복',
    '전분',
    '젓갈',
    '젤라틴',
    '젤리',
    '조개',
    '주꾸미',
    '주스',
    '죽순',
    '쥐포',
    '짜장',
    '짜파게티',
    '쫄면',
    '쯔유',
    '차',
    '참기름',
    '참깨 소스',
    '참나물',
    '참외',
    '참치',
    '채소',
    '채소 육수',
    '청경채',
    '청국장',
    '청주',
    '체다 치즈',
    '체리',
    '초코',
    '초코 시럽',
    '춘장',
    '치즈',
    '치즈떡',
    '치즈케이크',
    '치커리',
    '치킨스톡',
    '치킨 파우더',
    '칠리소스',
    '카레',
    '코코넛',
    '콜라',
    '콩나물',
    '크림',
    '키위',
    '타르타르 소스',
    '탄산수',
    '탕수육',
    '토란',
    '토마토',
    '토마토 소스',
    '톳',
    '튀김',
    '튀김가루',
    '트러플',
    '파',
    '파래',
    '파마산 가루',
    '파슬리',
    '파인애플',
    '파인애플잼',
    '파프리카',
    '파프리카 파우더',
    '팝콘',
    '팥',
    '팽이버섯',
    '페퍼론치노',
    '포도',
    '폰즈 소스',
    '표고버섯',
    '피망',
    '피쉬소스',
    '피자',
    '피클',
    '할라피뇨',
    '핫도그',
    '핫 소스',
    '핫케이크 가루',
    '해삼',
    '해파리',
    '햄',
    '허브',
    '현미',
    '호떡',
    '호박씨',
    '호박잎',
    '호박 파우더',
    '호빵',
    '홍시',
    '홍합',
    '황두장',
    '황태',
    '후추',
];

const ingredientsList = document.getElementById('ingredients-list');
const searchInput = document.getElementById('search-input');

// 재료 목록 필터링 및 표시
function displayIngredients(filter = '') {
    ingredientsList.innerHTML = '';
    ingredients
        .filter((item) => item.includes(filter))
        .forEach((item) => {
            let btn = document.createElement('button');
            btn.textContent = item;
            btn.classList.add('ingredient-btn');

            if (document.querySelector(`[data-item="${item}"]`)) {
                btn.classList.add('selected');
            }

            btn.onclick = () => toggleCartItem(btn, item);
            ingredientsList.appendChild(btn);
        });
}

// 검색 필터링
function filterIngredients() {
    displayIngredients(searchInput.value.trim());
}

// 장바구니 최대 개수 설정
const maxCartItems = 10;

// 재료 추가 및 제거 기능
function toggleCartItem(button, ingredient) {
    const cartBox = document.getElementById('cart-box');
    let existingItem = cartBox.querySelector(`[data-item="${ingredient}"]`);

    if (existingItem) {
        existingItem.remove();
        button.classList.remove('selected');
    } else {
        if ($('#cart-box button').length >= maxCartItems) {
            Swal.fire({
                icon: 'warning',
                title: '장바구니가 가득 찼습니다',
                text: '최대 10개의 재료만 선택할 수 있습니다',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
            return;
        }

        button.classList.add('selected');
        const btn = document.createElement('button');
        btn.innerHTML = `${ingredient} <span class='remove-btn'>✖</span>`;
        btn.setAttribute('data-item', ingredient);
        btn.classList.add('cart-item');

        btn.onclick = function () {
            this.remove();
            button.classList.remove('selected');
            updateCartData();
        };

        cartBox.appendChild(btn);
    }

    updateCartData();
}

// 장바구니 데이터 저장
function updateCartData() {
    let selectedIngredients = [];
    $('#cart-box button').each(function () {
        selectedIngredients.push($(this).attr('data-item'));
    });
    $('#ingredients-input').val(selectedIngredients.join(','));
}

// 옵션 버튼 선택 기능
$(document).on('click', '.option-btn', function () {
    let group = $(this).data('group');
    $(`.option-btn[data-group="${group}"]`).removeClass('selected');
    $(this).addClass('selected');

    //selected 연결
    let optionDom = $(this).siblings("input[type='radio']");
    optionDom.prop('checked', true);

});

const loader = document.querySelector('.loader');

function showLoader() {
  loader.style.display = 'block';
}

function hideLoader() {
  loader.style.display = 'none';
}


// 초기 재료 리스트 표시
displayIngredients();
