document.addEventListener('DOMContentLoaded', () => {

  /* =====================
     1. Navegación lateral
     ===================== */
  const menuItems = document.querySelectorAll('.menu-item');
  const sections = document.querySelectorAll('.content-section');

  menuItems.forEach(item => {
    item.addEventListener('click', () => {
      menuItems.forEach(i => i.classList.remove('active'));
      item.classList.add('active');

      const sectionId = item.dataset.section;
      sections.forEach(sec => {
        sec.classList.toggle('active', sec.id === sectionId);
      });
    });
  });

  /* =====================
     2. Importar CSV
     ===================== */
  const importForm = document.getElementById('importForm');
  const fileUpload = document.getElementById('fileUpload');
  const importResult = document.getElementById('importResult');

  if(importForm && fileUpload && importResult){
    importForm.addEventListener('submit', (e) => {
      e.preventDefault();

      const file = fileUpload.files[0];
      if (!file) {
        alert("Selecciona un archivo CSV antes de importar.");
        return;
      }

      const reader = new FileReader();
      reader.onload = function(event) {
        const content = event.target.result;
        const rows = content.split('\n').map(r => r.trim()).filter(r => r);

        let preview = `<strong>Archivo cargado:</strong> ${file.name}<br>`;
        preview += `<strong>Número de líneas:</strong> ${rows.length}<br><br>`;
        preview += `<strong>Primeras filas:</strong><br>`;
        preview += `<code>${rows.slice(0, 5).join('<br>')}</code>`;

        importResult.innerHTML = preview;
        importResult.style.display = "block";
      };
      reader.readAsText(file);
    });
  }

  /* =====================
     3. Solicitudes dinámicas
     ===================== */
  const solicitudes = [
    { id: 1, nombre: 'Juan', apellido: 'Pérez', hechoId: 2, razon: 'Contenido inapropiado', fecha: '2025-09-01' },
    { id: 2, nombre: 'María', apellido: 'González', hechoId: 3, razon: 'Duplicado', fecha: '2025-09-03' }
  ];

  const HECHOS = [
    { id: 2, title: 'Hecho 2' },
    { id: 3, title: 'Hecho 3' }
  ];

   const container = document.getElementById('solicitudes-list');

  
  function renderSolicitudes() {
    container.innerHTML = '';

    if (solicitudes.length === 0) {
      container.innerHTML = '<p>No hay solicitudes pendientes.</p>';
      return;
    }

    solicitudes.forEach(solicitud => {
      const hecho = HECHOS.find(h => h.id === solicitud.hechoId);

      const card = document.createElement('div');
      card.className = 'solicitud-card';

      card.innerHTML = `
        <div class="solicitud-info">
          <span><strong>ID:</strong> ${solicitud.id}</span>
          <span><strong>Nombre:</strong> ${solicitud.nombre} ${solicitud.apellido}</span>
          <span><strong>Fecha:</strong> ${solicitud.fecha}</span>
        </div>
        <div class="solicitud-razon"><strong>Razón:</strong> ${solicitud.razon}</div>
        <div class="solicitud-actions-row">
          <a href="hecho.html?id=${hecho ? hecho.id : '#'}" class="btn">Ver Hecho</a>
          <div class="solicitud-actions">
            <button class="accept">Aceptar</button>
            <button class="reject">Rechazar</button>
          </div>
        </div>
      `;

      const acceptBtn = card.querySelector('.accept');
      const rejectBtn = card.querySelector('.reject');

      acceptBtn.addEventListener('click', () => {
        alert(`Solicitud ID ${solicitud.id} aceptada.`);
        const index = solicitudes.indexOf(solicitud);
        if (index > -1) solicitudes.splice(index, 1);
        renderSolicitudes();
      });

      rejectBtn.addEventListener('click', () => {
        alert(`Solicitud ID ${solicitud.id} rechazada.`);
        const index = solicitudes.indexOf(solicitud);
        if (index > -1) solicitudes.splice(index, 1);
        renderSolicitudes();
      });

      container.appendChild(card);
    });
  }

  renderSolicitudes();

});
