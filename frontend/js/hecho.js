document.addEventListener('DOMContentLoaded', () => {
  const hechos = HECHOS;

  const urlParams = new URLSearchParams(window.location.search);
  const id = parseInt(urlParams.get('id'));
  const hecho = hechos.find(h => h.id === id);

  if (hecho) {
    // Elementos
    const titleEl = document.getElementById('hecho-title');
    const descEl = document.getElementById('hecho-description');
    const categoriasEl = document.getElementById('hecho-categorias');
    const etiquetasEl = document.getElementById('hecho-etiquetas');
    const fechaEl = document.getElementById('hecho-fecha');
    const ubicacionEl = document.getElementById('hecho-ubicacion');
    const mapEl = document.getElementById('hecho-map');
    const multimediaEl = document.getElementById('hecho-multimedia');
    const fuenteEl = document.getElementById('hecho-fuente');

    // Rellenar datos
    titleEl.textContent = hecho.title;
    descEl.textContent = hecho.description;
    categoriasEl.innerHTML = hecho.categoria ? `<li>${hecho.categoria}</li>` : '';
    etiquetasEl.innerHTML = hecho.etiqueta ? `<li>${hecho.etiqueta}</li>` : '';
    fechaEl.textContent = hecho.fecha || '';
    ubicacionEl.textContent = hecho.ubicacion || '';
    fuenteEl.textContent = hecho.fuente || 'Sin fuente disponible'; 

    // Mapa (Leaflet)
    let map, marker;
    if (mapEl && typeof L !== 'undefined' && hecho.lat && hecho.lng) {
      map = L.map('hecho-map').setView([hecho.lat, hecho.lng], 13);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      }).addTo(map);

      marker = L.marker([hecho.lat, hecho.lng]).addTo(map)
        .bindPopup(`<strong>${hecho.title}</strong><br>${hecho.fecha}`)
        .openPopup();
    }

    // Multimedia inicial
    multimediaEl.innerHTML = '';
    let hasContent = false;

    if (hecho.img) {
      const img = document.createElement('img');
      img.src = hecho.img;
      img.alt = hecho.title;
      multimediaEl.appendChild(img);
      hasContent = true;
    }
    if (hecho.video) {
      const video = document.createElement('video');
      video.controls = true;
      const source = document.createElement('source');
      source.src = hecho.video;
      source.type = 'video/mp4';
      video.appendChild(source);
      multimediaEl.appendChild(video);
      hasContent = true;
    }
    if (hecho.images && hecho.images.length) {
      hecho.images.forEach(url => {
        const img = document.createElement('img');
        img.src = url;
        img.alt = hecho.title;
        multimediaEl.appendChild(img);
        hasContent = true;
      });
    }
    if (!hasContent) {
      const placeholder = document.createElement('div');
      placeholder.className = 'placeholder';
      placeholder.textContent = 'No hay contenido multimedia disponible';
      multimediaEl.appendChild(placeholder);
    }

    // Título de la pestaña
    document.title = hecho.title + ' | MetaMapa';

    // Usuario actual (ejemplo)
    const currentUser = {
      loggedIn: true,
      role: 'admin' // o 'contribuyente' o 'guest'
    };

    if (currentUser.loggedIn && (currentUser.role === 'admin' || currentUser.role === 'contribuyente')) {
      const actionsEl = document.getElementById('hecho-actions');
      actionsEl.innerHTML = '';

      // Botón "Solicitar eliminación"
      const deleteBtn = document.createElement('button');
      deleteBtn.textContent = 'Solicitar eliminación';
      deleteBtn.className = 'btn-warning';
      actionsEl.appendChild(deleteBtn);

      // Botón "Editar/Guardar cambios"
      const editBtn = document.createElement('button');
      editBtn.textContent = 'Editar';
      editBtn.className = 'btn-primary';
      actionsEl.appendChild(editBtn);

      let editing = false;
      let addImageControls = null; // contenedor con botones + input file
      let mapClickHandler = null;
      let mapHint = null;

      editBtn.addEventListener('click', () => {
        if (!editing) {
          // --- ENTRAR EN MODO EDICIÓN ---
          editing = true;
          editBtn.textContent = 'Guardar cambios';

          // Hacer editables todos los campos excepto id
          titleEl.setAttribute('contenteditable', 'true');
          descEl.setAttribute('contenteditable', 'true');
          categoriasEl.setAttribute('contenteditable', 'true');
          etiquetasEl.setAttribute('contenteditable', 'true');
          fechaEl.setAttribute('contenteditable', 'true');
          ubicacionEl.setAttribute('contenteditable', 'true');
          fuenteEl.setAttribute('contenteditable', 'true'); 

          const latEl = document.getElementById('hecho-lat');
          const lngEl = document.getElementById('hecho-lng');
          if (latEl) latEl.setAttribute('contenteditable', 'true');
          if (lngEl) lngEl.setAttribute('contenteditable', 'true');

          // --- Controles para agregar imágenes (URL + subir desde PC) ---
          addImageControls = document.createElement('div');
          addImageControls.className = 'image-controls';
          addImageControls.innerHTML = `
            <button class="btn-secondary" id="addImageUrlBtn" type="button">Agregar imagen por URL</button>
            <input type="file" id="uploadImageInput" accept="image/*" style="display:none;" />
            <button class="btn-secondary" id="uploadImageBtn" type="button">Subir imagen desde PC</button>
          `;

          // Append debajo del contenedor multimedia (alineado al final)
          multimediaEl.appendChild(addImageControls);

          // Función utilitaria para quitar placeholder si existe
          const removePlaceholderIfAny = () => {
            const ph = multimediaEl.querySelector('.placeholder');
            if (ph) ph.remove();
          };

          // Manejo agregar por URL
          const addImageUrlBtn = addImageControls.querySelector('#addImageUrlBtn');
          addImageUrlBtn.addEventListener('click', () => {
            const url = prompt('Ingrese la URL de la imagen:');
            if (url) {
              removePlaceholderIfAny();
              const img = document.createElement('img');
              img.src = url;
              img.alt = hecho.title;
              multimediaEl.insertBefore(img, addImageControls); // insertar antes de los controles
              if (!hecho.images) hecho.images = [];
              hecho.images.push(url);
            }
          });

          // Manejo subir desde PC
          const uploadInput = addImageControls.querySelector('#uploadImageInput');
          const uploadBtn = addImageControls.querySelector('#uploadImageBtn');

          uploadBtn.addEventListener('click', () => {
            uploadInput.click();
          });

          const handleFileSelect = (e) => {
            const file = e.target.files && e.target.files[0];
            if (file) {
              const reader = new FileReader();
              reader.onload = function(ev) {
                removePlaceholderIfAny();
                const img = document.createElement('img');
                img.src = ev.target.result;
                img.alt = hecho.title;
                multimediaEl.insertBefore(img, addImageControls); // insertar antes de los controles
                if (!hecho.images) hecho.images = [];
                hecho.images.push(ev.target.result); // Guarda base64 temporalmente
              };
              reader.readAsDataURL(file);
              // limpiar input para permitir seleccionar el mismo archivo de nuevo si se necesita
              e.target.value = '';
            }
          };

          uploadInput.addEventListener('change', handleFileSelect);

          // --- Mostrar hint en el mapa (solo en modo edición) ---
          if (mapEl && map) {
            mapHint = document.createElement('div');
            mapHint.className = 'map-hint';
            mapHint.textContent = '📍 Haz clic en el mapa para seleccionar una nueva ubicación';
            // Insertar el hint justo antes del elemento del mapa (dentro wrapper)
            mapEl.parentNode.insertBefore(mapHint, mapEl);

            // Habilitar click en el mapa para cambiar ubicación
            mapClickHandler = function (e) {
              const { lat, lng } = e.latlng;
              hecho.lat = lat;
              hecho.lng = lng;
              ubicacionEl.textContent = `Lat: ${lat.toFixed(5)}, Lng: ${lng.toFixed(5)}`;
              if (marker) {
                marker.setLatLng([lat, lng]);
                marker.bindPopup(`<strong>${hecho.title}</strong><br>${hecho.fecha}`).openPopup();
              } else {
                marker = L.marker([lat, lng]).addTo(map);
                marker.bindPopup(`<strong>${hecho.title}</strong><br>${hecho.fecha}`).openPopup();
              }
            };
            map.on('click', mapClickHandler);
          }

          titleEl.focus();

        } else {
          // --- SALIR DEL MODO EDICIÓN (GUARDAR) ---
          editing = false;
          editBtn.textContent = 'Editar';

          // Quitar editable
          [titleEl, descEl, categoriasEl, etiquetasEl, fechaEl, ubicacionEl].forEach(el => el.removeAttribute('contenteditable'));
          const latEl = document.getElementById('hecho-lat');
          const lngEl = document.getElementById('hecho-lng');
          if (latEl) latEl.removeAttribute('contenteditable');
          if (lngEl) lngEl.removeAttribute('contenteditable');

          // Quitar controles de imágenes y listeners
          if (addImageControls) {
            // remover event listeners internal (no referencia global) - se elimina el nodo y con él los listeners
            const uploadInput = addImageControls.querySelector('#uploadImageInput');
            if (uploadInput) {
              uploadInput.removeEventListener('change', null); // safe attempt (handler was attached inline), but removing node is suficiente
            }
            addImageControls.remove();
            addImageControls = null;
          }

          // Quitar hint del mapa y desactivar click handler
          if (map && mapClickHandler) {
            map.off('click', mapClickHandler);
            mapClickHandler = null;
          }
          if (mapHint) {
            mapHint.remove();
            mapHint = null;
          }

          // Guardar cambios en objeto (actualiza hecho)
          hecho.title = titleEl.textContent.trim();
          hecho.description = descEl.textContent.trim();
          hecho.categoria = categoriasEl.textContent.trim();
          hecho.etiqueta = etiquetasEl.textContent.trim();
          hecho.fecha = fechaEl.textContent.trim();
          hecho.ubicacion = ubicacionEl.textContent.trim();
          hecho.fuente = fuenteEl.textContent.trim(); 

          // lat/lng: si el DOM tiene elementos hechos-lat/hecho-lng, preferirlos (si existen y parsean)
          if (latEl) hecho.lat = parseFloat(latEl.textContent.trim()) || hecho.lat;
          if (lngEl) hecho.lng = parseFloat(lngEl.textContent.trim()) || hecho.lng;

          // Nota: aquí solo actualizamos el objeto en memoria. Si querés persistir al servidor,
          // habría que hacer un fetch/POST con los datos guardados (no implementado aquí).

          alert('Cambios guardados!');
        }
      });


      // Modal de eliminación
      const modal = document.getElementById('deleteModal');
      const closeBtn = modal ? modal.querySelector('.close') : null;
      const form = document.getElementById('deleteForm');

      if (deleteBtn) {
        deleteBtn.addEventListener('click', () => {
          if (modal) modal.style.display = 'block';
        });
      }

      if (closeBtn) {
        closeBtn.addEventListener('click', () => {
          modal.style.display = 'none';
        });
      }

      window.addEventListener('click', (e) => {
        if (modal && e.target === modal) {
          modal.style.display = 'none';
        }
      });

      if (form) {
        form.addEventListener('submit', (e) => {
          e.preventDefault();
          const reasonEl = document.getElementById('reason');
          const reason = reasonEl ? reasonEl.value.trim() : '';
          if (reason) {
            alert('Solicitud enviada con la razón: ' + reason);
            form.reset();
            if (modal) modal.style.display = 'none';
          } else {
            alert('Por favor ingresa una razón.');
          }
        });
      }
    }

  } else {
    const detalle = document.getElementById('hecho-detail');
    detalle.innerHTML = '<p>Hecho no encontrado.</p>';
  }
});
