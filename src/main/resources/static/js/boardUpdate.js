// Quill 에디터 초기화 (boardContent HTML이 표시됨)
let quill = new Quill('#editor-container', {
    theme: 'snow',
    modules: { imageResize: {} }
});
document.getElementById('editor-container').addEventListener('click', function () {
    quill.focus();
});

// 폼 제출 시, 에디터의 최신 HTML을 hidden 필드에 저장하고 검증
document.getElementById('board-form').onsubmit = function (e) {
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

    return true;
};

let uploadedFiles = new Map();
Dropzone.autoDiscover = false;

const dropzone = new Dropzone('#dropzone', {
    url: '/board/upload',
    maxFiles: 5,
    maxFilesize: 5,
    acceptedFiles: 'image/*',
    addRemoveLinks: true,
    dictDefaultMessage: '',
    dictRemoveFile: '삭제',
    init: function () {
        let dropzoneInstance = this;

        // load 시에 thymeleaf로 그린 HTML에서 preload에 담을 image를 가져옴. load 시에 한번만 작동
        const editor = document.querySelector('.ql-editor');
        const imgElements = editor.querySelectorAll('img');
        const imgSrcs = Array.from(imgElements).map(img => img.getAttribute('src') || '');
        imgSrcs.forEach((base64Image, index) => {
            // 저장한 image를 dropzone에 preload를 적용 
            let mockFile = {
                name: "uploaded-image-" + index, 
                size: 0, 
                type: 'image/*'
            };
            dropzoneInstance.emit("addedfile", mockFile);
            dropzoneInstance.emit("thumbnail", mockFile, base64Image);
            dropzoneInstance.files.push(mockFile);
            mockFile.previewElement.classList.add('dz-complete'); 

            // thumbnail인 이미지는 thumnail 표시 CSS 적용
            let thumbnailLabel = generateThumbnailLabel(mockFile);
            setThumbnailOnLoad(mockFile, thumbnailLabel, base64Image);

            // dropzone click 시 thumbnailImage 변경되게 적용
            handleClickDropzoneImage(mockFile, thumbnailLabel, base64Image);


            // dropzone image에서 제거 버튼 누르면 dropzone에서 삭제되게 적용
            let removeButton = mockFile.previewElement.querySelector(".dz-remove");
            handleDropzoneImageRemoveBtn(removeButton, dropzoneInstance, mockFile, base64Image);

            // Base64 문자열을 디코딩하여 바이너리 데이터로 변환하는 함수
            const file = convertBase64ToFile(base64Image, mockFile);

            //나중에 fileUrl도 전부 추가해야함.
            const fileKey = mockFile.name;
            uploadedFiles.set(fileKey, { file: file, base64: base64Image, url: base64Image  });
        });
    
        this.on('addedfile', function (file) {
            
            let reader = new FileReader();
            reader.onload = (event) => {
                let base64Data = event.target.result;
                
                // base64로 저장된 값을 확인하여 중복된 이미지  올리기 방지
                for (const [key, value] of uploadedFiles) {
                    if (value.base64 === base64Data) {
                        Swal.fire({
                            icon: 'error',
                            title: '파일 중복',
                            text: '이미 업로드된 파일입니다.',
                            confirmButtonColor: '#ff7f50',
                            confirmButtonText: '확인'
                        });
                        this.removeFile(file);
                        return;
                    }
                }
            }
            reader.readAsDataURL(file);
            this.element.classList.add('dz-started');
        });

        //file dropzone에 올려서 upload 성공 시 발동하는 event listener
        this.on('success', function (file, response) {
            let fileUrl = response.fileUrl;
            const reader = new FileReader();
            reader.onload = function (e) {
                let base64Data = e.target.result;
                insertImageToQuill(file, base64Data, fileUrl);
            };
            reader.readAsDataURL(file);
        });

        //file dropzone에 올려서 upload 실패 시 발동하는 event listener
        this.on('error', function (file, errorMessage) {
            console.error('업로드 실패:', errorMessage);
        });

        //file dropzone에서 삭제했을 때 발생하는 event lisneter
        this.on('removedfile', function (file) {
            let fileKey = file.name;
            let fileData = uploadedFiles.get(fileKey);
            if (!fileData) {
                return;
            }

            /*
                fileData가 저장되어 있는 경우 quill에서도 제거
                만약 기존 저장된 preload가 아닌 upload파일이라면 서버에서도 파일 delete
            */
            removeImageFromQuillBoard(fileData.base64);
            if (fileData.url) {
                deleteUploadedFileAjax(fileData.url);
            }

            //dropzone에 image가 사라졌으므로 image가 없음을 알려주기 위해 css 제거
            if (this.files.length === 0) {
                this.element.classList.remove('dz-started');
            }
            uploadedFiles.delete(fileKey);
            preventRequestIfRemovedImageIsThumbnail(fileData.url);
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
            //base64 -> file 객체화한 file과 dropzone자체에서 만든 file이 서로 다른 객체라서 name만 같게 하여 dropzone 객체의 file을 찾아오게 함.
            const fileToRemove = dropzone.files.find(file => file.name === fileData.file.name);
            if (fileToRemove) {
                dropzone.removeFile(fileToRemove);
            } 
            uploadedFiles.delete(fileKey);
            preventRequestIfRemovedImageIsThumbnail(fileData?.url);
        }
    }
});

function deleteUploadedFileAjax(url) {
    fetch('/board/deleteFile', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'fileUrl=' + encodeURIComponent(url)
    })
    .then(response => response.json())
    .catch(err => console.error('파일 삭제 중 오류:', err));
}

