// js/collection-detail.js
// Usa HECHOS (de js/hechos.js) — asegúrate de cargar hechos.js antes de este script

(function () {
  function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
  }

  // Map simple colecciónId -> categoría (ajustá si tenés otra lógica)
  const coleccionMap = {
    1: "Historia",
    2: "Cultura",
    3: "Evento"
  };

  function safeText(s) {
    return (s === null || s === undefined) ? '' : String(s);
  }

  function createCard(h) {
    const a = document.createElement('a');
    a.className = 'card';
    a.href = `hecho.html?id=${encodeURIComponent(h.id)}`;

    // título (columna 1)
    const titleHTML = `<div class="card-title">${safeText(h.title)}</div>`;

    // descripción (columna 2)
    const descHTML = `<div class="card-description"><p>${safeText(h.description)}</p></div>`;

    // datos (columna 3)
    const parts = [];
    if (h.ubicacion) parts.push(`<p>📍 ${safeText(h.ubicacion)}</p>`);
    if (h.categoria) parts.push(`<p>🏷️ ${safeText(h.categoria)}</p>`);
    if (h.etiqueta) parts.push(`<p>🔖 ${safeText(h.etiqueta)}</p>`);
    if (h.fecha) parts.push(`<p>📅 ${safeText(h.fecha)}</p>`);
    const dataHTML = `<div class="card-data">${parts.join('')}</div>`;

    // media (columna 4) - la dejamos al final para que aparezca a la derecha
    let mediaHTML = '';
    if (h.img && h.img.trim() !== '') {
        mediaHTML = `<div class="card-img"><img src="${h.img}" alt="${safeText(h.title)}"></div>`;
    } else {
        const initial = safeText(h.title).trim().charAt(0) || '';
        mediaHTML = `<div class="card-img"><div class="card-icon">${initial}</div></div>`;
    }

    // concat: title | description | data | photo
    a.innerHTML = titleHTML + descHTML + dataHTML + mediaHTML;
    return a;
    }


  function renderHechos() {
    const coleccionId = parseInt(getQueryParam("id"), 10);
    const categoria = coleccionMap[coleccionId] || null;
    const container = document.getElementById("hechos-container");
    if (!container) return;

    container.innerHTML = "";

    const hechosFiltrados = (typeof HECHOS !== 'undefined' && Array.isArray(HECHOS))
      ? (categoria ? HECHOS.filter(h => h.categoria === categoria) : HECHOS.slice())
      : [];

    if (hechosFiltrados.length === 0) {
      container.innerHTML = "<p>No hay hechos en esta colección.</p>";
      return;
    }

    hechosFiltrados.forEach(h => {
      const card = createCard(h);
      container.appendChild(card);
    });
  }

  document.addEventListener("DOMContentLoaded", renderHechos);
})();
