// Quill 에디터 초기화 (boardContent HTML이 표시됨)
var quill = new Quill('#editor-container', {
    theme: 'snow',
    modules: { imageResize: {} }
});
document.getElementById('editor-container').addEventListener('click', function () {
    quill.focus();
});

// 폼 제출 시, 에디터의 최신 HTML을 hidden 필드에 저장하고 검증
document.getElementById('board-form').onsubmit = function (e) {
	if (dropzone.files.length === 0) {
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
    var imgs = quill.root.querySelectorAll('img');
    imgs.forEach(function (img) {
        var rect = img.getBoundingClientRect();
        img.setAttribute('style', 'width:' + rect.width + 'px; height:' + rect.height + 'px;');
    });
    document.getElementById('boardContent').value = quill.root.innerHTML;
	if (dropzone.files.length > 0 && !document.getElementById('thumbnailUrl').value) {
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

        const editor = document.querySelector('.ql-editor');
        const imgElements = editor.querySelectorAll('img');
        const imgSrcs = Array.from(imgElements).map(img => img.getAttribute('src') || '');
        imgSrcs.forEach((base64Image, index) => {
            let mockFile = {
                name: "uploaded-image-" + index, // Generate a mock name
                size: 0, // Size unknown
                type: 'image/*'
            };
            
            dropzoneInstance.emit("addedfile", mockFile);
            dropzoneInstance.emit("thumbnail", mockFile, base64Image); // Use Base64 image
            dropzoneInstance.files.push(mockFile);
            mockFile.previewElement.classList.add('dz-complete'); // Ensure UI updates

            let thumbnailLabel = document.createElement('div');
            thumbnailLabel.classList.add('thumbnail-label');
            thumbnailLabel.textContent = '썸네일로 지정';
            thumbnailLabel.style.display = 'none';
            mockFile.previewElement.insertBefore(thumbnailLabel, mockFile.previewElement.firstChild );
            const dropZoneImgElements = mockFile.previewElement.querySelectorAll('img');
            const dropzoneImgSrcs = Array.from(dropZoneImgElements).map(img => img.getAttribute('src') || '');
            for (let i = 0; i < dropzoneImgSrcs.length; i++) {
                if (dropzoneImgSrcs[i] === document.getElementById('thumbnail').value ) {
                    //dropZoneImgElements[i].classList.add('thumbnail-selected'); --> previewElement에 css 조정하지 않으면 다른 image에 css가 들어가서 UI 상 문제
                    mockFile.previewElement.classList.add('thumbnail-selected');
                    thumbnailLabel.style.display = 'block';
                    document.getElementById('thumbnail').value = base64Image;
                    document.getElementById('thumbnailUrl').value = base64Image;
                }
            }

            mockFile.previewElement.addEventListener('click', function (e) {
                if (e.target.classList.contains('dz-remove')) {
                    return;
                }
                document.querySelectorAll('.dz-preview').forEach(function (preview) {
                    preview.classList.remove('thumbnail-selected');
                    let label = preview.querySelector('.thumbnail-label');
                    if (label) { 
                        label.style.display = 'none'; 
                    }
                });
                mockFile.previewElement.classList.add('thumbnail-selected');
                thumbnailLabel.style.display = 'block';
                // 새로 지정한 썸네일이면 hidden input 갱신
                document.getElementById('thumbnail').value = base64Image;
                document.getElementById('thumbnailUrl').value = base64Image;
                console.log('썸네일 지정:', fileUrl);
            });


            // Add remove button manually
            let removeButton = mockFile.previewElement.querySelector(".dz-remove");
            if (removeButton) {
                removeButton.addEventListener("click", function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    dropzoneInstance.removeFile(mockFile);
                    let imgToRemove = quill.root.querySelector(`img[src="${base64Image}"]`);
                    if (imgToRemove) {
                        imgToRemove.remove();
                    }
                });
            }

            // Base64 문자열을 디코딩하여 바이너리 데이터로 변환하는 함수
            const base64String = base64Image.replace(/^data:image\/(png|jpeg);base64,/, '');
            function base64ToBinary(base64) {
                const binaryString = atob(base64);
                const len = binaryString.length;
                const bytes = new Uint8Array(len);
                for (let i = 0; i < len; i++) {
                    bytes[i] = binaryString.charCodeAt(i);
                }
                return bytes;
            }

            const binaryData = base64ToBinary(base64String);
            const blob = new Blob([binaryData], { type: 'image/*' });
            const file = new File([blob], mockFile.name , { type: 'image/*' });

            //나중에 fileUrl도 전부 추가해야함.
            const fileKey = mockFile.name;
            uploadedFiles.set(fileKey, { file: file, base64: base64Image, url: base64Image  });
        });
    
        this.on('addedfile', function (file) {
            let fileKey = file.name;
			if (uploadedFiles.has(fileKey)) {
			    console.warn('중복된 파일입니다:', file.name);
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

            this.element.classList.add('dz-started');
        });

        this.on('success', function (file, response) {
            console.log('업로드 완료:', response);
            if (response && response.fileUrl) {
                var fileUrl = response.fileUrl;
                const reader = new FileReader();
                reader.onload = function (e) {
                    var base64Data = e.target.result;
                    insertImageToQuill(file, base64Data, fileUrl);
                };
                reader.readAsDataURL(file);
            } else {
                const reader = new FileReader();
                reader.onload = function (e) {
                    var base64Data = e.target.result;
                    insertImageToQuill(file, base64Data, "");
                };
                reader.readAsDataURL(file);
            }
        });

        this.on('error', function (file, errorMessage) {
            console.error('업로드 실패:', errorMessage);
        });

        this.on('removedfile', function (file) {
            console.log('Dropzone에서 파일 삭제됨:', file.name);
            let fileKey = file.name;
            let fileData = uploadedFiles.get(fileKey);
            if (fileData) {
                let imgToRemove = quill.root.querySelector(`img[src="${fileData.base64}"]`);
                if (imgToRemove) {
                    imgToRemove.remove();
                }
                if (fileData.url) {
                    fetch('/board/deleteFile', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        body: 'fileUrl=' + encodeURIComponent(fileData.url)
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (data.error) {
                                console.error('파일 삭제 실패:', data.error);
                            } else {
                                console.log('파일 삭제 성공');
                            }
                        })
                        .catch(err => console.error('파일 삭제 중 오류:', err));
                }
            }
            uploadedFiles.delete(fileKey);
            if (this.files.length === 0) {
                this.element.classList.remove('dz-started');
            }
            const currentThumbnailUrl = document.getElementById('thumbnailUrl').value;
            if (fileData && fileData.url === currentThumbnailUrl) {
                document.getElementById('thumbnailUrl').value = "";
            }
        });

        this.on('complete', function (file) {
            file.previewElement.classList.add('dz-complete');
        });
    }
});

