function createToggleBookmark() {
    let isClicked = false; // This will persist between clicks
    let recipeSeq = 0; // Initially, there's no recipe sequence

    return async function toggleBookmark() {
        let bookmarkBtn = document.querySelector('.bookmark-btn');
        let bookmarkIcon = document.getElementById('bookmarkIcon');


        if (!bookmarkBtn.classList.contains('bookmarked')) {
            bookmarkBtn.classList.add('bookmarked');
            bookmarkIcon.src = '/image/star2.png'; 
        }

        console.log(isClicked);
        if (isClicked) {
            Swal.fire({
                icon: 'error',
                title: '이미 클릭하셨습니다.',
                text: '서버에서 프로세스가 진행중입니다.',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
            return;
        }

        // Block further requests if recipeSeq is already set (meaning data has been saved)
        if (recipeSeq !== 0) {
            Swal.fire({
                icon: 'warning',
                title: '이미 저장된 데이터입니다.',
                text: '저장은 이미 완료되었습니다.',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });
            return;
        }

        isClicked = true; // Disable further clicks until the API is done
        const title = document.querySelector(".recipe-title").textContent;
        const outputContent = document.querySelector(".recipe-info").innerHTML; // HTML itself as a string
        const usage = document.querySelector("input[name='usage']").value;
        const menu = document.querySelector("input[name='menu']").value;
        const taste = document.querySelector("input[name='taste']").value;
        const level = document.querySelector("input[name='level']").value;
        const nonce = document.querySelector("input[name='nonce']").value;

        const data = {
            title,
            recipeCondition: { usage, menu, taste, level },
            outputContent,
            nonce
        };

        try {
            const response = await fetch('/recipe/history/save', {
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
                method: 'POST'
            });

            if (!response.ok) {
                removeBoomark();
                Swal.fire({
                    icon: 'failure',
                    title: '저장 실패',
                    text: '저장이 완료되지 않았습니다.',
                    confirmButtonColor: '#ff7f50',
                    confirmButtonText: '확인',
                });
            }

            recipeSeq = await response.json(); // Store the server response (recipeSeq)

            Swal.fire({
                icon: 'success',
                title: '저장 성공!',
                text: '저장이 완료되었습니다.',
                confirmButtonColor: '#ff7f50',
                confirmButtonText: '확인',
            });

        } catch (error) {
            removeBoomark();
            console.error("Error:", error);
        } finally {
            isClicked = false; // Re-enable clicking after the response is handled (success or failure)
        }
    };
}

// Attach the function to the button event
const toggleBookmark = createToggleBookmark();
document.querySelector('.bookmark-btn').addEventListener('click', toggleBookmark);

function removeBoomark() {
    document.querySelector('.bookmark-btn').classList.remove('bookmarked');
    document.getElementById('bookmarkIcon').src = '/image/star1.png';
}


// Usage: Assign the closure to an event listener
// const toggleBookmark = createToggleBookmark();
// document.querySelector('.bookmark-btn').addEventListener('click', function() {
//     toggleBookmark();
// });