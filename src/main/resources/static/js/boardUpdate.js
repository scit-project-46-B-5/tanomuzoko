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

    // 업데이트된 HTML을 hidden input에 설정
    document.getElementById('boardContent').value = quill.root.innerHTML;

    return true;
};

let uploadedFiles = new Map();
Dropzone.autoDiscover = false;

const dropzone = new Dropzone('#dropzone', {
    url: '#', // Prevents "No URL provided" error
    autoProcessQueue: false, // Prevent automatic uploads
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
        imgSrcs.forEach((base64, index) => {
            // 저장한 image를 dropzone에 preload를 적용 
            let mockFile = {
                name: "uploaded-image-" + index, 
                size: calculateImageSize(base64), 
                type: 'image/*'
            };
            dropzoneInstance.emit("addedfile", mockFile);
            dropzoneInstance.emit("thumbnail", mockFile, base64);
            dropzoneInstance.files.push(mockFile);
            mockFile.previewElement.classList.add('dz-complete'); 

            // thumbnail인 이미지는 thumnail 표시 CSS 적용
            let thumbnailLabel = generateThumbnailLabel(mockFile);
            setThumbnailOnLoad(mockFile, thumbnailLabel, base64);

            // dropzone click 시 thumbnailImage 변경되게 적용
            handleClickDropzoneImage(mockFile, thumbnailLabel, base64);


            // dropzone image에서 제거 버튼 누르면 dropzone에서 삭제되게 적용
            let removeButton = mockFile.previewElement.querySelector(".dz-remove");
            handleDropzoneImageRemoveBtn(removeButton, dropzoneInstance, mockFile, base64);

            // Base64 문자열을 디코딩하여 바이너리 데이터로 변환하는 함수
            const file = convertBase64ToFile(base64, mockFile);

            //나중에 fileUrl도 전부 추가해야함.
            const fileKey = mockFile.name;
            uploadedFiles.set(fileKey, { file, base64  });
        });

        this.on("sending", function(file, xhr, formData) {
            // Prevent actual sending since we are not using a server
            xhr.abort();
        });
    
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

        //file dropzone에서 삭제했을 때 발생하는 event lisneter
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
            //base64 -> file 객체화한 file과 dropzone자체에서 만든 file이 서로 다른 객체라서 name만 같게 하여 dropzone 객체의 file을 찾아오게 함.
            const fileToRemove = dropzone.files.find(file => file.name === fileData.file.name);
            if (fileToRemove) {
                dropzone.removeFile(fileToRemove);
            } 
            uploadedFiles.delete(fileKey);
            preventRequestIfRemovedImageIsThumbnail(fileData?.base64);
        }
    }
});


function preventRequestIfRemovedImageIsThumbnail(base64) {
    const currentThumbnailBase64 = document.getElementById('thumbnailUrl').value;
    if (base64 === currentThumbnailBase64) {
        document.getElementById('thumbnailUrl').value = "";
    }
}

function removeImageFromQuillBoard(base64) {
    let imgToRemove = quill.root.querySelector(`img[src="${base64}"]`);
    if (imgToRemove) {
        imgToRemove.remove();
    }
}

function handleDropzoneImageRemoveBtn(removeButton, dropzoneInstance, mockFile, base64) {
    removeButton.addEventListener("click", function (e) {
        e.preventDefault();
        e.stopPropagation();
        dropzoneInstance.removeFile(mockFile);
        removeImageFromQuillBoard(base64);
    });
}

function handleClickDropzoneImage(mockFile, thumbnailLabel, base64) {
    mockFile.previewElement.addEventListener('click', function (e) {
        //삭제버튼을 누른 경우 차후 진행 방지
        if (e.target.classList.contains('dz-remove')) {
            return;
        }
        updateThumbnailSelection(mockFile, thumbnailLabel, base64);
    });
}

function setThumbnailOnLoad(mockFile, thumbnailLabel, base64) {
    const dropZoneImgElements = mockFile.previewElement.querySelectorAll('img');
    const dropzoneImgSrcs = Array.from(dropZoneImgElements).map(img => img.getAttribute('src') || '');
    for (let i = 0; i < dropzoneImgSrcs.length; i++) {
        if (dropzoneImgSrcs[i] === document.getElementById('thumbnail').value) {
            mockFile.previewElement.classList.add('thumbnail-selected');
            thumbnailLabel.style.display = 'block';
            document.getElementById('thumbnail').value = base64;
            document.getElementById('thumbnailUrl').value = base64;
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

function convertBase64ToFile(base64, mockFile) {
    const base64String = base64.replace(/^data:image\/(png|jpeg);base64,/, '');
    const binaryData = base64ToBinary(base64String);
    const blob = new Blob([binaryData], { type: 'image/*' });
    const file = new File([blob], mockFile.name, { type: 'image/*' });
    return file;
}

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

function base64ToBinary(base64) {
    const binaryString = atob(base64);
    const len = binaryString.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes;
}

function calculateImageSize(base64String) {
    // Remove data URL prefix if present
    let cleanedBase64 = base64String.split(',')[1] || base64String;

    // Calculate size in bytes
    let padding = (cleanedBase64.match(/=+$/) || [""])[0].length;
    let sizeInBytes = (cleanedBase64.length * 3) / 4 - padding;

    return sizeInBytes;
}