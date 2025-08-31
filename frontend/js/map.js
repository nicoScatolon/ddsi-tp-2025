document.addEventListener('DOMContentLoaded', () => {
  const hechos = HECHOS;

  const map = L.map('map').setView([-34.6037, -58.3816], 13);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map);

  // Agregar marcadores con popup
  hechos.forEach(h => {
    const popupContent = `
      <div style="max-width: 200px;">
        <strong>${h.title}</strong><br>
        <p>${h.description.slice(0, 100)}...</p>
        <a href="hecho.html?id=${h.id}" class="btn-popup">Ver más</a>
      </div>
    `;

    L.marker([h.lat, h.lng])
      .addTo(map)
      .bindPopup(popupContent);
  });
});
