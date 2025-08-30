document.addEventListener('DOMContentLoaded', () => {
  // --- Datos de ejemplo para todos los archivos ---
    let hechos = JSON.parse(localStorage.getItem("hechos")) || [
    { titulo: "Hecho 1", img: "images/hecho1.jpg", categoria: "Ambiental", ciudad: "Ciudad X", fecha: "27/08/2025", descripcion: "Descripción completa del hecho 1", lat: -34.6037, lng: -58.3816 },
    { titulo: "Hecho 2", img: "images/hecho2.jpg", categoria: "Social", ciudad: "Ciudad Y", fecha: "25/08/2025", descripcion: "Descripción completa del hecho 2", lat: -34.6090, lng: -58.3800 },
    { titulo: "Hecho 3", img: "images/hecho3.jpg", categoria: "Política", ciudad: "Ciudad Z", fecha: "23/08/2025", descripcion: "Descripción completa del hecho 3", lat: -34.6100, lng: -58.3900 }
  ];


  // --- MAPA ---
  const mapEl = document.getElementById('map');
  if (mapEl) {
    const map = L.map('map').setView([-34.6037, -58.3816], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '© OpenStreetMap contributors' }).addTo(map);
    hechos.forEach(h => L.marker([h.lat, h.lng]).addTo(map).bindPopup(`<strong>${h.titulo}</strong><br>${h.descripcion}`));
  }

  // --- FORMULARIO CATEGORÍA "OTRA" ---
  const selectCategoria = document.getElementById('categoria');
  const inputNuevaCategoria = document.getElementById('categoriaNueva');
  if (selectCategoria && inputNuevaCategoria) {
    inputNuevaCategoria.style.display = 'none';
    selectCategoria.addEventListener('change', () => {
      if (selectCategoria.value === 'other') {
        inputNuevaCategoria.style.display = 'block';
        inputNuevaCategoria.required = true;
      } else {
        inputNuevaCategoria.style.display = 'none';
        inputNuevaCategoria.required = false;
      }
    });
  }

  // --- FORMULARIO SUBMIT ---
  const formHecho = document.getElementById('hechoForm');
  if (formHecho) {
    formHecho.addEventListener('submit', (e) => {
      e.preventDefault();
      const formData = new FormData(formHecho);
      console.log('Formulario enviado:', Object.fromEntries(formData.entries()));
      alert('Formulario enviado (ver consola)');
    });
  }

  // --- EXPLORE CARDS ---
  const container = document.getElementById('hechos-container');
  if (container) {
    hechos.forEach(h => {
      const card = document.createElement('div');
      card.className = 'card';
      card.onclick = () => openModal(h.titulo, h.img, h.categoria, h.ciudad, h.fecha, h.descripcion);

      card.innerHTML = `
        <img src="${h.img}" alt="Imagen ${h.titulo}">
        <div class="card-content">
          <h2>${h.titulo}</h2>
          <p class="meta">${h.categoria} | ${h.ciudad} | ${h.fecha}</p>
          <p class="description">${h.descripcion.substring(0,50)}...</p>
        </div>
      `;
      container.appendChild(card);
    });
  }
});

// --- MODAL EXPLORE ---
function openModal(title, img, category, location, date, description) {
  document.getElementById('modal-title').innerText = title;
  document.getElementById('modal-img').src = img;
  document.getElementById('modal-meta').innerText = category + " | " + location + " | " + date;
  document.getElementById('modal-description').innerText = description;
  document.getElementById('modal').style.display = 'flex';
}
function closeModal() {
  document.getElementById('modal').style.display = 'none';
}
window.onclick = function(event) {
  if (event.target == document.getElementById('modal')) closeModal();
}
