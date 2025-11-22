document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('hechoForm');
    const formContainer = document.querySelector('.form-container');
    const esNuevo = formContainer?.dataset.esNuevo === 'true';
    const tipoUbicacionSelect = document.getElementById('tipoUbicacion');

    // Campos de ubicación
    const provinciaInput = document.getElementById('provincia');
    const localidadInput = document.getElementById('localidad');
    const calleInput = document.getElementById('calle');
    const numeroInput = document.getElementById('numero');
    const latitudInput = document.getElementById('latitudUbicacion');
    const longitudInput = document.getElementById('longitudUbicacion');

    // Elementos del mapa
    const mapaGroup = document.getElementById('mapaGroup');
    const coordenadasDisplay = document.getElementById('coordenadasDisplay');
    const latDisplay = document.getElementById('latDisplay');
    const lngDisplay = document.getElementById('lngDisplay');

    // Elementos multimedia
    const multimediaInput = document.getElementById('multimediaInput');
    const addFileBtn = document.getElementById('addFileBtn');
    const multimediaContainer = document.getElementById('multimediaContainer');
    const multimediaSection = document.getElementById('multimediaSection');

    const submitBtn = form.querySelector('button[type="submit"]');

    // Variable para el mapa de Leaflet
    let map = null;
    let marker = null;

    // Array para almacenar archivos multimedia
    let multimediaFiles = [];

    // IDs de multimedia a eliminar
    let multimediaToDelete = [];

    // Variable para drag & drop
    let draggedElement = null;

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

        // Manejar cambios en descripciones de multimedia existente
        document.querySelectorAll('.desc-input-existing').forEach(input => {
            const mediaId = input.dataset.mediaId;

            input.addEventListener('input', function(e) {
                console.log(`Descripción actualizada para ID ${mediaId}:`, e.target.value);
            });
        });

        // Inicializar drag & drop para multimedia existente
        initDragAndDrop();
        updatePortadaBadge();
    }

    // ===== DRAG & DROP FUNCTIONALITY =====
    function initDragAndDrop() {
        const allItems = document.querySelectorAll('.multimedia-item');

        allItems.forEach(item => {
            setupDragAndDrop(item);
        });
    }

    function setupDragAndDrop(item) {
        item.addEventListener('dragstart', handleDragStart);
        item.addEventListener('dragend', handleDragEnd);
        item.addEventListener('dragover', handleDragOver);
        item.addEventListener('drop', handleDrop);
        item.addEventListener('dragleave', handleDragLeave);
    }

    function handleDragStart(e) {
        draggedElement = this;
        this.classList.add('dragging');
        e.dataTransfer.effectAllowed = 'move';
        e.dataTransfer.setData('text/html', this.innerHTML);
    }

    function handleDragEnd(e) {
        this.classList.remove('dragging');

        // Remover clase drag-over de todos los items
        document.querySelectorAll('.multimedia-item').forEach(item => {
            item.classList.remove('drag-over');
        });

        draggedElement = null;
        updatePortadaBadge();
    }

    function handleDragOver(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        e.dataTransfer.dropEffect = 'move';

        if (this !== draggedElement) {
            this.classList.add('drag-over');
        }

        return false;
    }

    function handleDragLeave(e) {
        this.classList.remove('drag-over');
    }

    function handleDrop(e) {
        if (e.stopPropagation) {
            e.stopPropagation();
        }

        this.classList.remove('drag-over');

        if (draggedElement !== this) {
            // Determinar si insertar antes o después basado en la posición del mouse
            const rect = this.getBoundingClientRect();
            const midpoint = rect.left + rect.width / 2;

            if (e.clientX < midpoint) {
                // Insertar antes
                this.parentNode.insertBefore(draggedElement, this);
            } else {
                // Insertar después
                this.parentNode.insertBefore(draggedElement, this.nextSibling);
            }

            updatePortadaBadge();
        }

        return false;
    }

    function updatePortadaBadge() {
        // Remover todos los badges existentes
        document.querySelectorAll('.portada-badge').forEach(badge => {
            badge.style.display = 'none';
        });

        // Obtener todos los items de multimedia del contenedor unificado
        const allMediaItems = Array.from(document.querySelectorAll('#multimediaContainer .multimedia-item')).filter(item => item.style.display !== 'none');

        // Encontrar la primera imagen
        const firstImage = allMediaItems.find(item => {
            const tipo = item.dataset.mediaTipo || item.dataset.tipo;
            return tipo === 'IMAGEN';
        });

        if (firstImage) {
            let badge = firstImage.querySelector('.portada-badge');
            if (!badge) {
                // Crear badge si no existe
                badge = document.createElement('div');
                badge.className = 'portada-badge';
                badge.innerHTML = '<i class="fas fa-star"></i><span>PORTADA</span>';
                firstImage.insertBefore(badge, firstImage.firstChild);
            }
            badge.style.display = 'flex';
        }
    }

    function showDireccionFields() {
        provinciaInput.required = true;
        localidadInput.required = true;
        calleInput.required = true;
        numeroInput.required = true;

        document.querySelector('.direccion-fields').style.display = 'grid';
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
            itemDiv.style.display = 'none';
            multimediaToDelete.push(mediaId);
            console.log('Marcado para eliminar:', mediaId);
            updatePortadaBadge();
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
        document.querySelector('.direccion-fields').style.display = 'none';
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
        updatePortadaBadge();
    });

    // Renderizar preview del archivo multimedia
    function renderMultimediaItem(fileId, file, tipoContenido) {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'multimedia-item';
        itemDiv.dataset.fileId = fileId;
        itemDiv.dataset.tipo = tipoContenido;
        itemDiv.draggable = true;

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
            <div class="portada-badge" style="display: none;">
                <i class="fas fa-star"></i>
                <span>PORTADA</span>
            </div>
            <div class="drag-handle">
                <i class="fas fa-grip-vertical"></i>
            </div>
            <div class="media-preview">
                ${previewContent}
            </div>
            <button type="button" class="remove-btn" data-file-id="${fileId}">&times;</button>
            <div class="file-info">
                <strong>${tipoContenido}</strong>
                <span>${file.name} (${formatFileSize(file.size)})</span>
            </div>
            <div class="file-desc">
                <input type="text" 
                       placeholder="Descripción (opcional)" 
                       data-file-id="${fileId}"
                       class="desc-input"
                       maxlength="200">
            </div>
        `;

        multimediaContainer.appendChild(itemDiv);

        // Setup drag and drop para el nuevo item
        setupDragAndDrop(itemDiv);

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
            updatePortadaBadge();
        }
    }

    // Actualizar descripción del archivo
    function updateFileDescription(fileId, descripcion) {
        const fileItem = multimediaFiles.find(item => item.id === fileId);
        if (fileItem) {
            fileItem.descripcion = descripcion;
        }
    }

    // Preparar datos de multimedia existente antes de enviar
    function prepararMultimediaExistente() {
        const existingItems = document.querySelectorAll('.existing-item');
        const ids = [];
        const urls = [];
        const tipos = [];
        const descs = [];

        existingItems.forEach(item => {
            // Solo incluir items que NO están marcados para eliminar (display !== 'none')
            if (item.style.display !== 'none') {
                const mediaId = item.dataset.mediaId;
                ids.push(mediaId);
                urls.push(item.dataset.mediaUrl);
                tipos.push(item.dataset.mediaTipo);

                // Obtener la descripción del input (puede haber sido modificada)
                const descInput = item.querySelector('.desc-input-existing');
                const descripcion = descInput ? descInput.value : '';
                descs.push(descripcion || '');

                console.log(`Media ID ${mediaId}: descripción = "${descripcion}"`);
            }
        });

        // Usar separador único que no aparezca en URLs
        document.getElementById('multimediaExistenteIds').value = ids.join('|||');
        document.getElementById('multimediaExistenteUrls').value = urls.join('|||');
        document.getElementById('multimediaExistenteTipos').value = tipos.join('|||');
        document.getElementById('multimediaExistenteDescs').value = descs.join('|||');
        document.getElementById('multimediaEliminarIds').value = multimediaToDelete.join(',');

        console.log('=== MULTIMEDIA EXISTENTE PREPARADA ===');
        console.log('IDs:', ids);
        console.log('URLs:', urls);
        console.log('Tipos:', tipos);
        console.log('Descripciones:', descs);
        console.log('A eliminar:', multimediaToDelete);
    }

    // Preparar datos de multimedia nueva (respetando orden del DOM)
    function prepararMultimediaNueva() {
        const newItems = Array.from(document.querySelectorAll('#multimediaContainer .multimedia-item[data-file-id]'));

        // Reordenar multimediaFiles según el orden en el DOM
        const orderedFiles = [];

        newItems.forEach(item => {
            const fileId = item.dataset.fileId;
            const fileItem = multimediaFiles.find(f => f.id === fileId);
            if (fileItem) {
                orderedFiles.push(fileItem);
            }
        });

        // Actualizar multimediaFiles con el orden correcto
        multimediaFiles = orderedFiles;

        console.log('=== MULTIMEDIA NUEVA ORDENADA ===');
        multimediaFiles.forEach((item, index) => {
            console.log(`${index + 1}. ${item.tipoContenido} - ${item.file.name}`);
        });
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

        // === MODO EDICIÓN: Preparar multimedia existente ===
        if (!esNuevo) {
            prepararMultimediaExistente();

            // Agregar manualmente los campos ocultos actualizados
            fd.set('multimediaExistenteIds', document.getElementById('multimediaExistenteIds').value);
            fd.set('multimediaExistenteUrls', document.getElementById('multimediaExistenteUrls').value);
            fd.set('multimediaExistenteTipos', document.getElementById('multimediaExistenteTipos').value);
            fd.set('multimediaExistenteDescs', document.getElementById('multimediaExistenteDescs').value);
            fd.set('multimediaEliminarIds', document.getElementById('multimediaEliminarIds').value);
        }

        // === AGREGAR NUEVOS ARCHIVOS MULTIMEDIA (en orden del DOM) ===
        prepararMultimediaNueva();

        multimediaFiles.forEach((item) => {
            fd.append('multimedia', item.file);
        });
        multimediaFiles.forEach((item) => {
            fd.append('tipoContenido', item.tipoContenido);
        });
        multimediaFiles.forEach((item) => {
            fd.append('descripcionMultimedia', item.descripcion && item.descripcion.trim() ? item.descripcion.trim() : '');
        });

        // DEBUG: Ver qué se está enviando
        console.log('=== CONTENIDO DEL FORMDATA ===');
        for (let pair of fd.entries()) {
            if (pair[1] instanceof File) {
                console.log(pair[0], ':', pair[1].name, `(${pair[1].size} bytes)`);
            } else {
                console.log(pair[0], ':', pair[1]);
            }
        }

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
                alert('No se pudo procesar el hecho. Estado: ' + response.status + '\n' + text);
            }
        } catch (err) {
            console.error('Fetch error:', err);
            const doFallback = confirm('No se pudo conectar con el servidor vía AJAX. ¿Intentar envío tradicional?');
            if (doFallback) {
                const tempForm = form.cloneNode(true);
                form.parentNode.replaceChild(tempForm, form);
                tempForm.submit();
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
                multimediaContainer.querySelectorAll('.multimedia-item[data-file-id]').forEach(item => item.remove());
                multimediaInput.value = '';
            } else {
                // En modo edición, volver a los valores originales
                window.location.reload();
            }
        }, 0);
    });
});