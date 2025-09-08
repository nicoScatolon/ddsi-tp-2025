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

  // 1️⃣ Título
  const title = document.createElement("div");
  title.classList.add("card-title");
  title.textContent = hecho.title || "Sin título";
  card.appendChild(title);

  // 2️⃣ Descripción
  const description = document.createElement("div");
  description.classList.add("card-description");
  const maxLength = 300;
  if (hecho.description) {
    description.textContent = hecho.description.length > maxLength 
      ? hecho.description.slice(0, maxLength) + "..."
      : hecho.description;
  } else {
    description.textContent = "Sin descripción";
  }
  card.appendChild(description);

  // 3️⃣ Datos extra
  const data = document.createElement("div");
  data.classList.add("card-data");
  if (hecho.ubicacion) data.innerHTML += `<p><strong>📍 Ubicación:</strong> ${hecho.ubicacion}</p>`;
  if (hecho.categoria) data.innerHTML += `<p><strong>🏷️ Categoría:</strong> ${hecho.categoria}</p>`;
  if (hecho.etiqueta) data.innerHTML += `<p><strong>🔖 Etiqueta:</strong> ${hecho.etiqueta}</p>`;
  if (hecho.fecha) data.innerHTML += `<p><strong>📅 Fecha:</strong> ${hecho.fecha}</p>`;
  if (hecho.fuente) data.innerHTML += `<p><strong>🌐 Fuente:</strong> ${hecho.fuente}</p>`;
  card.appendChild(data);

  // 4️⃣ Imagen o color
  const imgWrapper = document.createElement("div");
  imgWrapper.classList.add("card-img");
  if (!hecho.img || hecho.img === "nil") {
    imgWrapper.style.backgroundColor = getRandomColor();
  } else {
    const img = document.createElement("img");
    img.src = hecho.img;
    img.onerror = () => {
      img.remove();
      imgWrapper.style.backgroundColor = getRandomColor();
    };
    imgWrapper.appendChild(img);
  }
  card.appendChild(imgWrapper);

  return card;
}

function renderCards(hechosArray) {
  const container = document.getElementById("hechos-container");
  if (!container) return;
  container.innerHTML = "";
  if (hechosArray.length === 0) {
    container.innerHTML = `<p>No se encontraron resultados con los filtros seleccionados.</p>`;
    return;
  }
  hechosArray.forEach(hecho => {
    const card = createCard(hecho);
    container.appendChild(card);
  });
}

function applyFilters() {
  const fecha = document.getElementById("filter-fecha")?.value.trim();
  const ubicacion = document.getElementById("filter-ubicacion")?.value.trim().toLowerCase();
  const categoria = document.getElementById("filter-categoria")?.value.trim().toLowerCase();
  const etiqueta = document.getElementById("filter-etiqueta")?.value.trim().toLowerCase();
  const fuente = document.getElementById("filter-fuente")?.value.trim().toLowerCase();
  const modo = document.getElementById("filter-modo")?.value; // 👈 nuevo

  let filtrados = HECHOS.filter(h => {
    let match = true;
    if (fecha && h.fecha) match = match && h.fecha.includes(fecha);
    if (ubicacion && h.ubicacion) match = match && h.ubicacion.toLowerCase().includes(ubicacion);
    if (categoria && h.categoria) match = match && h.categoria.toLowerCase().includes(categoria);
    if (etiqueta && h.etiqueta) match = match && h.etiqueta.toLowerCase().includes(etiqueta);
    if (fuente && h.fuente) match = match && h.fuente.toLowerCase().includes(fuente);
    return match;
  });

  // 👇 aquí podrías aplicar la lógica del modo (por ahora solo lo dejo loggeado)
  console.log("Modo seleccionado:", modo);

  renderCards(filtrados);
}

document.addEventListener("DOMContentLoaded", () => {
  renderCards(HECHOS);

  // Botón de aplicar filtros
  const filterBtn = document.getElementById("btn-apply-filters");
  if (filterBtn) {
    filterBtn.addEventListener("click", applyFilters);
  }
});
