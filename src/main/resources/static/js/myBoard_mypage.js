const showMoreBtn = document.querySelector(".show-more-btn");

let page = 1;

showMoreBtn.addEventListener("click", showBoardLists);

async function showBoardLists() {
    try {
        const response = await fetch(`/mypage/myBoard/more?page=${page}`);
        const data = await response.json();

        if (data.length <= 4 ) {
             showMoreBtn.style.display = "none";
        }

        const gallery = document.querySelector(".gallery");
        data.forEach(board => {
            const galleryItem = document.createElement("div");
            galleryItem.classList.add("gallery-item");
            galleryItem.innerHTML = `
                <a class="gallery-link" href="#">
                    <div class="img-wrapper">
                        <img src="${board.originalFileName}" alt="${board.boardTitle}" />
                    </div>
                    <div class="gallery-text">
                        <h4>${board.boardTitle}</h4>
                        <p>${board.boardContent}</p>
                        <span>❤️ ${board.heartCount}</span>
                    </div>
                </a>
            `;
            gallery.appendChild(galleryItem);
        });

        page++; 
    } catch (error) {
        console.error("게시물 불러오기 실패:", error);
    }
}