// explore.js (versión con paginación)
// -------------------------------

const colors = [
  "#2F60F4","#1746D2","#1A3FA0","#3B6EFF","#4A79FF","#5A5AFF","#6A6AFF"
];

function getRandomColor() {
  return colors[Math.floor(Math.random() * colors.length)];
}

function createCard(hecho) {
  const card = document.createElement("a");
  card.classList.add("card");
  card.href = hecho.id ? `hecho.html?id=${hecho.id}` : "#";
  card.setAttribute("aria-label", hecho.title || "Hecho");

  // 1️⃣ Título
  const title = document.createElement("div");
  title.classList.add("card-title");
  title.textContent = hecho.title || "Sin título";
  card.appendChild(title);

  // 2️⃣ Descripción (resumen)
  const description = document.createElement("div");
  description.classList.add("card-description");
  const maxLength = 300;
  if (hecho.description) {
    const txt = hecho.description.length > maxLength
      ? hecho.description.slice(0, maxLength) + "..."
      : hecho.description;
    // Usamos <p> para mantener el estilo que ya tenés
    const p = document.createElement("p");
    p.textContent = txt;
    description.appendChild(p);
  } else {
    const p = document.createElement("p");
    p.textContent = "Sin descripción";
    description.appendChild(p);
  }
  card.appendChild(description);

  // 3️⃣ Datos extra (emoji + estilo ligero)
  const data = document.createElement("div");
  data.classList.add("card-data");
  if (hecho.ubicacion) data.innerHTML += `<p><strong>📍</strong> ${escapeHtml(hecho.ubicacion)}</p>`;
  if (hecho.categoria) data.innerHTML += `<p><strong>🏷️</strong> ${escapeHtml(hecho.categoria)}</p>`;
  if (hecho.etiqueta) data.innerHTML += `<p><strong>🔖</strong> ${escapeHtml(hecho.etiqueta)}</p>`;
  if (hecho.fecha) data.innerHTML += `<p><strong>📅</strong> ${escapeHtml(hecho.fecha)}</p>`;
  if (hecho.fuente) data.innerHTML += `<p><strong>🌐</strong> ${escapeHtml(hecho.fuente)}</p>`;
  card.appendChild(data);

  // 4️⃣ Imagen o color de fondo
  const imgWrapper = document.createElement("div");
  imgWrapper.classList.add("card-img"); // coincide con tu CSS
  imgWrapper.setAttribute("aria-hidden", "true");

  if (!hecho.img || hecho.img === "nil" || String(hecho.img).trim() === "") {
    imgWrapper.style.backgroundColor = getRandomColor();
    // opcional: mostrar inicial si querés (comenta si no)
    const inicial = document.createElement("div");
    inicial.style.padding = "18px";
    inicial.style.fontWeight = "700";
    inicial.style.fontSize = "1.1rem";
    inicial.textContent = hecho.title ? String(hecho.title).charAt(0).toUpperCase() : "?";
    inicial.style.color = "#fff";
    imgWrapper.appendChild(inicial);
  } else {
    const img = document.createElement("img");
    img.src = hecho.img;
    img.alt = hecho.title || "imagen";
    img.onerror = () => {
      img.remove();
      imgWrapper.style.backgroundColor = getRandomColor();
    };
    imgWrapper.appendChild(img);
  }
  card.appendChild(imgWrapper);

  return card;
}

// ---------------- Pagination logic ----------------
const PAGE_SIZE = 6; // <-- cambia esto para mostrar más/menos hechos por página
let currentPage = 1;
let filteredHechos = Array.isArray(window.HECHOS) ? window.HECHOS.slice() : [];

