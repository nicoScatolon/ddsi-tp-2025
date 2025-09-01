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

  // Limitar a, por ejemplo, 150 caracteres
  const maxLength = 300;
  if (hecho.description) {
    description.textContent = hecho.description.length > maxLength 
      ? hecho.description.slice(0, maxLength) + "..."
      : hecho.description;
  } else {
    description.textContent = "Sin descripción";
  }

  card.appendChild(description);

  // 3️⃣ Datos
  const data = document.createElement("div");
  data.classList.add("card-data");
  if (hecho.ubicacion) data.innerHTML += `<p><strong>📍 Ubicación:</strong> ${hecho.ubicacion}</p>`;
  if (hecho.categoria) data.innerHTML += `<p><strong>🏷️ Categoría:</strong> ${hecho.categoria}</p>`;
  if (hecho.etiqueta) data.innerHTML += `<p><strong>🔖 Etiqueta:</strong> ${hecho.etiqueta}</p>`;
  if (hecho.fecha) data.innerHTML += `<p><strong>📅 Fecha:</strong> ${hecho.fecha}</p>`;
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
  hechosArray.forEach(hecho => {
    const card = createCard(hecho);
    container.appendChild(card);
  });
}

document.addEventListener("DOMContentLoaded", () => {
  // 🔹 Usar HECHOS definido en hechos.js
  renderCards(HECHOS);
});
