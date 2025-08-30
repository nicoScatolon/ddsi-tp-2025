document.addEventListener('DOMContentLoaded', () => {
  let hechos = JSON.parse(localStorage.getItem("hechos"));

  if (!hechos || hechos.length === 0) {
    hechos = [
      { id: 1, titulo: "Hecho 1", img: "images/hecho1.jpg", categoria: "Ambiental", ciudad: "Ciudad X", fecha: "27/08/2025", descripcion: "Descripción completa del hecho 1" },
      { id: 2, titulo: "Hecho 2", img: "images/hecho2.jpg", categoria: "Social", ciudad: "Ciudad Y", fecha: "25/08/2025", descripcion: "Descripción completa del hecho 2" },
      { id: 3, titulo: "Hecho 3", img: "images/hecho3.jpg", categoria: "Política", ciudad: "Ciudad Z", fecha: "23/08/2025", descripcion: "Descripción completa del hecho 3" }
    ];
    localStorage.setItem("hechos", JSON.stringify(hechos));
  }

  // Obtener ID de la URL
  const urlParams = new URLSearchParams(window.location.search);
  const id = parseInt(urlParams.get('id'));

  const hecho = hechos.find(h => h.id === id);

  if (hecho) {
    document.getElementById('hecho-title').textContent = hecho.titulo;
    document.getElementById('hecho-img').src = hecho.img;
    document.getElementById('hecho-img').alt = hecho.titulo;
    document.getElementById('hecho-meta').textContent = `${hecho.categoria} | ${hecho.ciudad} | ${hecho.fecha}`;
    document.getElementById('hecho-description').textContent = hecho.descripcion;
  } else {
    document.getElementById('hecho-detail').innerHTML = '<p>Hecho no encontrado.</p>';
  }
});
