// js/collection-detail.js
(function () {
  // -------------------- UTILIDADES --------------------
  function safeText(s) { return (s === null || s === undefined) ? '' : String(s); }
  const colors = ["#2F60F4","#1746D2","#1A3FA0","#3B6EFF","#4A79FF","#5A5AFF","#6A6AFF"];
  function getRandomColor() { return colors[Math.floor(Math.random() * colors.length)]; }

  // lee campo probando múltiples claves
  function pickField(obj, keys) {
    for (const k of keys) {
      if (obj && Object.prototype.hasOwnProperty.call(obj, k) && obj[k] !== undefined && obj[k] !== null) {
        return obj[k];
      }
    }
    return undefined;
  }

  function getHechoById(id) {
    if (!window.HECHOS || !Array.isArray(window.HECHOS)) return null;
    return window.HECHOS.find(h => String(h.id) === String(id)) || null;
  }

  // -------------------- CREAR CARD --------------------
  function createCard(h) {
    const a = document.createElement("a");
    a.className = "card";
    a.href = h.id ? `hecho.html?id=${encodeURIComponent(h.id)}` : "#";
    a.setAttribute("role", "link");

    // Title
    const titleDiv = document.createElement("div");
    titleDiv.className = "card-title";
    const titleVal = pickField(h, ["title", "titulo"]) || `Hecho ${safeText(h.id)}` || "Sin título";
    titleDiv.textContent = safeText(titleVal);
    a.appendChild(titleDiv);

    // Description
    const descDiv = document.createElement("div");
    descDiv.className = "card-description";
    const p = document.createElement("p");
    const descVal = pickField(h, ["description", "descripcion"]) || "";
    p.textContent = descVal ? (descVal.length > 300 ? descVal.slice(0, 300) + "..." : descVal) : "Sin descripción";
    descDiv.appendChild(p);
    a.appendChild(descDiv);

    // Data column
    const dataDiv = document.createElement("div");
    dataDiv.className = "card-data";
    const ubic = pickField(h, ["ubicacion", "location", "place"]);
    const categoria = pickField(h, ["categoria", "category"]);
    const etiqueta = pickField(h, ["etiqueta", "tag", "tags"]);
    const fecha = pickField(h, ["fecha", "date"]);
    const fuente = pickField(h, ["fuente", "source"]);
    if (ubic) dataDiv.innerHTML += `<p><strong>📍 Ubicación:</strong> ${safeText(ubic)}</p>`;
    if (categoria) dataDiv.innerHTML += `<p><strong>🏷️ Categoría:</strong> ${safeText(categoria)}</p>`;
    if (etiqueta) dataDiv.innerHTML += `<p><strong>🔖 Etiqueta:</strong> ${safeText(etiqueta)}</p>`;
    if (fecha) dataDiv.innerHTML += `<p><strong>📅 Fecha:</strong> ${safeText(fecha)}</p>`;
    if (fuente) dataDiv.innerHTML += `<p><strong>🌐 Fuente:</strong> ${safeText(fuente)}</p>`;
    if (!ubic && !categoria && !etiqueta && !fecha && !fuente) dataDiv.innerHTML = `<p>—</p>`;
    a.appendChild(dataDiv);

    // Image column
    const imgDiv = document.createElement("div");
    imgDiv.className = "card-img";
    const imgSrc = pickField(h, ["img", "image", "foto"]) || "";
    if (imgSrc && String(imgSrc).trim() !== "") {
      const img = document.createElement("img");
      img.src = imgSrc;
      img.alt = safeText(titleVal);
      img.onerror = () => {
        img.remove();
        setFallbackInitial(imgDiv, titleVal);
      };
      imgDiv.appendChild(img);
    } else {
      setFallbackInitial(imgDiv, titleVal);
    }
    a.appendChild(imgDiv);

    return a;
  }

  function setFallbackInitial(container, titleVal) {
    const initial = safeText(titleVal).trim().charAt(0).toUpperCase() || "?";
    container.style.backgroundColor = getRandomColor();
    container.style.color = "#fff";
    container.style.display = "flex";
    container.style.justifyContent = "center";
    container.style.alignItems = "center";
    container.style.fontWeight = "700";
    container.style.fontSize = "1.4rem";
    container.textContent = initial;
  }

  // -------------------- RENDER CARDS --------------------
  function renderCards(hechosArray) {
    const container = document.getElementById("hechos-container");
    if (!container) {
      console.warn("No existe #hechos-container en el DOM");
      return;
    }
    container.innerHTML = "";
    if (!Array.isArray(hechosArray) || hechosArray.length === 0) {
      container.innerHTML = "<p>No se encontraron hechos en esta colección.</p>";
      return;
    }
    hechosArray.forEach(h => container.appendChild(createCard(h)));
  }

  // -------------------- ENRIQUECER HECHO --------------------
  function enrichHecho(minimal) {
    // minimal es simplemente un ID (número/string) o un objeto { id, img? }
    const id = (typeof minimal === "object" && minimal !== null) ? pickField(minimal, ["id"]) : minimal;
    if (id === undefined || id === null) return { id: null, img: pickField(minimal, ["img", "image", "foto"]) || "" };

    const found = getHechoById(id);
    if (found) {
      // DEV: retornamos el objeto completo del HECHOS, pero permitimos que el minimal (colección)
      // nos proporcione una imagen override: preferimos minimal.img si existe.
      return Object.assign({}, found, {
        img: pickField(minimal, ["img", "image", "foto"]) || pickField(found, ["img", "image", "foto"]) || ""
      });
    }

    // no existe en HECHOS global -> devolvemos solo id + img si vino en minimal
    return {
      id,
      img: pickField(minimal, ["img", "image", "foto"]) || ""
    };
  }

  // -------------------- FILTRADO --------------------
  function applyFiltersOnArray(baseArray) {
    const modo = document.getElementById("filter-modo")?.value;
    const fecha = document.getElementById("filter-fecha")?.value.trim().toLowerCase();
    const ubicacion = document.getElementById("filter-ubicacion")?.value.trim().toLowerCase();
    const categoria = document.getElementById("filter-categoria")?.value.trim().toLowerCase();
    const etiqueta = document.getElementById("filter-etiqueta")?.value.trim().toLowerCase();
    const fuente = document.getElementById("filter-fuente")?.value.trim().toLowerCase();

    const filtered = (baseArray || []).filter(h => {
      let ok = true;
      if (modo && modo !== "irrestricto" && pickField(h, ["modo", "mode"])) {
        ok = ok && (pickField(h, ["modo", "mode"]) === modo);
      }
      if (fecha && !(pickField(h, ["fecha", "date"]) && String(pickField(h, ["fecha", "date"])).toLowerCase().includes(fecha))) ok = false;
      if (ubicacion && !(pickField(h, ["ubicacion", "location"]) && pickField(h, ["ubicacion", "location"]).toLowerCase().includes(ubicacion))) ok = false;
      if (categoria && !(pickField(h, ["categoria", "category"]) && pickField(h, ["categoria", "category"]).toLowerCase().includes(categoria))) ok = false;
      if (etiqueta && !(pickField(h, ["etiqueta", "tag", "tags"]) && pickField(h, ["etiqueta", "tag", "tags"]).toLowerCase().includes(etiqueta))) ok = false;
      if (fuente && !(pickField(h, ["fuente", "source"]) && pickField(h, ["fuente", "source"]).toLowerCase().includes(fuente))) ok = false;
      return ok;
    });

    renderCards(filtered);
  }

  // -------------------- FAVICON (FORZADO) --------------------
  function setFavicon() {
    // elimina favicons existentes
    document.querySelectorAll("link[rel~='icon']").forEach(el => el.remove());
    // fuerza el nuevo favicon
    const favicon = document.createElement("link");
    favicon.rel = "icon";
    favicon.href = "images/icon.ico"; // tu favicon fijo
    document.head.appendChild(favicon);
  }

  // -------------------- INIT --------------------
  document.addEventListener("DOMContentLoaded", () => {
    if (!window.coleccionesDB || !Array.isArray(window.coleccionesDB)) {
      console.error("coleccionesDB no está definido o no es un array. Revisa que collections-db.js esté cargado antes.");
      return;
    }

    const params = new URLSearchParams(window.location.search);
    const coleccionId = params.get("id");
    const coleccion = coleccionesDB.find(c => String(c.id) === String(coleccionId)) || coleccionesDB[0];

    if (!coleccion) {
      console.error("No se encontró la colección solicitada.");
      return;
    }

    // título/desc en DOM y title de pestaña
    const titleEl = document.getElementById("collection-title");
    const descEl = document.getElementById("collection-description");
    if (titleEl) titleEl.textContent = pickField(coleccion, ["titulo", "title"]) || "Colección";
    if (descEl)  descEl.textContent  = pickField(coleccion, ["descripcion", "description"]) || "";
    if (pickField(coleccion, ["titulo", "title"])) document.title = pickField(coleccion, ["titulo", "title"]);

    // forzar favicon a icon.ico (sin preguntar)
    setFavicon();

    // construir array enriquecido: la colección tiene IDs (o objetos con id+img)
    const minimalHechos = Array.isArray(coleccion.hechos) ? coleccion.hechos : [];
    const enriched = minimalHechos.map(h => enrichHecho(h));

    // debug
    console.groupCollapsed(`Colección "${pickField(coleccion, ["titulo","title"]) || coleccion.id}" — debug`);
    console.log("coleccion (raw):", coleccion);
    console.log("HECHOS global:", window.HECHOS || "NO HECHOS global definido");
    console.log("minimalHechos:", minimalHechos);
    console.log("enrichedHechos (final que se renderiza):", enriched);
    console.groupEnd();

    // render inicial
    renderCards(enriched);

    // conectar filtros
    const btn = document.getElementById("btn-apply-filters");
    if (btn) btn.addEventListener("click", () => applyFiltersOnArray(enriched));
  });
})();