function escapeHtml(s) {
  if (s === null || s === undefined) return "";
  return String(s)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function getTotalPages() {
  return Math.max(1, Math.ceil(filteredHechos.length / PAGE_SIZE));
}

function renderPage(page = 1) {
  const container = document.getElementById("hechos-container");
  if (!container) return;

  // clamp page
  const total = getTotalPages();
  if (page < 1) page = 1;
  if (page > total) page = total;
  currentPage = page;

  // slice según paginado
  const start = (currentPage - 1) * PAGE_SIZE;
  const end = start + PAGE_SIZE;
  const pageItems = filteredHechos.slice(start, end);

  container.innerHTML = ""; // limpiamos

  if (pageItems.length === 0) {
    container.innerHTML = `<p>No se encontraron resultados con los filtros seleccionados.</p>`;
  } else {
    const cardsRow = document.createElement("div");
    cardsRow.classList.add("cards-row-explore"); // clase local para layout si querés
    // añadimos cada card
    pageItems.forEach(h => {
      const card = createCard(h);
      cardsRow.appendChild(card);
    });
    container.appendChild(cardsRow);
  }

  renderPaginationControls(container);
  // scroll suave al contenedor (útil al cambiar de página)
  container.scrollIntoView({ behavior: "smooth", block: "start" });
}

function renderPaginationControls(container) {
  // eliminamos paginador antiguo si existe
  const existing = document.getElementById("pagination-controls");
  if (existing) existing.remove();

  const pagination = document.createElement("div");
  pagination.id = "pagination-controls";
  pagination.style.display = "flex";
  pagination.style.justifyContent = "center";
  pagination.style.alignItems = "center";
  pagination.style.gap = "12px";
  pagination.style.marginTop = "18px";

  const total = getTotalPages();

  // Botón Anterior (solo flecha compacta)
  const prevBtn = document.createElement("button");
  prevBtn.className = "pagination-arrow";
  prevBtn.type = "button";
  prevBtn.innerHTML = "◀";
  prevBtn.title = "Anterior";
  prevBtn.disabled = currentPage <= 1;
  prevBtn.setAttribute("aria-label", "Anterior");
  prevBtn.addEventListener("click", () => renderPage(currentPage - 1));
  pagination.appendChild(prevBtn);

  // Indicador de páginas (texto)
  const pageIndicator = document.createElement("div");
  pageIndicator.setAttribute("aria-live", "polite");
  pageIndicator.textContent = `Página ${currentPage} de ${total}`;
  pageIndicator.style.fontWeight = "600";
  pageIndicator.style.padding = "0 8px";
  pagination.appendChild(pageIndicator);

  // Botón Siguiente (solo flecha compacta)
  const nextBtn = document.createElement("button");
  nextBtn.className = "pagination-arrow";
  nextBtn.type = "button";
  nextBtn.innerHTML = "▶";
  nextBtn.title = "Siguiente";
  nextBtn.disabled = currentPage >= total;
  nextBtn.setAttribute("aria-label", "Siguiente");
  nextBtn.addEventListener("click", () => renderPage(currentPage + 1));
  pagination.appendChild(nextBtn);

  // Insertamos después del contenedor (o al final del mismo)
  container.appendChild(pagination);
}


// ---------------- Filters ----------------
function applyFilters() {
  const fecha = document.getElementById("filter-fecha")?.value.trim();
  const ubicacion = document.getElementById("filter-ubicacion")?.value.trim().toLowerCase();
  const categoria = document.getElementById("filter-categoria")?.value.trim().toLowerCase();
  const etiqueta = document.getElementById("filter-etiqueta")?.value.trim().toLowerCase();
  const fuente = document.getElementById("filter-fuente")?.value.trim().toLowerCase();
  const modo = document.getElementById("filter-modo")?.value; // por si lo querés usar

  // aplicamos filtros sobre la lista original HECHOS
  filteredHechos = (Array.isArray(window.HECHOS) ? window.HECHOS.slice() : []).filter(h => {
    let match = true;
    if (fecha && h.fecha) match = match && h.fecha.includes(fecha);
    if (ubicacion && h.ubicacion) match = match && h.ubicacion.toLowerCase().includes(ubicacion);
    if (categoria && h.categoria) match = match && h.categoria.toLowerCase().includes(categoria);
    if (etiqueta && h.etiqueta) match = match && h.etiqueta.toLowerCase().includes(etiqueta);
    if (fuente && h.fuente) match = match && h.fuente.toLowerCase().includes(fuente);
    return match;
  });

  // si querés, podés aplicar lógica de 'modo' aquí (curados vs irrestricto)
  console.log("Modo seleccionado:", modo);

  // reset página y render
  renderPage(1);
}

// ---------------- Init ----------------
document.addEventListener("DOMContentLoaded", () => {
  // si no hay HECHOS definidos, no hacemos nada
  if (!Array.isArray(window.HECHOS)) {
    console.warn("HECHOS no está definido o no es un array.");
    return;
  }

  // por defecto, sin filtros
  filteredHechos = window.HECHOS.slice();
  renderPage(1);

  // Botón de aplicar filtros
  const filterBtn = document.getElementById("btn-apply-filters");
  if (filterBtn) {
    filterBtn.addEventListener("click", applyFilters);
  }

  // Soporte: aplicar filtros al presionar Enter en inputs (opcional)
  ["filter-fecha","filter-ubicacion","filter-categoria","filter-etiqueta","filter-fuente"].forEach(id => {
    const el = document.getElementById(id);
    if (!el) return;
    el.addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        e.preventDefault();
        applyFilters();
      }
    });
  });
});
