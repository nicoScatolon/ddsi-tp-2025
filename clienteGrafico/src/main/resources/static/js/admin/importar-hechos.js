// ===== ELEMENTOS DEL DOM =====
const uploadArea = document.getElementById('uploadArea');
const fileInput = document.getElementById('fileInput');
const btnSelect = document.getElementById('btnSelect');
const fileInfo = document.getElementById('fileInfo');
const fileName = document.getElementById('fileName');
const fileSize = document.getElementById('fileSize');
const btnRemove = document.getElementById('btnRemove');
const btnSubmit = document.getElementById('btnSubmit');
const alertMessage = document.getElementById('alertMessage');

// ===== EVENT LISTENERS =====

// Click en botón seleccionar
btnSelect.addEventListener('click', () => fileInput.click());

// Click en área de upload
uploadArea.addEventListener('click', (e) => {
    if (e.target !== btnSelect) {
        fileInput.click();
    }
});

// Drag and drop - dragover
uploadArea.addEventListener('dragover', (e) => {
    e.preventDefault();
    uploadArea.classList.add('drag-over');
});

// Drag and drop - dragleave
uploadArea.addEventListener('dragleave', () => {
    uploadArea.classList.remove('drag-over');
});

// Drag and drop - drop
uploadArea.addEventListener('drop', (e) => {
    e.preventDefault();
    uploadArea.classList.remove('drag-over');

    const files = e.dataTransfer.files;
    if (files.length > 0) {
        handleFile(files[0]);
    }
});

// Cambio en input file
fileInput.addEventListener('change', (e) => {
    if (e.target.files.length > 0) {
        handleFile(e.target.files[0]);
    }
});

// Remover archivo
btnRemove.addEventListener('click', () => {
    fileInput.value = '';
    uploadArea.style.display = 'flex';
    fileInfo.style.display = 'none';
    btnSubmit.disabled = true;
    hideAlert();
});

// ===== FUNCIONES =====

/**
 * Maneja el archivo seleccionado o arrastrado
 */
function handleFile(file) {
    if (!file.name.endsWith('.csv')) {
        showAlert('Por favor selecciona un archivo CSV válido', 'error');
        return;
    }

    fileName.textContent = file.name;
    fileSize.textContent = formatFileSize(file.size);
    uploadArea.style.display = 'none';
    fileInfo.style.display = 'block';
    btnSubmit.disabled = false;
    hideAlert();
}

/**
 * Formatea el tamaño del archivo a formato legible
 */
function formatFileSize(bytes) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
}

/**
 * Muestra un mensaje de alerta
 */
function showAlert(message, type) {
    alertMessage.textContent = message;
    alertMessage.className = 'alert-message alert-' + type;
    alertMessage.style.display = 'block';
}

/**
 * Oculta el mensaje de alerta
 */
function hideAlert() {
    alertMessage.style.display = 'none';
}