function preventRequestIfRemovedImageIsThumbnail(url) {
    const currentThumbnailUrl = document.getElementById('thumbnailUrl').value;
    if (url === currentThumbnailUrl) {
        document.getElementById('thumbnailUrl').value = "";
    }
}

function removeImageFromQuillBoard(base64Image) {
    let imgToRemove = quill.root.querySelector(`img[src="${base64Image}"]`);
    if (imgToRemove) {
        imgToRemove.remove();
    }
}

function handleDropzoneImageRemoveBtn(removeButton, dropzoneInstance, mockFile, base64Image) {
    removeButton.addEventListener("click", function (e) {
        e.preventDefault();
        e.stopPropagation();
        dropzoneInstance.removeFile(mockFile);
        removeImageFromQuillBoard(base64Image);
    });
}

function handleClickDropzoneImage(mockFile, thumbnailLabel, base64Image) {
    mockFile.previewElement.addEventListener('click', function (e) {
        //삭제버튼을 누른 경우 차후 진행 방지
        if (e.target.classList.contains('dz-remove')) {
            return;
        }
        updateThumbnailSelection(mockFile, thumbnailLabel, base64Image, base64Image);
    });
}

function setThumbnailOnLoad(mockFile, thumbnailLabel, base64Image) {
    const dropZoneImgElements = mockFile.previewElement.querySelectorAll('img');
    const dropzoneImgSrcs = Array.from(dropZoneImgElements).map(img => img.getAttribute('src') || '');
    for (let i = 0; i < dropzoneImgSrcs.length; i++) {
        if (dropzoneImgSrcs[i] === document.getElementById('thumbnail').value) {
            mockFile.previewElement.classList.add('thumbnail-selected');
            thumbnailLabel.style.display = 'block';
            document.getElementById('thumbnail').value = base64Image;
            document.getElementById('thumbnailUrl').value = base64Image;
        }
    }
}

function generateThumbnailLabel(file) {
    let thumbnailLabel = document.createElement('div');
    thumbnailLabel.classList.add('thumbnail-label');
    thumbnailLabel.textContent = '썸네일로 지정';
    thumbnailLabel.style.display = 'none';
    file.previewElement.insertBefore(thumbnailLabel, file.previewElement.firstChild);
    return thumbnailLabel;
}

function convertBase64ToFile(base64Image, mockFile) {
    const base64String = base64Image.replace(/^data:image\/(png|jpeg);base64,/, '');
    const binaryData = base64ToBinary(base64String);
    const blob = new Blob([binaryData], { type: 'image/*' });
    const file = new File([blob], mockFile.name, { type: 'image/*' });
    return file;
}

/**
 * Inserts an image into Quill.
 *
 * @param {File} file - The file object from the dropzone (the image to insert).
 * @param {string} base64Data - The base64-encoded image data.
 * @param {string} [fileUrl] - The optional URL of the file (if already uploaded).
 */
function insertImageToQuill(file, base64Data, fileUrl = "") {
    let range = quill.getSelection();
    let insertIndex = range ? range.index : quill.getLength();
    quill.insertEmbed(insertIndex, 'image', base64Data);
    quill.setSelection(insertIndex + 1);

    let fileKey = file.name;
    uploadedFiles.set(fileKey, { file: file, base64: base64Data, url: fileUrl });

    file.previewElement.classList.add('dz-complete');

    const thumbnailLabel = generateThumbnailLabel(file);

    file.previewElement.addEventListener('click', function (e) {
        if (e.target.classList.contains('dz-remove')) {
            return;
        }
        updateThumbnailSelection(file, thumbnailLabel, base64Data, fileUrl);
    });
}

/**
 * 
 * @param {File} file  dropzonefile
 * @param {HTMLElement} thumbnailLabel 
 * @param {base64String} base64Data 
 * @param {string} fileUrl base64String | filePath_string
 */
function updateThumbnailSelection(file, thumbnailLabel, base64Data, fileUrl) {
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
    document.getElementById('thumbnail').value = base64Data;
    document.getElementById('thumbnailUrl').value = fileUrl;
}

function base64ToBinary(base64) {
    const binaryString = atob(base64);
    const len = binaryString.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes;
}