function insertImageToQuill(file, base64Data, fileUrl) {
    let range = quill.getSelection();
    let insertIndex = range ? range.index : quill.getLength();
    quill.insertEmbed(insertIndex, 'image', base64Data);
    quill.setSelection(insertIndex + 1);

    let fileKey = file.name;
    uploadedFiles.set(fileKey, { file: file, base64: base64Data, url: fileUrl });

    file.previewElement.classList.add('dz-complete');

    let thumbnailLabel = document.createElement('div');
    thumbnailLabel.classList.add('thumbnail-label');
    thumbnailLabel.textContent = '썸네일로 지정';
    thumbnailLabel.style.display = 'none';
    file.previewElement.insertBefore(thumbnailLabel, file.previewElement.firstChild);

    file.previewElement.addEventListener('click', function (e) {
        if (e.target.classList.contains('dz-remove')) {
            return;
        }
        document.querySelectorAll('.dz-preview').forEach(function (preview) {
            preview.classList.remove('thumbnail-selected');
            let label = preview.querySelector('.thumbnail-label');
            if (label) { 
                label.style.display = 'none'; 
            }
        });
        file.previewElement.classList.add('thumbnail-selected');
        thumbnailLabel.style.display = 'block';
        // 새로 지정한 썸네일이면 hidden input 갱신
        document.getElementById('thumbnail').value = base64Data;
        document.getElementById('thumbnailUrl').value = fileUrl;
        console.log('썸네일 지정:', fileUrl);
    });
}

quill.on('text-change', function () {
    let quillImages = new Set([...quill.root.querySelectorAll('img')].map(img => img.src));
    for (let [fileKey, fileData] of uploadedFiles.entries()) {
        if (!quillImages.has(fileData.base64)) {

            //base64 -> file 객체화한 file과 dropzone자체에서 만든 file이 서로 다른 객체라서 name을 동일하게 만들고 dropzone 객체의 file을 찾아오게 함.
            const fileToRemove = dropzone.files.find(f => f.name === fileData.file.name);
            if (fileToRemove) {
                dropzone.removeFile(fileToRemove);
            } else {
                console.warn('Dropzone에서 파일을 찾을 수 없음:', fileData.file.name);
            }
            uploadedFiles.delete(fileKey);
            const currentThumbnailUrl = document.getElementById('thumbnailUrl').value;
            if (fileData.url === currentThumbnailUrl) {
                document.getElementById('thumbnailUrl').value = "";
            }
        }
    }
});