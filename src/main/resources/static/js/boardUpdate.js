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
    alert('이미지 파일을 첨부해주세요');
    e.preventDefault();
    return false;
  }
  var imgs = quill.root.querySelectorAll('img');
  imgs.forEach(function(img) {
     var rect = img.getBoundingClientRect();
     img.setAttribute('style', 'width:' + rect.width + 'px; height:' + rect.height + 'px;');
  });
  document.getElementById('boardContent').value = quill.root.innerHTML;
  if (dropzone.files.length > 0 && !document.getElementById('thumbnailUrl').value) {
    alert('썸네일로 사용할 사진을 선택해주세요');
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
    this.on('addedfile', function (file) {
      let fileKey = file.name + file.size;
      if (uploadedFiles.has(fileKey)) {
        console.warn('중복된 파일입니다:', file.name);
        alert('이미 업로드된 파일입니다.');
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
        reader.onload = function(e) {
          var base64Data = e.target.result;
          insertImageToQuill(file, base64Data, fileUrl);
        };
        reader.readAsDataURL(file);
      } else {
        const reader = new FileReader();
        reader.onload = function(e) {
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
      let fileKey = file.name + file.size;
      let fileData = uploadedFiles.get(fileKey);
      if (fileData) {
        let imgToRemove = quill.root.querySelector(`img[src="${fileData.base64}"]`);
        if (imgToRemove) {
          imgToRemove.remove();
        }
        if(fileData.url) {
          fetch('/board/deleteFile', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'fileUrl=' + encodeURIComponent(fileData.url)
          })
          .then(response => response.json())
          .then(data => {
            if(data.error) {
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

  let fileKey = file.name + file.size;
  uploadedFiles.set(fileKey, { file: file, base64: base64Data, url: fileUrl });

  file.previewElement.classList.add('dz-complete');

  let thumbnailLabel = document.createElement('div');
  thumbnailLabel.classList.add('thumbnail-label');
  thumbnailLabel.textContent = '썸네일로 지정';
  thumbnailLabel.style.display = 'none';
  file.previewElement.insertBefore(thumbnailLabel, file.previewElement.firstChild);

  file.previewElement.addEventListener('click', function(e) {
    if (e.target.classList.contains('dz-remove')) return;
    document.querySelectorAll('.dz-preview').forEach(function(preview) {
      preview.classList.remove('thumbnail-selected');
      let label = preview.querySelector('.thumbnail-label');
      if (label) { label.style.display = 'none'; }
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
      console.log('Quill에서 이미지 삭제됨:', fileData.base64);
      dropzone.removeFile(fileData.file);
      uploadedFiles.delete(fileKey);
      const currentThumbnailUrl = document.getElementById('thumbnailUrl').value;
      if (fileData.url === currentThumbnailUrl) {
        document.getElementById('thumbnailUrl').value = "";
      }
    }
  }
});