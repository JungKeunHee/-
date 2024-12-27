function previewImage(event) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = function(e) {
        const img = document.getElementById('profilePreview');
        img.src = e.target.result;
        img.style.display = 'block'; // 이미지가 선택되면 보이도록 설정
    }

    if (file) {
        reader.readAsDataURL(file);
    }
}
