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
    if(recipes.length === 0) {
        recipeHTML.innerHTML = 
                                `<div class="noRecipe">
                                    <p>작성한 레시피가 없습니다.</p>
                                </div>`
        return;
    }
    
    recipes.forEach(recipe => {
        const outerHTML = escapeHTML(recipe.outputHTML);
        const createdDateTime = new Date(recipe.createdDateTime)
                                                        .toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' })
                                                        .replace(/. /g, '-')
                                                        .replace('.', ''); 
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
        pagination += `<button onclick="fetchRecipes(${startPage - 1})" id="previous">◀ 이전</button>`;
    }

    for (let i = startPage; i < endPage; i++) {
        pagination += `<button onclick="fetchRecipes(${i})" class="${i === currentPage ? 'active' : ''}">${i + 1}</button>`;
    }

    if (endPage < totalPages) {
        pagination += `<button onclick="fetchRecipes(${endPage})" id="after">다음 ▶</button>`;
    }

    document.querySelector('#pagination').innerHTML= pagination;
}

async function handlerRecipeItemClick() {
    const data = await getRecipeWritten();

    for (const eventListener of document.querySelectorAll(".recipe-item")) {
        eventListener.addEventListener("click", function (event) {
            event.preventDefault(); // 페이지 이동 방지
            modal.style.display = "flex";
            const recipeId = this.dataset.recipeId;
            const recipeData = data.find(item => item.recipeSeq === parseInt(recipeId));

            let buttonTag = '';
            if (recipeData) {
                // If recipeData is found, it means there's a corresponding board, so show "See Board"
                buttonTag = `<div class="footer">
                                <button onclick="window.location.href='/board/boardDetail?boardSeq=${recipeData.boardSeq}'">게시글 보러가기</button>
                                <button onclick="unActivateRecipe(${recipeId})">레시피 삭제하기</button>
                            </div>`;
            } else {
                // If no corresponding board, show "Write Board"
                buttonTag = `<div class="footer">
                                <button onclick="window.location.href='/board/boardWrite?recipeSeq=${recipeId}'">게시글 작성하기</button>
                                <button onclick="unActivateRecipe(${recipeId})">레시피 삭제하기</button>
                            </div>`;
            }

            document.querySelector(".recipe-info").innerHTML = this.dataset.recipeOutput + buttonTag;
        })
    }
}

// Load first page
fetchRecipes(0);

async function unActivateRecipe(recipeSeq) {

    try {
        const response = await fetch(`/recipe/unactivate`, {
            'body':JSON.stringify({recipeSeq}),
            'headers': {
                'content-type' : 'application/json'
            },
            'method' : 'post'
        });
    
        if(!response.ok) {
          return;
        }

        let currentPage = 0;
        for(const item of Array.from(document.querySelector("#pagination").childNodes)) {
            if (item.classList.contains('active')) {
                currentPage = item.textContent;
            }
        }

        closeBtn.click();

        let size = Array.from(document.querySelector("#recipe-fetch-region").children).length;
        if (size === 1) {
            if(currentPage - 1 === 0) {
                fetchRecipes(currentPage - 1);
            } else {
                fetchRecipes(currentPage - 2);
            }
        } else {
            fetchRecipes(currentPage - 1);
        }
    } catch (error) {
        Promise.reject("다음과 같은 문제가 발생하였습니다" , error)
    }
    
}

async function getRecipeWritten() {
    try {
        const response = await fetch(`/mypage/getRecipeWritten`, {
            'method' : 'get'
        });
    
        if(!response.ok) {
          return;
        }

        const data = await response.json();

        return data;
    } catch (error) {
        console.error(first);
    }
}

function escapeHTML(str) {
    return str.replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
