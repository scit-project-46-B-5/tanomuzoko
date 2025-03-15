const showMoreBtn = document.querySelector(".show-more-btn");

let page = 1;
    
if (showMoreBtn) {
    showMoreBtn.addEventListener("click", showBoardLists);
}

async function showBoardLists() {
    try {
        const response = await fetch(`/mypage/likedBoard/more?page=${page}`);
        const data = await response.json();

        const gallery = document.querySelector(".gallery");

        data.boards.forEach(board => {
            const galleryItem = document.createElement("div");
            galleryItem.classList.add("gallery-item");
            const boardDetailUrl = `/board/boardDetail?boardSeq=${board.boardSeq}`;

            galleryItem.innerHTML = `
                <a class="gallery-link" href="${boardDetailUrl}">
                    <div class="img-wrapper">
                        <img src="${board.originalFileName}" alt="${board.boardTitle}" />
                    </div>
                    <div class="gallery-text">
                        <h4>${board.boardTitle}</h4>
                        <p>${board.firstLineContent}</p>
                        <span>❤️ ${board.heartCount}</span>
                    </div>
                </a>
            `;
            gallery.appendChild(galleryItem);
        });

        page++; 

        // 더보기 버튼 처리
        if (data.boards.length < 4 || data.totalElements <= (page * 4)) {
            showMoreBtn.style.display = "none";  
        } else {
            showMoreBtn.style.display = "block"; 
        }
    } catch (error) {
        console.error("게시물 불러오기 실패:", error);
    }
}