// 모달창 요소
const modal = document.getElementById("recipeModal");
const closeBtn = document.querySelector(".close-btn");

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

async function fetchRecipes(currentPage) {
    try {
        const response = await fetch(`/mypage/getRecipeSave?page=${currentPage}`);
        const data = await response.json(); // Assuming JSON response

        renderRecipes(data.content);
        generatePagination(data);

    } catch (error) {
        console.error("Error fetching recipes:", error);
    }
}

function renderRecipes(recipes) {
    const recipeHTML = document.querySelector("#recipe-fetch-region");
    recipeHTML.innerHTML = ""; // Clear previous content
    let html = '';

    recipes.forEach(recipe => {
        const outerHTML = escapeHTML(recipe.outputHTML);
        const createdDateTime = new Date(recipe.createdDateTime).toISOString().split('T')[0];
        html += `<li>
                        <a href="#" class="recipe-item" data-recipe-id="${recipe.id}" data-recipe-output="${outerHTML}">
                            <span class="recipe-name">${recipe.title}</span>
                            <div>
                            `;
        recipe.inputKeywords.forEach(keyword => {
            html += `
                                    <span class="option">${keyword}</span>
                        `;
        })
        html += `
                                    <span class="date">${createdDateTime}</span>
                            </div>
                        </a>
                    </li>
                `;
    });
    recipeHTML.innerHTML = html;
    handlerRecipeItemClick();
}

function generatePagination(resp) {
    let pagination = '';
    let currentPage = resp.number;
    let totalPages = resp.totalPages;
    let groupSize = 10;
    let startPage = Math.floor(currentPage / groupSize) * groupSize;
    let endPage = Math.min(startPage + groupSize, totalPages);

    if (startPage > 0) {
        pagination += `<button onclick="fetchRecipes(${startPage - 1})">◀ 이전</button>`;
    }

    for (let i = startPage; i < endPage; i++) {
        pagination += `<button onclick="fetchRecipes(${i})" class="${i === currentPage ? 'active' : ''}">${i + 1}</button>`;
    }

    if (endPage < totalPages) {
        pagination += `<button onclick="fetchRecipes(${endPage})">다음 ▶</button>`;
    }

    document.querySelector('#pagination').innerHTML= pagination;
}

function handlerRecipeItemClick() {
    for (const eventListener of document.querySelectorAll(".recipe-item")) {
        eventListener.addEventListener("click", function (event) {
            event.preventDefault(); // 페이지 이동 방지
            modal.style.display = "flex";

            const buttonTag = `<div class="footer">
                                    <button onclick="window.location.href='/board/boardWrite'">게시글 작성하기</button>
                                </div>`;
            document.querySelector(".recipe-info").innerHTML = this.dataset.recipeOutput + buttonTag;
        })
    }
}

// Load first page
fetchRecipes(0);

function escapeHTML(str) {
    return str.replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
