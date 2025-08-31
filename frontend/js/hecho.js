document.addEventListener('DOMContentLoaded', () => {
  const hechos = HECHOS;

  const urlParams = new URLSearchParams(window.location.search);
  const id = parseInt(urlParams.get('id'));
  const hecho = hechos.find(h => h.id === id);

  if (hecho) {
    // Título y descripción
    document.getElementById('hecho-title').textContent = hecho.title;
    document.getElementById('hecho-description').textContent = hecho.description;

    // Categorías
    const categoriasEl = document.getElementById('hecho-categorias');
    categoriasEl.innerHTML = hecho.categoria ? `<li>${hecho.categoria}</li>` : '';

    // Etiquetas
    const etiquetasEl = document.getElementById('hecho-etiquetas');
    etiquetasEl.innerHTML = hecho.etiqueta ? `<li>${hecho.etiqueta}</li>` : '';

    // Fecha
    const fechaEl = document.getElementById('hecho-fecha');
    if (fechaEl && hecho.fecha) {
      fechaEl.textContent = hecho.fecha;
    }

    // Ubicación textual
    const ubicacionEl = document.getElementById('hecho-ubicacion');
    if (ubicacionEl && hecho.ubicacion) {
      ubicacionEl.textContent = hecho.ubicacion;
    }

    // Mapa
    const mapEl = document.getElementById('hecho-map');
    if (mapEl && hecho.lat && hecho.lng) {
      const map = L.map('hecho-map').setView([hecho.lat, hecho.lng], 13);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      }).addTo(map);

      L.marker([hecho.lat, hecho.lng]).addTo(map)
      .bindPopup(`<strong>${hecho.title}</strong><br>${hecho.fecha}`)
      .openPopup();
    }

    // Multimedia
    const multimediaEl = document.getElementById('hecho-multimedia');
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
  } else {
    const detalle = document.getElementById('hecho-detail');
    detalle.innerHTML = '<p>Hecho no encontrado.</p>';
  }
});
