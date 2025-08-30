document.addEventListener('DOMContentLoaded', () => {
  // --- Datos de ejemplo ---
  let hechos = JSON.parse(localStorage.getItem("hechos")) || [
    { id: 1, titulo: "Hecho 1", img: "images/hecho1.jpg", categoria: "Ambiental", ciudad: "Ciudad X", fecha: "27/08/2025", descripcion: "Descripción completa del hecho 1", lat: -34.6037, lng: -58.3816 },
    { id: 2, titulo: "Hecho 2", img: "images/hecho2.jpg", categoria: "Social", ciudad: "Ciudad Y", fecha: "25/08/2025", descripcion: "Descripción completa del hecho 2", lat: -34.6090, lng: -58.3800 },
    { id: 3, titulo: "Hecho 3", img: "images/hecho3.jpg", categoria: "Política", ciudad: "Ciudad Z", fecha: "23/08/2025", descripcion: "Descripción completa del hecho 3", lat: -34.6100, lng: -58.3900 }
  ];

  // --- MAPA ---
  const mapEl = document.getElementById('map');
  if (mapEl) {
    const map = L.map('map').setView([-34.6037, -58.3816], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '© OpenStreetMap contributors' }).addTo(map);
    hechos.forEach(h => L.marker([h.lat, h.lng]).addTo(map).bindPopup(`<strong>${h.titulo}</strong><br>${h.descripcion}`));
  }

  // --- EXPLORE CARDS ---
  const container = document.getElementById('hechos-container');
  if (container) {
    hechos.forEach(h => {
      const card = document.createElement('div');
      card.className = 'card';

      // Enlace a hecho.html con el ID
      card.innerHTML = `
        <a href="hecho.html?id=${h.id}">
          <img src="${h.img}" alt="Imagen ${h.titulo}">
          <div class="card-content">
            <h2>${h.titulo}</h2>
            <p class="meta">${h.categoria} | ${h.ciudad} | ${h.fecha}</p>
            <p class="description">${h.descripcion.substring(0,50)}...</p>
          </div>
        </a>
      `;
      container.appendChild(card);
    });
  }
});
