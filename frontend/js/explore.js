// explore.js

// Paleta de colores de la página
const colors = ["#2F60F4", "#1746D2", "#444", "#555", "#666"];

// Función para obtener un color aleatorio de la paleta
function getRandomColor() {
  return colors[Math.floor(Math.random() * colors.length)];
}

// Función para crear una card
function createCard(hecho) {
  const card = document.createElement("a");
  card.classList.add("card");

  // ✅ Agregar href basado en el ID del hecho
  if (hecho.id) {
    card.href = `hecho.html?id=${hecho.id}`;
  } else {
    card.href = "#"; // fallback si no hay id
  }

  // Contenedor de texto
  const content = document.createElement("div");
  content.classList.add("card-content");

  const title = document.createElement("h2");
  title.textContent = hecho.title || "Sin título";
  content.appendChild(title);

  const description = document.createElement("p");
  description.textContent = hecho.description || "Sin descripción disponible";
  content.appendChild(description);

  card.appendChild(content);

  // Imagen o fondo aleatorio
  const img = document.createElement("img");
  if (hecho.img && hecho.img !== "") {
    img.src = hecho.img;
  } else {
    // Fondo aleatorio si no hay imagen
    img.style.backgroundColor = getRandomColor();
    img.style.width = "220px";   // mantener tamaño
    img.style.height = "160px";
    img.style.display = "block";
  }

  card.appendChild(img);

  return card;
}

// Función para renderizar todas las cards
function renderCards(hechos) {
  const container = document.getElementById("hechos-container");
  container.innerHTML = ""; // limpiar container

  hechos.forEach(hecho => {
    const card = createCard(hecho);
    container.appendChild(card);
  });
}

// Ejemplo de uso: lista de hechos
const hechos = [
    { id: 1, title: "Hecho 1", description: "Descripción del hecho 1", img: "" },
    { id: 2, title: "Hecho 2", description: "Descripción del hecho 2", img: "images/hecho2.jpg" },
    { id: 3, title: "Hecho 3", description: "Descripción del hecho 3", img: "" },
    { id: 4, title: "Hecho 4", description: "Descripción del hecho 4", img: "" },
];

// Renderizamos las cards al cargar la página
document.addEventListener("DOMContentLoaded", () => {
  renderCards(hechos);
});
