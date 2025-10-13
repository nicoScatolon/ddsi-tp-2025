document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('hechoForm');
    const formContainer = document.querySelector('.form-container');
    const esNuevo = formContainer?.dataset.esNuevo === 'true';
    const tipoUbicacionSelect = document.getElementById('tipoUbicacion');

    // Campos de ubicación
    const provinciaGroup = document.getElementById('provinciaGroup');
    const localidadGroup = document.getElementById('localidadGroup');
    const calleGroup = document.getElementById('calleGroup');
    const numeroGroup = document.getElementById('numeroGroup');
    const mapaGroup = document.getElementById('mapaGroup');

    const provinciaInput = document.getElementById('provincia');
    const localidadInput = document.getElementById('localidad');
    const calleInput = document.getElementById('calle');
    const numeroInput = document.getElementById('numero');
    const latitudInput = document.getElementById('latitudUbicacion');
    const longitudInput = document.getElementById('longitudUbicacion');

    // Elementos del mapa
    const coordenadasDisplay = document.getElementById('coordenadasDisplay');
    const latDisplay = document.getElementById('latDisplay');
    const lngDisplay = document.getElementById('lngDisplay');

    // Elementos multimedia
    const multimediaInput = document.getElementById('multimediaInput');
    const addFileBtn = document.getElementById('addFileBtn');
    const multimediaPreview = document.getElementById('multimediaPreview');
    const existingMultimedia = document.getElementById('existingMultimedia');

    const submitBtn = form.querySelector('button[type="submit"]');

    // Variable para el mapa de Leaflet
    let map = null;
    let marker = null;

    // Array para almacenar archivos multimedia
    let multimediaFiles = [];

    // IDs de multimedia a eliminar
    let multimediaToDelete = [];

    // Configuración de tipos de archivo permitidos
    const ALLOWED_TYPES = {
        imagen: ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'],
        video: ['video/mp4', 'video/avi', 'video/mov', 'video/quicktime', 'video/x-msvideo'],
        audio: ['audio/mpeg', 'audio/mp3', 'audio/wav', 'audio/ogg'],
        documento: ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document']
    };

    const MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    // ===== INICIALIZACIÓN PARA MODO EDICIÓN =====
    if (!esNuevo) {
        initEditMode();
    }

    function initEditMode() {
        // Detectar tipo de ubicación según los datos cargados
        const hasDireccion = provinciaInput.value || localidadInput.value || calleInput.value || numeroInput.value;
        const hasCoordenadas = latitudInput.value && longitudInput.value;

        if (hasDireccion) {
            tipoUbicacionSelect.value = 'direccion';
            showDireccionFields();
        } else if (hasCoordenadas) {
            tipoUbicacionSelect.value = 'mapa';
            showMapaFields();
            initMapWithMarker(parseFloat(latitudInput.value), parseFloat(longitudInput.value));
        }

        // Manejar botones de eliminar multimedia existente
        document.querySelectorAll('.remove-existing-btn').forEach(btn => {
            btn.addEventListener('click', function() {
                const mediaId = this.dataset.mediaId;
                removeExistingMedia(mediaId);
            });
        });
    }

    function showDireccionFields() {
        provinciaInput.required = true;
        localidadInput.required = true;
        calleInput.required = true;
        numeroInput.required = true;

        provinciaGroup.style.display = 'flex';
        localidadGroup.style.display = 'flex';
        calleGroup.style.display = 'flex';
        numeroGroup.style.display = 'flex';
    }

    function showMapaFields() {
        mapaGroup.style.display = 'flex';
        latitudInput.required = true;
        longitudInput.required = true;
        initMap();
    }

    // Inicializar el mapa de Leaflet
    function initMap() {
        if (!map) {
            map = L.map('mapContainer').setView([-34.6037, -58.3816], 13);

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
                maxZoom: 19
            }).addTo(map);

            map.on('click', function(e) {
                const lat = e.latlng.lat.toFixed(7);
                const lng = e.latlng.lng.toFixed(7);

                latitudInput.value = lat;
                longitudInput.value = lng;

                latDisplay.textContent = lat;
                lngDisplay.textContent = lng;
                coordenadasDisplay.style.display = 'block';

                if (marker) {
                    marker.setLatLng(e.latlng);
                } else {
                    marker = L.marker(e.latlng, { draggable: true }).addTo(map);

                    marker.on('dragend', function() {
                        const position = marker.getLatLng();
                        const newLat = position.lat.toFixed(7);
                        const newLng = position.lng.toFixed(7);

                        latitudInput.value = newLat;
                        longitudInput.value = newLng;
                        latDisplay.textContent = newLat;
                        lngDisplay.textContent = newLng;
                    });
                }
            });
        }

        setTimeout(() => map.invalidateSize(), 100);
    }

    // Inicializar mapa con marcador existente (modo edición)
    function initMapWithMarker(lat, lng) {
        initMap();

        setTimeout(() => {
            map.setView([lat, lng], 15);
            marker = L.marker([lat, lng], { draggable: true }).addTo(map);

            latDisplay.textContent = lat.toFixed(7);
            lngDisplay.textContent = lng.toFixed(7);
            coordenadasDisplay.style.display = 'block';

            marker.on('dragend', function() {
                const position = marker.getLatLng();
                const newLat = position.lat.toFixed(7);
                const newLng = position.lng.toFixed(7);

                latitudInput.value = newLat;
                longitudInput.value = newLng;
                latDisplay.textContent = newLat;
                lngDisplay.textContent = newLng;
            });
        }, 200);
    }

    // Eliminar multimedia existente
    function removeExistingMedia(mediaId) {
        const itemDiv = document.querySelector(`.existing-item[data-media-id="${mediaId}"]`);
        if (itemDiv) {
            // Marcar como eliminado
            const eliminarFlag = itemDiv.querySelector('.eliminar-flag');
            if (eliminarFlag) {
                eliminarFlag.value = 'true';
            }
            itemDiv.style.display = 'none';
            multimediaToDelete.push(mediaId);
        }
    }

    // Manejar campos de ubicación según el tipo seleccionado
    tipoUbicacionSelect.addEventListener('change', function() {
        const tipo = this.value;
        resetUbicacionFields();

        if (tipo === 'direccion') {
            showDireccionFields();
        } else if (tipo === 'mapa') {
            showMapaFields();
        }
    });

    // Función para resetear campos de ubicación
    function resetUbicacionFields() {
        provinciaGroup.style.display = 'none';
        localidadGroup.style.display = 'none';
        calleGroup.style.display = 'none';
        numeroGroup.style.display = 'none';
        mapaGroup.style.display = 'none';
        coordenadasDisplay.style.display = 'none';

        provinciaInput.required = false;
        localidadInput.required = false;
        calleInput.required = false;
        numeroInput.required = false;
        latitudInput.required = false;
        longitudInput.required = false;

        provinciaInput.value = '';
        localidadInput.value = '';
        calleInput.value = '';
        numeroInput.value = '';
        latitudInput.value = '';
        longitudInput.value = '';

        if (marker && map) {
            try { map.removeLayer(marker); } catch (err) {}
            marker = null;
        }
    }

    // Determinar el tipo de contenido según el MIME type
    function getTipoContenido(mimeType) {
        if (ALLOWED_TYPES.imagen.includes(mimeType)) return 'IMAGEN';
        if (ALLOWED_TYPES.video.includes(mimeType)) return 'VIDEO';
        if (ALLOWED_TYPES.audio.includes(mimeType)) return 'AUDIO';
        if (ALLOWED_TYPES.documento.includes(mimeType)) return 'DOCUMENTO';
        return null;
    }

    // Validar archivo
    function validateFile(file) {
        const tipoContenido = getTipoContenido(file.type);

        if (!tipoContenido) {
            alert(`Tipo de archivo no permitido: ${file.name}`);
            return false;
        }

        if (file.size > MAX_FILE_SIZE) {
            alert(`El archivo ${file.name} excede el tamaño máximo de 10MB`);
            return false;
        }

        return true;
    }

    // Agregar archivos multimedia
    addFileBtn.addEventListener('click', function() {
        const files = multimediaInput.files;

        if (files.length === 0) {
            alert('Por favor, seleccione al menos un archivo');
            return;
        }

        Array.from(files).forEach(file => {
            if (validateFile(file)) {
                const fileId = Date.now() + '_' + Math.random().toString(36).substr(2, 9);
                const tipoContenido = getTipoContenido(file.type);

                multimediaFiles.push({
                    id: fileId,
                    file: file,
                    tipoContenido: tipoContenido,
                    descripcion: ''
                });

                renderMultimediaItem(fileId, file, tipoContenido);
            }
        });

        multimediaInput.value = '';
    });

    // Renderizar preview del archivo multimedia
    function renderMultimediaItem(fileId, file, tipoContenido) {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'multimedia-item';
        itemDiv.dataset.fileId = fileId;

        let previewContent = '';

        if (tipoContenido === 'IMAGEN') {
            const objectUrl = URL.createObjectURL(file);
            previewContent = `<img src="${objectUrl}" alt="${file.name}">`;
        } else if (tipoContenido === 'VIDEO') {
            const objectUrl = URL.createObjectURL(file);
            previewContent = `<video controls><source src="${objectUrl}" type="${file.type}"></video>`;
        } else if (tipoContenido === 'AUDIO') {
            previewContent = `<div class="file-icon">🎵</div>`;
        } else if (tipoContenido === 'DOCUMENTO') {
            previewContent = `<div class="file-icon">📄</div>`;
        }

        itemDiv.innerHTML = `
            ${previewContent}
            <button type="button" class="remove-btn" data-file-id="${fileId}">&times;</button>
            <div class="file-info">
                <strong>${tipoContenido}</strong><br>
                ${file.name} (${formatFileSize(file.size)})
            </div>
            <div class="file-desc">
                <input type="text" 
                       placeholder="Descripción (opcional)" 
                       data-file-id="${fileId}"
                       class="desc-input"
                       maxlength="200">
            </div>
        `;

        multimediaPreview.appendChild(itemDiv);

        // Event listener para el botón de eliminar
        itemDiv.querySelector('.remove-btn').addEventListener('click', function() {
            removeMultimediaItem(fileId);
        });

        // Event listener para la descripción
        itemDiv.querySelector('.desc-input').addEventListener('input', function(e) {
            updateFileDescription(fileId, e.target.value);
        });
    }

    // Formatear tamaño de archivo
    function formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
    }

    // Eliminar archivo multimedia
    function removeMultimediaItem(fileId) {
        multimediaFiles = multimediaFiles.filter(item => item.id !== fileId);
        const itemDiv = document.querySelector(`.multimedia-item[data-file-id="${fileId}"]`);
        if (itemDiv) {
            // Liberar URL del objeto si existe
            const img = itemDiv.querySelector('img');
            const video = itemDiv.querySelector('video source');
            if (img) URL.revokeObjectURL(img.src);
            if (video) URL.revokeObjectURL(video.src);

            itemDiv.remove();
        }
    }

    // Actualizar descripción del archivo
    function updateFileDescription(fileId, descripcion) {
        const fileItem = multimediaFiles.find(item => item.id === fileId);
        if (fileItem) {
            fileItem.descripcion = descripcion;
        }
    }

    // Manejar envío del formulario
    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        // Validación: si usuario eligió Mapa, asegurarse que haya coordenadas
        if (tipoUbicacionSelect.value === 'mapa' && (!latitudInput.value || !longitudInput.value)) {
            alert('Por favor, seleccione una ubicación en el mapa haciendo clic sobre él.');
            return;
        }

        const originalBtnText = submitBtn.textContent;
        submitBtn.disabled = true;
        submitBtn.textContent = esNuevo ? 'Enviando...' : 'Actualizando...';

        const actionUrl = form.getAttribute('action') || (esNuevo ? '/hechos/create' : '/hechos/editar');
        const fd = new FormData(form);

        // Agregar archivos multimedia nuevos al FormData
        multimediaFiles.forEach((item, index) => {
            fd.append(`multimedia[${index}].file`, item.file);
            fd.append(`multimedia[${index}].tipoContenido`, item.tipoContenido);
            fd.append(`multimedia[${index}].descripcion`, item.descripcion || '');
        });

        try {
            const response = await fetch(actionUrl, {
                method: 'POST',
                body: fd,
                credentials: 'same-origin'
            });

            if (response.ok) {
                // Liberar URLs de objetos antes de redireccionar
                multimediaFiles.forEach(item => {
                    if (item.file.type.startsWith('image/') || item.file.type.startsWith('video/')) {
                        const itemDiv = document.querySelector(`.multimedia-item[data-file-id="${item.id}"]`);
                        if (itemDiv) {
                            const img = itemDiv.querySelector('img');
                            const video = itemDiv.querySelector('video source');
                            if (img) URL.revokeObjectURL(img.src);
                            if (video) URL.revokeObjectURL(video.src);
                        }
                    }
                });

                window.location.href = '/hechos';
            } else {
                let text = '';
                try { text = await response.text(); } catch (err) {}
                console.error('Error al procesar hecho:', response.status, text);
                alert('No se pudo procesar el hecho. Estado: ' + response.status);
            }
        } catch (err) {
            console.error('Fetch error:', err);
            const doFallback = confirm('No se pudo conectar con el servidor vía AJAX. ¿Intentar envío tradicional?');
            if (doFallback) {
                // retirar este listener y hacer submit clásico
                form.removeEventListener('submit', arguments.callee);
                form.submit();
            }
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = originalBtnText;
        }
    });

    // Manejar botón reset
    form.addEventListener('reset', function() {
        setTimeout(() => {
            if (esNuevo) {
                resetUbicacionFields();
                tipoUbicacionSelect.value = '';

                // Limpiar archivos multimedia
                multimediaFiles.forEach(item => {
                    const itemDiv = document.querySelector(`.multimedia-item[data-file-id="${item.id}"]`);
                    if (itemDiv) {
                        const img = itemDiv.querySelector('img');
                        const video = itemDiv.querySelector('video source');
                        if (img) URL.revokeObjectURL(img.src);
                        if (video) URL.revokeObjectURL(video.src);
                    }
                });

                multimediaFiles = [];
                multimediaPreview.innerHTML = '';
                multimediaInput.value = '';
            } else {
                // En modo edición, volver a los valores originales
                window.location.reload();
            }
        }, 0);
    });
});