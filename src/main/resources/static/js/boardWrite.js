// Quill 에디터 초기화
var quill = new Quill('#editor-container', {
    theme: 'snow',
    modules: { imageResize: {} }
});
document.getElementById('editor-container').addEventListener('click', function () {
    quill.focus();
});

// 폼 제출 시, 레시피 선택 여부 및 에디터 내 이미지 처리, 파일 첨부 여부 등을 확인
document.getElementById('board-form').onsubmit = function (e) {
    // 레시피 선택 여부 확인
    var recipeSelect = document.getElementById('savedRecipe');
    if (recipeSelect.value.trim() === '') {
        Swal.fire({
            icon: 'error',
            title: '재료 선택 오류',
            text: '최소 1개의 재료를 입력해주세요',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인'
        });
        e.preventDefault();
        return false;
    }

    // 첨부된 이미지 파일이 없으면 alert 창 띄움
    const isDropZoneImagedEmpty = (dropzone.files.length === 0);
	if (isDropZoneImagedEmpty) {
        Swal.fire({
            icon: 'error',
            title: '파일 첨부 오류',
            text: '이미지 파일을 첨부해주세요',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인'
        });
        e.preventDefault();
        return false;
    }

    // 파일이 하나 이상 첨부된 경우, 썸네일 지정 여부 확인
    const isThumbnailNotSelected = (dropzone.files.length > 0 && !document.getElementById('thumbnailUrl').value);
	if (isThumbnailNotSelected) {
        Swal.fire({
            icon: 'error',
            title: '썸네일 선택 오류',
            text: '썸네일로 사용할 사진을 선택해주세요',
            confirmButtonColor: '#ff7f50',
            confirmButtonText: '확인'
        });
        e.preventDefault();
        return false;
    }

     // 업데이트된 HTML을 hidden input에 설정
     document.getElementById('boardContent').value = quill.root.innerHTML;

    return true;
};

let uploadedFiles = new Map();
Dropzone.autoDiscover = false;

const dropzone = new Dropzone('#dropzone', {
    url: '#', // server에 request하지 않게 변경
    autoProcessQueue: false, // server에 request하지 않게 변경
    maxFiles: 5,
    maxFilesize: 5,
    acceptedFiles: 'image/*',
    addRemoveLinks: true,
    dictDefaultMessage: '',
    dictRemoveFile: '삭제',
    init: function () {
        this.on('addedfile', function (file) {
            let reader = new FileReader();
            reader.onload = (event) => {
                let base64 = event.target.result;
                
                // base64로 저장된 값을 확인하여 중복된 이미지  올리기 방지
                for (const [key, value] of uploadedFiles) {
                    if (value.base64 === base64) {
                        Swal.fire({
                            icon: 'error',
                            title: '파일 중복',
                            text: '이미 업로드된 파일입니다.',
                            confirmButtonColor: '#ff7f50',
                            confirmButtonText: '확인'
                        });
                        this.duplicate = true;
                        this.removeFile(file);
                        return;
                    }
                }

                uploadedFiles.set(file.name, { file, base64 });
                insertImageToQuill(file, base64);
            }
            reader.readAsDataURL(file);
            this.element.classList.add('dz-started');
        });

        this.on("sending", function(file, xhr, formData) {
            //upload 시 server에 request하지 않게 변경
            xhr.abort();
        });

        this.on('removedfile', function (file) {
            let fileKey = file.name;
            let fileData = uploadedFiles.get(fileKey);

            //중복 파일이라면 quill에서 제거하지 않고, 새로 upload된 파일이면 제거
            if (!this.duplicate) {
                removeImageFromQuillBoard(fileData.base64);
            } else {
                this.duplicate = false;
                return;
            }

            //dropzone에 image가 사라졌으므로 image가 없음을 알려주기 위해 css 제거
            if (this.files.length === 0) {
                this.element.classList.remove('dz-started');
            }
            uploadedFiles.delete(fileKey);
            preventRequestIfRemovedImageIsThumbnail(fileData.base64);
        });

        this.on('complete', function (file) {
            file.previewElement.classList.add('dz-complete');
        });
    }
});




quill.on('text-change', function () {
    let quillImages = new Set([...quill.root.querySelectorAll('img')].map(img => img.src));
    for (let [fileKey, fileData] of uploadedFiles.entries()) {
        if (!quillImages.has(fileData.base64)) {
            dropzone.removeFile(fileData.file);
            uploadedFiles.delete(fileKey);
            preventRequestIfRemovedImageIsThumbnail(fileData?.base64);
        }
    }
});


/**
 * Inserts an image into Quill.
 *
 * @param {File} file - dropzone file.
 * @param {string} base64 - The base64-encoded image data.
 */
function insertImageToQuill(file, base64) {
    let range = quill.getSelection();
    let insertIndex = range ? range.index : quill.getLength();
    quill.insertEmbed(insertIndex, 'image', base64);
    quill.setSelection(insertIndex + 1);

    let fileKey = file.name;
    uploadedFiles.set(fileKey, { file, base64 });

    file.previewElement.classList.add('dz-complete');

    const thumbnailLabel = generateThumbnailLabel(file);

    file.previewElement.addEventListener('click', function (e) {
        if (e.target.classList.contains('dz-remove')) {
            return;
        }
        updateThumbnailSelection(file, thumbnailLabel, base64);
    });
}

function removeImageFromQuillBoard(base64) {
    let imgToRemove = quill.root.querySelector(`img[src="${base64}"]`);
    if (imgToRemove) {
        imgToRemove.remove();
    }
}

/**
 * 
 * @param {File} file  dropzonefile
 * @param {HTMLElement} thumbnailLabel 
 * @param {base64String} base64 
 */
function updateThumbnailSelection(file, thumbnailLabel, base64) {
    //썸네일 이미지를 바꾸기 위해 이전 썸네일 이미지 표시 css를 초기화
    document.querySelectorAll('.dz-preview').forEach(function (preview) {
        preview.classList.remove('thumbnail-selected');
        let label = preview.querySelector('.thumbnail-label');
        if (label) {
            label.style.display = 'none';
        }
    });
     //새로 선택된 썸네일 이미지를 보여주기 위해 이미지 표시 css 추가
     file.previewElement.classList.add('thumbnail-selected');
     thumbnailLabel.style.display = 'block';
     // 새로 지정한 썸네일이면 hidden input 갱신
    document.getElementById('thumbnail').value = base64;
    document.getElementById('thumbnailUrl').value = base64;
}

function generateThumbnailLabel(file) {
    let thumbnailLabel = document.createElement('div');
    thumbnailLabel.classList.add('thumbnail-label');
    thumbnailLabel.textContent = '썸네일로 지정';
    thumbnailLabel.style.display = 'none';
    file.previewElement.insertBefore(thumbnailLabel, file.previewElement.firstChild);
    return thumbnailLabel;
}

function preventRequestIfRemovedImageIsThumbnail(base64) {
    const currentThumbnailBase64 = document.getElementById('thumbnailUrl').value;
    if (base64 === currentThumbnailBase64) {
        document.getElementById('thumbnailUrl').value = "";
    }
}
