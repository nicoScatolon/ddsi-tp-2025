// collections.js
const coleccionesDB = [
  { id: 1, titulo: "Colección Historia", descripcion: "Hechos históricos importantes." },
  { id: 2, titulo: "Colección Cultura", descripcion: "Eventos culturales destacados." },
  { id: 3, titulo: "Colección Eventos", descripcion: "Registro de eventos recientes." }
];

function renderColecciones() {
  const container = document.getElementById("collections-container");
  container.innerHTML = "";

  coleccionesDB.forEach(col => {
    const card = document.createElement("a");
    card.className = "card";
    card.href = `collection-detail.html?id=${col.id}`;

    card.innerHTML = `
      <div class="card-title">${col.titulo}</div>
      <div class="card-description"><p>${col.descripcion}</p></div>
    `;
    container.appendChild(card);
  });
}

document.addEventListener("DOMContentLoaded", renderColecciones);
