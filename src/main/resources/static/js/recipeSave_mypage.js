 const recipeItems = document.querySelectorAll(".recipe-item");

        // 모달창 요소
        const modal = document.getElementById("recipeModal");
        const closeBtn = document.querySelector(".close-btn");

        recipeItems.forEach(item => {
            item.addEventListener("click", function (event) {
                    event.preventDefault(); // 페이지 이동 방지
                    modal.style.display = "flex";
                })
        });

        // 모달창 닫기 버튼
        closeBtn.addEventListener("click", function () {
            modal.style.display = "none";
        });

        // 모달창 외부 클릭 시 닫기
        window.addEventListener("click", function (event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        });