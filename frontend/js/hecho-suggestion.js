// js/hecho-suggestion.js
// Script completo para "hecho-suggestion.html"
// Demodata + UI: diffs, mapas (Leaflet), multimedia, modal expandido.
//
// En producción reemplazá los arrays por fetch a tu API y la simulación de
// aceptar/rechazar por llamadas reales al backend.

document.addEventListener('DOMContentLoaded', () => {

  /* ==========================
     Demo data (reemplazar por fetch real)
     ========================== */
  const HECHOS = [
    { id: 5, title: "Hecho 5 - original", description: "Descripción original del hecho 5.", ubicacion: "Barrio X, Ciudad Y", categoria: "Comunitario", etiqueta: "Evento", fecha: "2025-08-10", img: "", images: [], lat: -34.6, lng: -58.4 },
    { id: 7, title: "Hecho 7 - original", description: "Descripción original del hecho 7.", ubicacion: "Zona Z", categoria: "Vial", etiqueta: "Cortes", fecha: "2025-07-22", images: [], lat: -34.7, lng: -58.3 },
    // Ejemplo que compartiste (id 13)
    { id: 13, title: "Violento tiroteo en Quilmes", description: "Un tiroteo entre bandas dejó un herido grave en Quilmes.", ubicacion: "Quilmes, Buenos Aires", categoria: "Policial", etiqueta: "Tiroteo", fecha: "2023-10-21", img: "", images: [], lat: -34.7207, lng: -58.2546 }
  ];

  const moderacionNuevos = [
    { id: 'n-1', usuario: 'Ana López', title: 'Denuncia ilegal - nueva', descripcion: 'Propuesta: nuevo hecho por robo en local comercial, testigos informan 2 heridos.', fecha: '2025-09-02', img: '', images: [], lat: null, lng: null },
    { id: 'n-2', usuario: 'Carlos Ruiz', title: 'Incidente en parque', descripcion: 'Nuevo hecho reportado con fotos y coordenadas.', fecha: '2025-09-03', images: ['https://placehold.co/600x400?text=Parque+1'] }
  ];

  const moderacionModificados = [
    { id: 'm-1', usuario: 'Lucía Díaz', hechoId: 5, title: 'Hecho 5 (modificado)', descripcion: 'Cambio de ubicación: ahora indica Plaza Principal. Añade detalle de cortes de tránsito.', fecha: '2025-09-04', categoria: 'Vial', etiqueta: 'Cortes', ubicacion: 'Plaza Principal, Ciudad Y', images: [] },
    { id: 'm-2', usuario: 'Martín Soto', hechoId: 13, title: 'Violento tiroteo en Quilmes (corrección)', descripcion: 'Corrección: fueron 2 heridos y la zona es Av. Mitre y Smith. Se agrega detalle de unidad policial.', fecha: '2025-09-05', categoria: 'Policial', etiqueta: 'Tiroteo', ubicacion: 'Av. Mitre y Smith, Quilmes', lat: -34.7220, lng: -58.2550, images: [] }
  ];

  /* ==========================
     Helpers
     ========================== */
  function escapeHtml(s) {
    if (s === null || s === undefined) return '';
    return String(s)
      .replaceAll('&', '&amp;')
      .replaceAll('<', '&lt;')
      .replaceAll('>', '&gt;')
      .replaceAll('"', '&quot;')
      .replaceAll("'", '&#039;');
  }

  // Word-level diff using LCS; returns HTML with .diff-removed and .diff-added
  function escapeHtmlWithSpaces(s) {
    if (s === null || s === undefined) return '';
    return String(s)
      .replaceAll('&', '&amp;')
      .replaceAll('<', '&lt;')
      .replaceAll('>', '&gt;')
      .replaceAll('"', '&quot;')
      .replaceAll("'", '&#039;')
      .replaceAll(' ', '&nbsp;');  // <-- esto mantiene los espacios visibles
  }

  function diffWords(oldStr = '', newStr = '') {
    const a = oldStr ? oldStr.match(/\S+\s*/g) : [];
    const b = newStr ? newStr.match(/\S+\s*/g) : [];
    const n = a.length, m = b.length;

    const dp = Array.from({ length: n + 1 }, () => Array(m + 1).fill(0));
    for (let i = n - 1; i >= 0; i--) {
      for (let j = m - 1; j >= 0; j--) {
        if (a[i] === b[j]) dp[i][j] = dp[i+1][j+1] + 1;
        else dp[i][j] = Math.max(dp[i+1][j], dp[i][j+1]);
      }
    }

    const parts = [];
    let i = 0, j = 0;
    while (i < n || j < m) {
      if (i < n && j < m && a[i] === b[j]) {
        parts.push(escapeHtml(a[i]));
        i++; j++;
      } else {
        if (j < m && (i === n || dp[i][j+1] >= (i < n ? dp[i+1][j] : -1))) {
          parts.push(`<span class="diff-added">${escapeHtml(b[j])}</span>`);
          j++;
        } else if (i < n) {
          parts.push(`<span class="diff-removed">${escapeHtml(a[i])}</span>`);
          i++;
        } else break;
      }
    }

    return parts.join('');
  }

  // Función de escape normal
  function escapeHtml(s) {
    if (s == null) return '';
    return String(s)
      .replaceAll('&', '&amp;')
      .replaceAll('<', '&lt;')
      .replaceAll('>', '&gt;')
      .replaceAll('"', '&quot;')
      .replaceAll("'", '&#039;');
  }





  function diffCoords(beforeObj, afterObj) {
    const before = (beforeObj && beforeObj.lat != null && beforeObj.lng != null) ? `(${Number(beforeObj.lat).toFixed(5)}, ${Number(beforeObj.lng).toFixed(5)})` : '';
    const after = (afterObj && afterObj.lat != null && afterObj.lng != null) ? `(${Number(afterObj.lat).toFixed(5)}, ${Number(afterObj.lng).toFixed(5)})` : '';
    if (!before && !after) return '<em>No disponible</em>';
    if (!before) return `<span class="diff-added">${escapeHtml(after)}</span>`;
    if (!after) return `<span class="diff-removed">${escapeHtml(before)}</span>`;
    if (before === after) return escapeHtml(before);
    return `<span class="diff-removed">${escapeHtml(before)}</span> → <span class="diff-added">${escapeHtml(after)}</span>`;
  }

  function buildDiffHtml(beforeObj, afterObj) {
    if (!afterObj) return `<p><em>Sin datos de sugerencia</em></p>`;
    const parts = [];
    parts.push(`<div class="field"><strong>Título:</strong> ${diffWords(beforeObj?.title || '', afterObj.title || '')}</div>`);
    parts.push(`<div class="field"><strong>Descripción:</strong> ${diffWords(beforeObj?.description || beforeObj?.descripcion || '', afterObj.description || afterObj.descripcion || '')}</div>`);
    parts.push(`<div class="field"><strong>Ubicación:</strong> ${diffWords(beforeObj?.ubicacion || '', afterObj.ubicacion || '')}</div>`);
    parts.push(`<div class="field"><strong>Categoría:</strong> ${diffWords(beforeObj?.categoria || '', afterObj.categoria || '')}</div>`);
    parts.push(`<div class="field"><strong>Etiqueta:</strong> ${diffWords(beforeObj?.etiqueta || '', afterObj.etiqueta || '')}</div>`);
    parts.push(`<div class="field"><strong>Fecha:</strong> ${diffWords(beforeObj?.fecha || '', afterObj.fecha || '')}</div>`);
    parts.push(`<div class="field"><strong>Coordenadas:</strong> ${diffCoords(beforeObj, afterObj)}</div>`);
    return parts.join('');
  }

  function mountMultimedia(container, obj) {
    if (!container) return;
    container.innerHTML = '';
    let added = false;
    if (obj) {
      if (Array.isArray(obj.images) && obj.images.length) {
        obj.images.forEach(url => {
          const img = document.createElement('img');
          img.src = url;
          img.alt = escapeHtml(obj.title || 'Imagen');
          container.appendChild(img);
          added = true;
        });
      }
      if (!added && obj.img) {
        const img = document.createElement('img');
        img.src = obj.img;
        img.alt = escapeHtml(obj.title || 'Imagen');
        container.appendChild(img);
        added = true;
      }
    }
    if (!added) {
      const ph = document.createElement('div');
      ph.className = 'placeholder';
      ph.textContent = 'No hay contenido multimedia disponible';
      container.appendChild(ph);
    }
  }

  /* ==========================
     DOM refs + lectura id
     ========================== */
  const params = new URLSearchParams(window.location.search);
  const idParam = params.get('id'); // p.e. n-1 / m-2

  const elId = document.getElementById('hecho-id');
  const elUsuario = document.getElementById('hecho-usuario');
  const elFecha = document.getElementById('hecho-fecha');
  const elTitle = document.getElementById('hecho-title');

  const beforeBlock = document.getElementById('before-block');
  const afterBlock = document.getElementById('after-block');
  const multimediaContainer = document.getElementById('hecho-multimedia');
  const pageMapContainer = document.getElementById('hecho-map');

  const viewBtn = document.getElementById('viewSuggestionBtn');
  const acceptBtn = document.getElementById('acceptBtn');
  const rejectBtn = document.getElementById('rejectBtn');
  const expandBtn = document.getElementById('expandBtn');

  if (!idParam) {
    const root = document.getElementById('suggestion-detail');
    if (root) root.innerHTML = '<p>Sugerencia no especificada (falta ?id=...)</p>';
    return;
  }

  // encontrar sugerencia
  const sugerenciaNuevo = moderacionNuevos.find(s => s.id === idParam);
  const sugerenciaMod = moderacionModificados.find(s => s.id === idParam);
  const sugerencia = sugerenciaNuevo || sugerenciaMod;
  const tipo = Boolean(sugerenciaMod) ? 'modificado' : 'nuevo';

  if (!sugerencia) {
    const root = document.getElementById('suggestion-detail');
    if (root) root.innerHTML = `<p>Sugerencia con id="${escapeHtml(idParam)}" no encontrada.</p>`;
    return;
  }

  // cabecera
  if (elId) elId.textContent = escapeHtml(sugerencia.id);
  if (elUsuario) elUsuario.textContent = escapeHtml(sugerencia.usuario || 'Usuario desconocido');
  if (elFecha) elFecha.textContent = escapeHtml(sugerencia.fecha || '');
  if (elTitle) elTitle.textContent = escapeHtml(sugerencia.title || '(sin título)');

  // beforeObj (si modificado)
  let beforeObj = null;
  if (tipo === 'modificado' && sugerencia.hechoId != null) {
    beforeObj = HECHOS.find(h => String(h.id) === String(sugerencia.hechoId)) || null;
  }

  // normalizar afterObj a formato cercano al HECHO
  const afterObj = {
    title: sugerencia.title || '',
    description: sugerencia.descripcion || sugerencia.description || '',
    ubicacion: sugerencia.ubicacion || '',
    categoria: sugerencia.categoria || '',
    etiqueta: sugerencia.etiqueta || '',
    fecha: sugerencia.fecha || '',
    lat: ('lat' in sugerencia) ? sugerencia.lat : null,
    lng: ('lng' in sugerencia) ? sugerencia.lng : null,
    img: sugerencia.img || '',
    images: sugerencia.images || []
  };

  // Exponer para modal
  window.__modal_beforeObj = beforeObj;
  window.__modal_afterObj = afterObj;

  /* ==========================
     Renderizar BEFORE / AFTER en la página
     ========================== */
  if (beforeBlock) {
    if (tipo === 'modificado') {
      // mostramos el contenido "antes" (sin diffs mezclados, como referencia)
      beforeBlock.innerHTML = `<strong>Antes</strong><div class="fields">${buildDiffHtml(beforeObj, beforeObj)}</div>`;
    } else {
      beforeBlock.innerHTML = `<strong>Antes</strong><div class="fields"><em>No existe (nuevo)</em></div>`;
    }
    // multimedia del antes
    const beforeMedia = document.createElement('div');
    beforeMedia.className = 'media-block';
    beforeMedia.style.marginTop = '0.6rem';
    beforeBlock.appendChild(beforeMedia);
    mountMultimedia(beforeMedia, beforeObj || {});
  }

  if (afterBlock) {
    afterBlock.innerHTML = `<strong>Después</strong><div class="fields">${buildDiffHtml(beforeObj, afterObj)}</div>`;
    const afterMedia = document.createElement('div');
    afterMedia.className = 'media-block';
    afterMedia.style.marginTop = '0.6rem';
    afterBlock.appendChild(afterMedia);
    mountMultimedia(afterMedia, afterObj);
  }

  // multimedia general (derecha): preferir sugerencia, sino original
  if (multimediaContainer) {
    const mmSource = (afterObj.images && afterObj.images.length) || afterObj.img ? afterObj : beforeObj;
    mountMultimedia(multimediaContainer, mmSource || {});
  }

  /* ==========================
     Montar mapa en la página (Leaflet)
     ========================== */
  function mountPageMap(beforeObjLocal, afterObjLocal) {
    if (!pageMapContainer) return;
    if (typeof L === 'undefined') {
      pageMapContainer.innerHTML = '<p style="padding:0.6rem;margin:0;color:#666">Leaflet no está cargado. Incluí Leaflet para ver el mapa.</p>';
      return;
    }
    // limpiar
    pageMapContainer.innerHTML = '';
    const defaultCenter = (afterObjLocal.lat && afterObjLocal.lng) ? [afterObjLocal.lat, afterObjLocal.lng] :
                          (beforeObjLocal && beforeObjLocal.lat && beforeObjLocal.lng) ? [beforeObjLocal.lat, beforeObjLocal.lng] :
                          [-34.6, -58.4];
    const map = L.map(pageMapContainer).setView(defaultCenter, 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    const redIcon = L.icon({ iconUrl: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png', iconSize: [32,32], iconAnchor: [16,32] });
    const greenIcon = L.icon({ iconUrl: 'https://maps.google.com/mapfiles/ms/icons/green-dot.png', iconSize: [32,32], iconAnchor: [16,32] });

    const markers = [];
    if (beforeObjLocal && beforeObjLocal.lat != null && beforeObjLocal.lng != null) {
      const mk = L.marker([beforeObjLocal.lat, beforeObjLocal.lng], { icon: redIcon }).addTo(map)
        .bindPopup(`<strong>Ubicación anterior</strong><br>${escapeHtml(beforeObjLocal.title || '')}`);
      markers.push(mk);
    }
    if (afterObjLocal && afterObjLocal.lat != null && afterObjLocal.lng != null) {
      const mk2 = L.marker([afterObjLocal.lat, afterObjLocal.lng], { icon: greenIcon }).addTo(map)
        .bindPopup(`<strong>Ubicación nueva</strong><br>${escapeHtml(afterObjLocal.title || '')}`);
      markers.push(mk2);
    }

    if (markers.length === 2) {
      const latlngs = markers.map(m => m.getLatLng());
      const bounds = L.latLngBounds(latlngs[0], latlngs[1]);
      map.fitBounds(bounds.pad(0.25));
    } else if (markers.length === 1) {
      map.setView(markers[0].getLatLng(), 14);
    }
  }

  mountPageMap(beforeObj, afterObj);

  /* ==========================
     View Sugerencia: scroll + highlight
     ========================== */
  if (viewBtn) {
    viewBtn.setAttribute('href', '#');
    viewBtn.addEventListener('click', (e) => {
      e.preventDefault();
      if (!afterBlock) return;
      afterBlock.scrollIntoView({ behavior: 'smooth', block: 'center' });
      afterBlock.style.transition = 'box-shadow 0.25s, transform 0.25s';
      afterBlock.style.boxShadow = '0 10px 30px rgba(47,96,244,0.14)';
      afterBlock.style.transform = 'translateY(-3px)';
      setTimeout(() => {
        afterBlock.style.boxShadow = '';
        afterBlock.style.transform = '';
      }, 1400);
    });
  }

  /* ==========================
     Aceptar / Rechazar (simulado)
     ========================== */
  function finalizeAction(action) {
    const confirmMsg = action === 'accept'
      ? `¿Confirmás aceptar la sugerencia ${escapeHtml(sugerencia.id)}?`
      : `¿Confirmás rechazar la sugerencia ${escapeHtml(sugerencia.id)}?`;
    if (!confirm(confirmMsg)) return;

    // TODO: Reemplazar por fetch real al backend
    // fetch('/api/moderacion/resolve', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ id: sugerencia.id, action }) })

    const btn = action === 'accept' ? acceptBtn : rejectBtn;
    if (btn) { btn.disabled = true; btn.textContent = 'Procesando...'; }
    setTimeout(() => {
      alert(`Sugerencia ${escapeHtml(sugerencia.id)} ${action === 'accept' ? 'aceptada' : 'rechazada'} (simulado).`);
      if (document.referrer && document.referrer.includes('admin')) {
        window.location.href = document.referrer;
      } else {
        try { history.back(); } catch (e) { window.location.href = '/'; }
      }
    }, 700);
  }

  if (acceptBtn) acceptBtn.addEventListener('click', () => finalizeAction('accept'));
  if (rejectBtn) rejectBtn.addEventListener('click', () => finalizeAction('reject'));

  /* ==========================
     Modal expandido: abrir / cerrar / mapas
     ========================== */
  (function () {
    const expandBtnEl = expandBtn;
    const compareModal = document.getElementById('compareModal');
    const compareBackdrop = document.getElementById('compareBackdrop');
    const compareClose = document.getElementById('compareClose');
    const compareCloseFooter = document.getElementById('compareCloseFooter');
    const modalBefore = document.getElementById('modal-before');
    const modalAfter = document.getElementById('modal-after');
    const modalMapBefore = document.getElementById('modal-map-before');
    const modalMapAfter = document.getElementById('modal-map-after');
    const compareAccept = document.getElementById('compareAccept');
    const compareReject = document.getElementById('compareReject');

    let modalMapBeforeInstance = null;
    let modalMapAfterInstance = null;

    function cloneBlock(sourceEl, targetEl) {
      if (!sourceEl || !targetEl) return;
      targetEl.innerHTML = sourceEl.innerHTML;
      // remover cualquier contenedor de mapa embebido
      const innerMap = targetEl.querySelector('#hecho-map');
      if (innerMap) innerMap.remove();
    }

    function initSmallMap(container, obj, color = 'green') {
      if (!container) return null;
      container.innerHTML = '';
      if (typeof L === 'undefined') {
        container.innerHTML = '<p style="padding:0.5rem">Leaflet no cargado.</p>';
        return null;
      }
      const lat = (obj && obj.lat != null) ? obj.lat : -34.6;
      const lng = (obj && obj.lng != null) ? obj.lng : -58.4;
      const map = L.map(container).setView([lat, lng], 13);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      }).addTo(map);

      const iconUrl = color === 'red'
        ? 'https://maps.google.com/mapfiles/ms/icons/red-dot.png'
        : 'https://maps.google.com/mapfiles/ms/icons/green-dot.png';
      const icon = L.icon({ iconUrl, iconSize: [32,32], iconAnchor: [16,32] });

      if (obj && obj.lat != null && obj.lng != null) {
        const mk = L.marker([obj.lat, obj.lng], { icon }).addTo(map);
        if (obj.title) mk.bindPopup(escapeHtml(obj.title));
        map.setView([obj.lat, obj.lng], 14);
      }
      return map;
    }

    function openCompareModal() {
      cloneBlock(beforeBlock, modalBefore);
      cloneBlock(afterBlock, modalAfter);

      if (compareModal) {
        compareModal.setAttribute('aria-hidden', 'false');
        compareModal.style.display = 'flex';
      }
      if (compareClose) compareClose.focus();
    }

    function closeCompareModal() {
      try {
        if (modalMapBeforeInstance && modalMapBeforeInstance.remove) modalMapBeforeInstance.remove();
        if (modalMapAfterInstance && modalMapAfterInstance.remove) modalMapAfterInstance.remove();
      } catch (err) { /* ignore */ }
      modalMapBeforeInstance = null;
      modalMapAfterInstance = null;

      if (compareModal) {
        compareModal.setAttribute('aria-hidden', 'true');
        compareModal.style.display = 'none';
      }
      if (expandBtnEl) expandBtnEl.focus();
    }

    if (expandBtnEl) expandBtnEl.addEventListener('click', openCompareModal);
    if (compareBackdrop) compareBackdrop.addEventListener('click', closeCompareModal);
    if (compareClose) compareClose.addEventListener('click', closeCompareModal);
    if (compareCloseFooter) compareCloseFooter.addEventListener('click', closeCompareModal);

    if (compareAccept) {
      compareAccept.addEventListener('click', () => {
        if (acceptBtn) acceptBtn.click();
        closeCompareModal();
      });
    }
    if (compareReject) {
      compareReject.addEventListener('click', () => {
        if (rejectBtn) rejectBtn.click();
        closeCompareModal();
      });
    }

    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape' && compareModal && compareModal.getAttribute('aria-hidden') === 'false') {
        closeCompareModal();
      }
    });

  })();

  /* ==========================
     Accesibilidad / atajos
     ========================== */
  document.addEventListener('keydown', (e) => {
    if (e.key === 'a' || e.key === 'A') { if (acceptBtn) { acceptBtn.focus(); acceptBtn.click(); } }
    if (e.key === 'r' || e.key === 'R') { if (rejectBtn) { rejectBtn.focus(); rejectBtn.click(); } }
    if (e.key === 'v' || e.key === 'V') { if (viewBtn) { viewBtn.focus(); viewBtn.click(); } }
    if (e.key === 'e' || e.key === 'E') { if (expandBtn) { expandBtn.focus(); expandBtn.click(); } }
  });

});
