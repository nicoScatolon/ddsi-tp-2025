// hecho-detail.js - Gestión completa del detalle de hechos con Leaflet

document.addEventListener('DOMContentLoaded', () => {
    // Verificar que tenemos datos del hecho
    if (typeof HECHO_DATA === 'undefined' || !HECHO_DATA.id) {
        console.warn('No hay datos del hecho disponibles');
        return;
    }

    // Referencias a elementos del DOM
    const titleEl = document.getElementById('hecho-title');
    const descEl = document.getElementById('hecho-description');
    const categoriasEl = document.getElementById('hecho-categorias');
    const etiquetasEl = document.getElementById('hecho-etiquetas');
    const fechaEl = document.getElementById('hecho-fecha');
    const ubicacionEl = document.getElementById('hecho-ubicacion');
    const mapEl = document.getElementById('hecho-map');
    const multimediaEl = document.getElementById('hecho-multimedia');

    // Variables para el mapa
    let map = null;
    let marker = null;

    // Variables para el modo edición
    let editing = false;
    let addImageControls = null;
    let mapClickHandler = null;
    let mapHint = null;

    // ===== INICIALIZAR MAPA CON LEAFLET =====
    const initMap = () => {
        if (!mapEl) return;

        const lat = HECHO_DATA.ubicacion.latitud;
        const lng = HECHO_DATA.ubicacion.longitud;

        if (lat && lng && !isNaN(lat) && !isNaN(lng)) {
            try {
                map = L.map('hecho-map').setView([lat, lng], 14);

                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
                    maxZoom: 19
                }).addTo(map);

                marker = L.marker([lat, lng]).addTo(map);

                const popupContent = `
                    <strong>${HECHO_DATA.titulo}</strong><br>
                    ${HECHO_DATA.ubicacion.calle || ''} ${HECHO_DATA.ubicacion.numero || ''}<br>
                    ${HECHO_DATA.ubicacion.localidad || ''}, ${HECHO_DATA.ubicacion.provincia || ''}
                `;
                marker.bindPopup(popupContent).openPopup();
            } catch (error) {
                console.error('Error al inicializar el mapa:', error);
                mapEl.innerHTML = '<div class="placeholder">Error al cargar el mapa</div>';
            }
        } else {
            mapEl.innerHTML = '<div class="placeholder">No hay coordenadas disponibles para mostrar el mapa</div>';
        }
    };
    initMap();

    // ===== MODAL DE ELIMINACIÓN =====
    const setupDeleteModal = () => {
        const deleteBtn = document.getElementById('deleteBtn');
        const modal = document.getElementById('deleteModal');
        if (!deleteBtn || !modal) return;

        const closeBtn = modal.querySelector('.close');
        const form = document.getElementById('deleteForm');

        // Abrir modal solo al apretar botón
        deleteBtn.addEventListener('click', () => {
            modal.style.display = 'flex'; // usar flex para centrar
        });

        // Cerrar modal
        if (closeBtn) {
            closeBtn.addEventListener('click', () => {
                modal.style.display = 'none';
            });
        }

        window.addEventListener('click', (e) => {
            if (e.target === modal) modal.style.display = 'none';
        });

        // Enviar formulario
        if (form) {
            form.addEventListener('submit', (e) => {
                e.preventDefault();
                const reasonEl = document.getElementById('reason');
                const reason = reasonEl ? reasonEl.value.trim() : '';
                if (reason && reason.length >= 500) {
                    console.log('Solicitud de eliminación enviada:', {
                        hechoId: HECHO_DATA.id,
                        razon: reason
                    });
                    alert('Solicitud de eliminación enviada correctamente');
                    form.reset();
                    modal.style.display = 'none';
                } else {
                    alert('Por favor ingresa una razón de al menos 500 caracteres.');
                }
            });
        }
    };

    // ===== MODO EDICIÓN =====
    const setupEditMode = () => {
        const editBtn = document.getElementById('editBtn');
        if (!editBtn) return;

        editBtn.addEventListener('click', () => {
            if (!editing) enterEditMode();
            else exitEditMode();
        });
    };

    const enterEditMode = () => {
        editing = true;
        const editBtn = document.getElementById('editBtn');
        editBtn.textContent = 'Guardar cambios';
        editBtn.classList.replace('btn-primary', 'btn-success');

        [titleEl, descEl, fechaEl, ubicacionEl].forEach(el => el?.setAttribute('contenteditable', 'true'));
        [categoriasEl, etiquetasEl].forEach(list => {
            list?.querySelectorAll('li').forEach(li => li.setAttribute('contenteditable', 'true'));
        });

        // Controles de imagen
        addImageControls = document.createElement('div');
        addImageControls.className = 'image-controls';
        addImageControls.innerHTML = `
            <button class="btn-secondary" id="addImageUrlBtn" type="button">📷 Agregar imagen por URL</button>
            <input type="file" id="uploadImageInput" accept="image/*" style="display:none;" multiple />
            <button class="btn-secondary" id="uploadImageBtn" type="button">📁 Subir imagen desde PC</button>
        `;
        multimediaEl.appendChild(addImageControls);

        const removePlaceholder = () => multimediaEl.querySelector('.placeholder')?.remove();

        // Agregar imagen por URL
        addImageControls.querySelector('#addImageUrlBtn')
            .addEventListener('click', () => {
                const url = prompt('Ingrese la URL de la imagen:');
                if (url) {
                    removePlaceholder();
                    const img = document.createElement('img');
                    img.src = url.trim();
                    img.alt = HECHO_DATA.titulo;
                    img.className = 'multimedia-item';
                    multimediaEl.insertBefore(img, addImageControls);
                }
            });

        // Subir imagen desde PC
        const uploadInput = addImageControls.querySelector('#uploadImageInput');
        const uploadBtn = addImageControls.querySelector('#uploadImageBtn');

        uploadBtn.addEventListener('click', () => uploadInput.click());

        uploadInput.addEventListener('change', e => {
            Array.from(e.target.files || []).forEach(file => {
                const reader = new FileReader();
                reader.onload = ev => {
                    removePlaceholder();
                    const img = document.createElement('img');
                    img.src = ev.target.result;
                    img.alt = HECHO_DATA.titulo;
                    img.className = 'multimedia-item';
                    multimediaEl.insertBefore(img, addImageControls);
                };
                reader.readAsDataURL(file);
            });
            uploadInput.value = '';
        });

        // Editar mapa
        if (map) {
            mapHint = document.createElement('div');
            mapHint.className = 'map-hint';
            mapHint.textContent = '📍 Haz clic en el mapa para cambiar la ubicación';
            mapEl.parentNode.insertBefore(mapHint, mapEl);

            mapClickHandler = e => {
                const { lat, lng } = e.latlng;
                HECHO_DATA.ubicacion.latitud = lat;
                HECHO_DATA.ubicacion.longitud = lng;
                ubicacionEl.textContent = `Lat: ${lat.toFixed(6)}, Lng: ${lng.toFixed(6)}`;

                if (marker) marker.setLatLng([lat, lng]);
                else marker = L.marker([lat, lng]).addTo(map);

                marker.bindPopup(`<strong>${HECHO_DATA.titulo}</strong><br>Lat: ${lat.toFixed(6)}<br>Lng: ${lng.toFixed(6)}`).openPopup();
            };

            map.on('click', mapClickHandler);
        }

        titleEl?.focus();
    };

    const exitEditMode = () => {
        editing = false;
        const editBtn = document.getElementById('editBtn');
        editBtn.textContent = 'Editar';
        editBtn.classList.replace('btn-success', 'btn-primary');

        [titleEl, descEl, fechaEl, ubicacionEl].forEach(el => el?.removeAttribute('contenteditable'));
        [categoriasEl, etiquetasEl].forEach(list => {
            list?.querySelectorAll('li').forEach(li => li.removeAttribute('contenteditable'));
        });

        addImageControls?.remove();
        addImageControls = null;

        if (mapHint) { mapHint.remove(); mapHint = null; }
        if (map && mapClickHandler) { map.off('click', mapClickHandler); mapClickHandler = null; }

        // Guardar cambios en HECHO_DATA
        if (titleEl) HECHO_DATA.titulo = titleEl.textContent.trim();
        if (descEl) HECHO_DATA.descripcion = descEl.textContent.trim();

        if (categoriasEl) {
            const categorias = Array.from(categoriasEl.querySelectorAll('li'))
                .map(li => li.textContent.trim()).filter(Boolean);
            HECHO_DATA.categoria = categorias[0] || HECHO_DATA.categoria;
        }

        if (etiquetasEl) {
            HECHO_DATA.etiquetas = Array.from(etiquetasEl.querySelectorAll('li'))
                .map(li => ({ nombre: li.textContent.trim() }))
                .filter(e => e.nombre);
        }

        // URLs de multimedia
        HECHO_DATA.multimedia = Array.from(multimediaEl.querySelectorAll('img.multimedia-item'))
            .map(img => img.src);

        console.log('Cambios guardados:', HECHO_DATA);
        alert('✅ Cambios guardados correctamente!');
    };

    // ===== DROPDOWN NAVBAR =====
    const setupDesktopDropdown = () => {
        const desktopUserBtn = document.getElementById('desktopUserBtn');
        const desktopUserDropdown = document.getElementById('desktopUserDropdown');
        if (!desktopUserBtn || !desktopUserDropdown) return;

        desktopUserBtn.addEventListener('click', e => {
            e.stopPropagation();
            const isVisible = desktopUserDropdown.style.display === 'block';
            desktopUserDropdown.style.display = isVisible ? 'none' : 'block';
        });

        document.addEventListener('click', () => {
            if (desktopUserDropdown.style.display === 'block') {
                desktopUserDropdown.style.display = 'none';
            }
        });
    };

    // ===== INICIALIZAR FUNCIONALIDADES =====
    if (USER_DATA?.loggedIn) {
        setupDeleteModal();
        if (USER_DATA.role === 2) setupEditMode();
    }

    setupDesktopDropdown();

    console.log('Hecho cargado:', HECHO_DATA);
    console.log('Usuario:', USER_DATA);
});
