// js/collections.js
(function () {
  const fallbackColors = [
    "#2F60F4", "#1746D2", "#FF6B6B", "#FFA94D",
    "#51CF66", "#845EF7", "#20C997", "#FF8787"
  ];

  const PAGE_SIZE = 16;
  let currentPage = 1; // página actual (1-indexed)

  function getHechoById(id) {
    if (!window.HECHOS || !Array.isArray(window.HECHOS)) return null;
    return window.HECHOS.find(h => String(h.id) === String(id)) || null;
  }

  /**
   * Render collections with optional filter and page.
   * @param {string} filtro - filtro por tipo (valor "all" para todas)
   * @param {number} page - página a mostrar (1-indexed)
   */
  function renderColecciones(filtro = "all", page = 1) {
    const container = document.getElementById("collections-container");
    const pagination = document.getElementById("pagination");
    if (!container) {
      console.warn("No se encontró #collections-container en el DOM.");
      return;
    }

    // limpieza
    container.innerHTML = "";
    if (pagination) pagination.innerHTML = "";

    // datos
    if (!window.coleccionesDB || !Array.isArray(window.coleccionesDB) || window.coleccionesDB.length === 0) {
      container.innerHTML = `<div class="empty">No hay colecciones para mostrar.</div>`;
      return;
    }

    // filtrado
    let filtradas = window.coleccionesDB.slice();
    if (filtro && filtro !== "all") {
      filtradas = filtradas.filter(col => String(col.tipo) === String(filtro));
    }

    const totalItems = filtradas.length;
    const totalPages = Math.max(1, Math.ceil(totalItems / PAGE_SIZE));

    // clamp page
    currentPage = Math.min(Math.max(1, parseInt(page, 10) || 1), totalPages);

    const startIndex = (currentPage - 1) * PAGE_SIZE;
    const pageItems = filtradas.slice(startIndex, startIndex + PAGE_SIZE);

    const isCollectionsPage = window.location.pathname.includes("collections.html");
    const isIndexPage = window.location.pathname.includes("index.html");

    // render cards
    if (pageItems.length === 0) {
      container.innerHTML = `<div class="empty">No hay colecciones para mostrar en esta página.</div>`;
    } else {
      pageItems.forEach(col => {
        const hechoId = (Array.isArray(col.hechos) && col.hechos.length) ? col.hechos[0] : null;
        const hecho = hechoId ? getHechoById(hechoId) : null;

        const card = document.createElement("div");
        card.className = "card collection-card";

        card.addEventListener("click", () => {
          window.location.href = `collection-detail.html?id=${encodeURIComponent(col.id)}`;
        });

        const titleHtml = `<div class="card-title">${escapeHtml(col.titulo || "Sin título")}</div>`;
        let cardContent = "";

        if (isIndexPage) {
          if (hecho && hecho.img && String(hecho.img).trim() !== "") {
            cardContent = `<img src="${escapeAttr(hecho.img)}" alt="${escapeAttr(hecho.title || "")}" class="card-img">${titleHtml}`;
          } else {
            const randomColor = fallbackColors[Math.floor(Math.random() * fallbackColors.length)];
            const letter = (hecho && hecho.title) ? hecho.title.charAt(0) : (col.titulo ? String(col.titulo).charAt(0) : "?");
            cardContent = `
              <div class="card-fallback" style="background:${randomColor}; display:flex; justify-content:center; align-items:center; height:150px;">
                <span style="color:#fff; font-weight:700; font-size:1.4rem;">${escapeHtml(letter)}</span>
              </div>
              ${titleHtml}
            `;
          }
        } else {
          const descHtml = `<div class="card-description"><p>${escapeHtml(truncate(col.descripcion || "", 140))}</p></div>`;
          const metaHtml = `<div class="card-meta">
              <p>Cantidad de hechos: ${Array.isArray(col.hechos) ? col.hechos.length : 0}</p>
              <p>Última actualización: ${escapeHtml(col.ultimaActualizacion || "—")}</p>
            </div>`;
          const actionsHtml = `
            <div class="card-actions">
              <button class="btn-action btn-edit">Editar</button>
              <button class="btn-action btn-delete">Eliminar</button>
            </div>
          `;

          if (hecho && hecho.img && String(hecho.img).trim() !== "") {
            cardContent = `<img src="${escapeAttr(hecho.img)}" alt="${escapeAttr(hecho.title || "")}" class="card-img">
              ${titleHtml}${descHtml}${metaHtml}${actionsHtml}`;
          } else {
            const randomColor = fallbackColors[Math.floor(Math.random() * fallbackColors.length)];
            const letter = (hecho && hecho.title) ? hecho.title.charAt(0) : (col.titulo ? String(col.titulo).charAt(0) : "?");
            cardContent = `
              <div class="card-fallback" style="background:${randomColor}; display:flex; justify-content:center; align-items:center; height:150px;">
                <span style="color:#fff; font-weight:700; font-size:1.4rem;">${escapeHtml(letter)}</span>
              </div>
              ${titleHtml}${descHtml}${metaHtml}${actionsHtml}`;
          }
        }

        card.innerHTML = cardContent;

        // botones editar/eliminar solo en la página collections.html (no en index)
        if (isCollectionsPage) {
          const btnEdit = card.querySelector(".btn-edit");
          const btnDelete = card.querySelector(".btn-delete");

          if (btnEdit) {
            btnEdit.addEventListener("click", (e) => {
              e.stopPropagation();
              window.location.href = `edit-collection.html?id=${col.id}`;
            });
          }

          if (btnDelete) {
            btnDelete.addEventListener("click", (e) => {
              e.stopPropagation();
              eliminarColeccion(col.id);
            });
          }
        }

        container.appendChild(card);
      });
    }

    // RENDER PAGINACIÓN: solo anterior / siguiente con indicador
    if (pagination && totalPages > 1) {
      // Contenedor para botones (flex)
      const wrapper = document.createElement("div");
      wrapper.className = "pagination-wrapper";
      wrapper.style.display = "inline-flex";
      wrapper.style.alignItems = "center";
      wrapper.style.gap = "0.6rem";

      // Botón Anterior (←)
      const prevBtn = document.createElement("button");
      prevBtn.className = "btn-pagination btn-prev";
      prevBtn.type = "button";
      prevBtn.innerHTML = "&#8592;"; // flecha izquierda
      prevBtn.setAttribute("aria-label", "Página anterior");
      prevBtn.disabled = currentPage === 1;
      prevBtn.addEventListener("click", () => {
        if (currentPage > 1) {
          renderColecciones(filtro, currentPage - 1);
          // scroll al top del container si querés:
          // container.scrollIntoView({ behavior: "smooth", block: "start" });
        }
      });
      wrapper.appendChild(prevBtn);

      // Indicador de página
      const info = document.createElement("span");
      info.className = "pagination-info";
      info.textContent = `Página ${currentPage} de ${totalPages}`;
      info.style.fontSize = "0.95rem";
      info.style.color = "#333";
      wrapper.appendChild(info);

      // Botón Siguiente (→)
      const nextBtn = document.createElement("button");
      nextBtn.className = "btn-pagination btn-next";
      nextBtn.type = "button";
      nextBtn.innerHTML = "&#8594;"; // flecha derecha
      nextBtn.setAttribute("aria-label", "Página siguiente");
      nextBtn.disabled = currentPage === totalPages;
      nextBtn.addEventListener("click", () => {
        if (currentPage < totalPages) {
          renderColecciones(filtro, currentPage + 1);
          // container.scrollIntoView({ behavior: "smooth", block: "start" });
        }
      });
      wrapper.appendChild(nextBtn);

      // Añadimos al contenedor de paginación
      pagination.appendChild(wrapper);

      // (Opcional) info de resultados a la derecha
      const resultsInfo = document.createElement("div");
      resultsInfo.className = "pagination-results";
      const start = startIndex + 1;
      const end = Math.min(totalItems, startIndex + PAGE_SIZE);
      resultsInfo.textContent = `Mostrando ${start}-${end} de ${totalItems}`;
      resultsInfo.style.marginLeft = "1rem";
      resultsInfo.style.color = "#666";
      resultsInfo.style.fontSize = "0.9rem";
      pagination.appendChild(resultsInfo);
    }
  }

  // Eliminar colección y preservar página actual (ajusta si la página queda vacía)
  function eliminarColeccion(id) {
    if (!confirm("¿Seguro que quieres eliminar esta colección?")) return;

    if (!Array.isArray(window.coleccionesDB)) return;

    window.coleccionesDB = window.coleccionesDB.filter(c => String(c.id) !== String(id));

    // Recalcular páginas según filtro actual si existe el select
    const filtroSelect = document.getElementById("filtro-colecciones");
    const filtro = filtroSelect ? (filtroSelect.value || "all") : "all";

    const filtradas = (filtro && filtro !== "all")
      ? window.coleccionesDB.filter(col => String(col.tipo) === String(filtro))
      : window.coleccionesDB.slice();

    const totalPages = Math.max(1, Math.ceil(filtradas.length / PAGE_SIZE));
    // si currentPage > totalPages, lo ajustamos
    if (currentPage > totalPages) currentPage = totalPages;

    renderColecciones(filtro, currentPage);
  }

  // Conectar el select de filtro existente (si está en el HTML)
  function addFiltroIfNeeded() {
    const filtroSelect = document.getElementById("filtro-colecciones");
    if (!filtroSelect) return;

    // Si ya tiene listener, no agregamos otro (evitar duplicados)
    if (!filtroSelect.dataset.bound) {
      filtroSelect.addEventListener("change", (e) => {
        const nuevoFiltro = e.target.value || "all";
        currentPage = 1;
        renderColecciones(nuevoFiltro, 1);
      });
      filtroSelect.dataset.bound = "true";
    }
  }

  function truncate(str, n) {
    if (typeof str !== "string") return "";
    return str.length > n ? str.slice(0, n - 1) + "…" : str;
  }

  function escapeHtml(unsafe) {
    if (unsafe === undefined || unsafe === null) return "";
    return String(unsafe)
      .replaceAll("&", "&amp;")
      .replaceAll("<", "&lt;")
      .replaceAll(">", "&gt;")
      .replaceAll('"', "&quot;")
      .replaceAll("'", "&#039;");
  }

  function escapeAttr(s) {
    return escapeHtml(s).replaceAll('"', "&quot;");
  }

  // Agregamos favicon siempre
  (function addFavicon() {
    const existing = document.querySelector('link[rel="icon"]');
    if (existing) existing.href = "icon.ico";
    else {
      const link = document.createElement("link");
      link.rel = "icon";
      link.href = "icon.ico";
      document.head.appendChild(link);
    }
  })();

  // Exponer funciones para debugging / uso externo
  window.renderColecciones = renderColecciones;
  window.eliminarColeccion = eliminarColeccion;

  // Inicialización al cargar DOM
  document.addEventListener("DOMContentLoaded", () => {
    addFiltroIfNeeded();

    // Si existe select, usamos su valor como filtro inicial
    const filtroSelect = document.getElementById("filtro-colecciones");
    const initialFilter = filtroSelect ? (filtroSelect.value || "all") : "all";

    renderColecciones(initialFilter, 1);
  });
})();
