document.addEventListener('DOMContentLoaded', () => {

  /* =====================
     0. Resumen de Actividad dinámico
     ===================== */
  const ACTIVIDADES = [
    {
      tipo: "Hecho creado",
      datetime: "2025-09-01 01:45",
      usuario: { id: 102, nombre: "Juan Pérez" }
    },
    {
      tipo: "Solicitud de eliminación",
      datetime: "2025-09-01 01:20",
      usuario: { id: 203, nombre: "María González" }
    }
  ];

  const resumenContainer = document.querySelector("#resumen .activity-feed");
  if (resumenContainer) {
    resumenContainer.innerHTML = ''; // limpiar contenido hardcodeado
    ACTIVIDADES.forEach(act => {
      const item = document.createElement("div");
      item.className = "activity-item";

      const header = document.createElement("div");
      header.className = "activity-header";
      header.innerHTML = `
        <span class="activity-type">${act.tipo}</span>
        <span class="activity-datetime">${act.datetime}</span>
      `;

      const details = document.createElement("div");
      details.className = "activity-details";
      details.innerHTML = `
        <span><strong>ID Usuario:</strong> ${act.usuario.id}</span>
        <span><strong>Nombre:</strong> ${act.usuario.nombre}</span>
      `;

      item.appendChild(header);
      item.appendChild(details);
      resumenContainer.appendChild(item);
    });
  }

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

  const solicitudesContainer = document.getElementById('solicitudes-list');

  function renderSolicitudes() {
    if (!solicitudesContainer) return;
    solicitudesContainer.innerHTML = '';

    if (solicitudes.length === 0) {
      solicitudesContainer.innerHTML = '<p>No hay solicitudes pendientes.</p>';
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

      solicitudesContainer.appendChild(card);
    });
  }

  renderSolicitudes();

  /* =========================
   Gestión de Hechos (Moderación)
   ========================= */
  const hechosContainer = document.getElementById('moderacion-hechos');

  // Datos demo
  const moderacionNuevos = [
    { id: 'n-1', usuario: 'Ana López', title: 'Denuncia ilegal - nueva', descripcion: 'Descripción del hecho nuevo enviado por usuario.', fecha: '2025-09-02' },
    { id: 'n-2', usuario: 'Carlos Ruiz', title: 'Incidente en parque', descripcion: 'Nuevo hecho reportado con fotos y coordenadas.', fecha: '2025-09-03' }
  ];

  const moderacionModificados = [
    { id: 'm-1', usuario: 'Lucía Díaz', title: 'Hecho 5 (modificado)', descripcion: 'Cambio de ubicación y texto explicativo.', fecha: '2025-09-04' },
    { id: 'm-2', usuario: 'Martín Soto', title: 'Hecho 7 (modificado)', descripcion: 'Corrección en categoría y detalle.', fecha: '2025-09-05' }
  ];

  function renderHechos(type = 'nuevos') {
    if (!hechosContainer) return;
    hechosContainer.innerHTML = '';

    const list = type === 'nuevos' ? moderacionNuevos : moderacionModificados;

    if (!list || list.length === 0) {
      hechosContainer.innerHTML = `<p>No hay peticiones de ${type} pendientes.</p>`;
      return;
    }

    list.forEach(item => {
      const card = document.createElement('div');
      card.className = 'solicitud-card';

      // Definir link según tipo
      const viewHref = `hecho-suggestion.html?id=${item.id}`;


      card.innerHTML = `
        <div class="solicitud-info">
          <span><strong>ID:</strong> ${item.id}</span>
          <span><strong>Usuario:</strong> ${item.usuario}</span>
          <span><strong>Fecha:</strong> ${item.fecha}</span>
        </div>
        <div class="solicitud-razon"><strong>Título:</strong> ${item.title}</div>
        <div class="solicitud-actions-row">
          <a href="${viewHref}" class="btn">Ver ${type === 'nuevos' ? 'Nuevo Hecho' : 'Sugerencia'}</a>
          <div class="solicitud-actions">
            <button class="accept">Aceptar</button>
            <button class="reject">Rechazar</button>
          </div>
        </div>
      `;

      const acceptBtn = card.querySelector('.accept');
      const rejectBtn = card.querySelector('.reject');

      acceptBtn.addEventListener('click', () => {
        alert(`Moderación (${type}) ID ${item.id} aceptada.`);
        const idx = list.indexOf(item);
        if (idx > -1) list.splice(idx, 1);
        renderHechos(type);
      });

      rejectBtn.addEventListener('click', () => {
        alert(`Moderación (${type}) ID ${item.id} rechazada.`);
        const idx = list.indexOf(item);
        if (idx > -1) list.splice(idx, 1);
        renderHechos(type);
      });

      hechosContainer.appendChild(card);
    });
  }

  // Pestañas de moderación
  const modTabs = document.querySelectorAll('.mod-tab');
  modTabs.forEach(tab => {
    tab.addEventListener('click', () => {
      modTabs.forEach(t => {
        t.classList.toggle('active', t === tab);
        t.setAttribute('aria-selected', t === tab ? 'true' : 'false');
      });
      const type = tab.dataset.type;
      renderHechos(type);
    });
  });

  // Render inicial
  renderHechos('nuevos');


  /* =====================
     Auxiliares
     ===================== */
  function escapeHtml(unsafe) {
    if (!unsafe) return '';
    return unsafe
      .replaceAll('&', '&amp;')
      .replaceAll('<', '&lt;')
      .replaceAll('>', '&gt;')
      .replaceAll('"', '&quot;')
      .replaceAll("'", '&#039;');
  }

});
