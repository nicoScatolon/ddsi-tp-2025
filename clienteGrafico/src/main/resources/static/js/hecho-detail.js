// hecho-interactive.js
// Funcionalidades interactivas: mapa, modal, modo edición

document.addEventListener('DOMContentLoaded', () => {
    // ====== INICIALIZAR MAPA ======
    const mapEl = document.getElementById('hecho-map');
    let map, marker;

    if (mapEl && typeof L !== 'undefined') {
        const lat = parseFloat(mapEl.dataset.lat);
        const lng = parseFloat(mapEl.dataset.lng);
        const title = mapEl.dataset.title || '';
        const fecha = mapEl.dataset.fecha || '';

        if (!isNaN(lat) && !isNaN(lng)) {
            map = L.map('hecho-map').setView([lat, lng], 13);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© OpenStreetMap contributors'
            }).addTo(map);

            marker = L.marker([lat, lng]).addTo(map)
                .bindPopup(`<strong>${title}</strong><br>${fecha}`)
                .openPopup();
        }
    }

    // ====== MODAL DE ELIMINACIÓN ======
    const modal = document.getElementById('deleteModal');
    const deleteBtn = document.getElementById('deleteBtn');
    const closeBtn = modal ? modal.querySelector('.close') : null;

    if (deleteBtn && modal) {
        deleteBtn.addEventListener('click', () => {
            modal.style.display = 'block';
        });
    }

    if (closeBtn && modal) {
        closeBtn.addEventListener('click', () => {
            modal.style.display = 'none';
        });
    }

    window.addEventListener('click', (e) => {
        if (modal && e.target === modal) {
            modal.style.display = 'none';
        }
    });

    // ====== MODO EDICIÓN ======
    const editBtn = document.getElementById('editBtn');
    if (!editBtn) return; // Si no hay botón de editar, salir

    const titleEl = document.getElementById('hecho-title');
    const descEl = document.getElementById('hecho-description');
    const categoriasEl = document.getElementById('hecho-categorias');
    const etiquetasEl = document.getElementById('hecho-etiquetas');
    const fechaEl = document.getElementById('hecho-fecha');
    const ubicacionEl = document.getElementById('hecho-ubicacion');
    const fuenteEl = document.getElementById('hecho-fuente');
    const multimediaEl = document.getElementById('hecho-multimedia');

    let editing = false;
    let addImageControls = null;
    let mapClickHandler = null;
    let mapHint = null;

    editBtn.addEventListener('click', () => {
        if (!editing) {
            // ===== ENTRAR EN MODO EDICIÓN =====
            editing = true;
            editBtn.textContent = 'Guardar cambios';
            editBtn.classList.remove('btn-primary');
            editBtn.classList.add('btn-success');

            // Hacer editables los campos
            titleEl.setAttribute('contenteditable', 'true');
            descEl.setAttribute('contenteditable', 'true');

            // Para las listas, hacemos editable el primer <li> o creamos uno
            const makeListEditable = (ulEl) => {
                const firstLi = ulEl.querySelector('li');
                if (firstLi) {
                    firstLi.setAttribute('contenteditable', 'true');
                } else {
                    const newLi = document.createElement('li');
                    newLi.setAttribute('contenteditable', 'true');
                    newLi.textContent = 'Editar aquí';
                    ulEl.appendChild(newLi);
                }
            };

            makeListEditable(categoriasEl);
            makeListEditable(etiquetasEl);

            fechaEl.setAttribute('contenteditable', 'true');
            ubicacionEl.setAttribute('contenteditable', 'true');
            fuenteEl.setAttribute('contenteditable', 'true');

            // ===== CONTROLES PARA AGREGAR IMÁGENES =====
            addImageControls = document.createElement('div');
            addImageControls.className = 'image-controls';
            addImageControls.style.marginTop = '12px';
            addImageControls.innerHTML = `
        <button class="btn-secondary" id="addImageUrlBtn" type="button">Agregar imagen por URL</button>
        <input type="file" id="uploadImageInput" accept="image/*" style="display:none;" />
        <button class="btn-secondary" id="uploadImageBtn" type="button">Subir imagen desde PC</button>
      `;
            multimediaEl.appendChild(addImageControls);

            // Función para quitar placeholder
            const removePlaceholderIfAny = () => {
                const ph = multimediaEl.querySelector('.placeholder');
                if (ph) ph.remove();
            };

            // Agregar por URL
            const addImageUrlBtn = addImageControls.querySelector('#addImageUrlBtn');
            addImageUrlBtn.addEventListener('click', () => {
                const url = prompt('Ingrese la URL de la imagen:');
                if (url) {
                    removePlaceholderIfAny();
                    const img = document.createElement('img');
                    img.src = url;
                    img.alt = titleEl.textContent;
                    multimediaEl.insertBefore(img, addImageControls);
                }
            });

            // Subir desde PC
            const uploadInput = addImageControls.querySelector('#uploadImageInput');
            const uploadBtn = addImageControls.querySelector('#uploadImageBtn');

            uploadBtn.addEventListener('click', () => {
                uploadInput.click();
            });

            uploadInput.addEventListener('change', (e) => {
                const file = e.target.files && e.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function(ev) {
                        removePlaceholderIfAny();
                        const img = document.createElement('img');
                        img.src = ev.target.result;
                        img.alt = titleEl.textContent;
                        multimediaEl.insertBefore(img, addImageControls);
                    };
                    reader.readAsDataURL(file);
                    e.target.value = '';
                }
            });

            // ===== EDITAR UBICACIÓN EN EL MAPA =====
            if (mapEl && map) {
                mapHint = document.createElement('div');
                mapHint.className = 'map-hint';
                mapHint.textContent = '📍 Haz clic en el mapa para seleccionar una nueva ubicación';
                mapHint.style.cssText = 'background:#ffeaa7;padding:8px;margin-bottom:8px;border-radius:4px;text-align:center;font-size:14px;';
                mapEl.parentNode.insertBefore(mapHint, mapEl);

                mapClickHandler = function (e) {
                    const { lat, lng } = e.latlng;

                    // Actualizar data attributes
                    mapEl.dataset.lat = lat;
                    mapEl.dataset.lng = lng;

                    // Actualizar texto de ubicación
                    ubicacionEl.textContent = `Lat: ${lat.toFixed(5)}, Lng: ${lng.toFixed(5)}`;

                    // Mover marcador
                    if (marker) {
                        marker.setLatLng([lat, lng]);
                        marker.bindPopup(`<strong>${titleEl.textContent}</strong><br>${fechaEl.textContent}`).openPopup();
                    } else {
                        marker = L.marker([lat, lng]).addTo(map);
                        marker.bindPopup(`<strong>${titleEl.textContent}</strong><br>${fechaEl.textContent}`).openPopup();
                    }
                };
                map.on('click', mapClickHandler);
            }

            titleEl.focus();

        } else {
            // ===== SALIR DEL MODO EDICIÓN (GUARDAR) =====
            editing = false;
            editBtn.textContent = 'Editar';
            editBtn.classList.remove('btn-success');
            editBtn.classList.add('btn-primary');

            // Quitar editable
            titleEl.removeAttribute('contenteditable');
            descEl.removeAttribute('contenteditable');
            fechaEl.removeAttribute('contenteditable');
            ubicacionEl.removeAttribute('contenteditable');
            fuenteEl.removeAttribute('contenteditable');

            // Quitar editable de listas
            categoriasEl.querySelectorAll('li').forEach(li => li.removeAttribute('contenteditable'));
            etiquetasEl.querySelectorAll('li').forEach(li => li.removeAttribute('contenteditable'));

            // Quitar controles de imágenes
            if (addImageControls) {
                addImageControls.remove();
                addImageControls = null;
            }

            // Quitar hint del mapa y desactivar click
            if (map && mapClickHandler) {
                map.off('click', mapClickHandler);
                mapClickHandler = null;
            }
            if (mapHint) {
                mapHint.remove();
                mapHint = null;
            }

            // ===== ENVIAR DATOS AL SERVIDOR =====
            // Recopilar datos editados
            const editedData = {
                title: titleEl.textContent.trim(),
                description: descEl.textContent.trim(),
                categoria: categoriasEl.querySelector('li')?.textContent.trim() || '',
                etiqueta: etiquetasEl.querySelector('li')?.textContent.trim() || '',
                fecha: fechaEl.textContent.trim(),
                ubicacion: ubicacionEl.textContent.trim(),
                fuente: fuenteEl.textContent.trim(),
                lat: parseFloat(mapEl?.dataset.lat) || null,
                lng: parseFloat(mapEl?.dataset.lng) || null,
                // Recopilar URLs de imágenes
                images: Array.from(multimediaEl.querySelectorAll('img')).map(img => img.src)
            };

            // Aquí deberías hacer un fetch al servidor para guardar
            // Ejemplo:
            /*
            fetch('/hecho/update', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(editedData)
            })
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                alert('Cambios guardados exitosamente!');
                // Opcionalmente recargar la página
                // window.location.reload();
              } else {
                alert('Error al guardar: ' + data.message);
              }
            })
            .catch(err => {
              alert('Error al guardar los cambios');
              console.error(err);
            });
            */

            // Por ahora solo mostramos alerta
            alert('Cambios guardados! (En producción se enviarían al servidor)');
            console.log('Datos editados:', editedData);
        }
    });
});