// js/collections.js
(function () {
  const fallbackColors = [
    "#2F60F4", "#1746D2", "#FF6B6B", "#FFA94D",
    "#51CF66", "#845EF7", "#20C997", "#FF8787"
  ];

  function getHechoById(id) {
    if (!window.HECHOS || !Array.isArray(window.HECHOS)) return null;
    return window.HECHOS.find(h => String(h.id) === String(id)) || null;
  }

  function renderColecciones(filtro = "all") {
    const container = document.getElementById("collections-container");
    if (!container) {
      console.warn("No se encontró #collections-container en el DOM.");
      return;
    }

    container.innerHTML = "";

    if (!window.coleccionesDB || !Array.isArray(window.coleccionesDB) || window.coleccionesDB.length === 0) {
      container.innerHTML = `<div class="empty">No hay colecciones para mostrar.</div>`;
      return;
    }

    let filtradas = window.coleccionesDB.slice();
    if (filtro !== "all") {
      filtradas = filtradas.filter(col => String(col.tipo) === String(filtro));
    }

    const isCollectionsPage = window.location.pathname.includes("collections.html");
    const isIndexPage = window.location.pathname.includes("index.html");

    filtradas.forEach(col => {
      const hechoId = (Array.isArray(col.hechos) && col.hechos.length) ? col.hechos[0] : null;
      const hecho = hechoId ? getHechoById(hechoId) : null;

      const card = document.createElement("div");
      card.className = "card collection-card";

      // Click en la card abre detalle
      card.addEventListener("click", () => {
        window.location.href = `collection-detail.html?id=${encodeURIComponent(col.id)}`;
      });

      const titleHtml = `<div class="card-title">${escapeHtml(col.titulo || "Sin título")}</div>`;

      let cardContent = "";

      // En index.html solo título e imagen/fallback
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
        // En collections.html mostramos todo
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

      // Botones solo en collections.html
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

  // Simulación de eliminar
  function eliminarColeccion(id) {
    if (!confirm("¿Seguro que quieres eliminar esta colección?")) return;

    if (Array.isArray(window.coleccionesDB)) {
      window.coleccionesDB = window.coleccionesDB.filter(c => String(c.id) !== String(id));
      renderColecciones("all");
    }
  }

  function addFiltroIfNeeded() {
    const onCollectionsPage = window.location.pathname.includes("collections.html");
    if (!onCollectionsPage) return; // solo lo agregamos en esa página

    const section = document.getElementById("colecciones");
    if (!section || document.getElementById("filtro-colecciones")) return;

    const controls = document.createElement("div");
    controls.id = "filtro-colecciones-container";
    controls.style.margin = "8px 0";

    const select = document.createElement("select");
    select.id = "filtro-colecciones";
    select.innerHTML = `
      <option value="all">Todas</option>
      <option value="Mayoria Simple">Mayoría Simple</option>
      <option value="OtroTipo">Otro Tipo</option>
    `;
    controls.appendChild(select);

    const scrollContainer = section.querySelector(".scroll-container");
    if (scrollContainer) section.insertBefore(controls, scrollContainer);
    else section.appendChild(controls);

    select.addEventListener("change", (e) => {
      renderColecciones(e.target.value);
    });
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

  window.renderColecciones = renderColecciones;
  window.eliminarColeccion = eliminarColeccion;

  document.addEventListener("DOMContentLoaded", () => {
    addFiltroIfNeeded(); // solo en collections.html
    renderColecciones("all"); // index.html y collections.html
  });
})();
