document.addEventListener("DOMContentLoaded", () => {
  // Usamos HECHOS del script hechos.js
  const hechos = HECHOS;

  const container = document.getElementById("mh-list");
  const emptyMsg = document.getElementById("mh-empty");

  // Filtrar solo hechos del usuario
  // Por ahora simulamos que todos son del usuario
  const misHechos = hechos;

  // Función para renderizar la lista compacta
  function renderList(lista) {
    container.innerHTML = "";
    if (!lista.length) {
      emptyMsg.hidden = false;
      return;
    } else {
      emptyMsg.hidden = true;
    }

    lista.forEach(h => {
      const row = document.createElement("div");
      row.className = "mh-row";

      // Izquierda: título + categoría
      const left = document.createElement("div");
      left.className = "mh-left";
      const title = document.createElement("span");
      title.className = "mh-title";
      title.textContent = h.title;
      const meta = document.createElement("span");
      meta.className = "mh-meta";
      meta.textContent = `${h.categoria} • ${h.etiqueta} • ${h.fecha}`;
      left.appendChild(title);
      left.appendChild(meta);

      // Derecha: botones de acción
      const right = document.createElement("div");
      right.className = "mh-right";
      
      // Editar
      const editBtn = document.createElement("button");
      editBtn.className = "mh-edit";
      editBtn.textContent = "Editar";
      editBtn.addEventListener("click", e => {
        e.preventDefault();
        window.location.href = `hecho.html?id=${h.id}`;
      });

      // Borrar
      const delBtn = document.createElement("button");
      delBtn.className = "mh-delete";
      delBtn.textContent = "Borrar";
      delBtn.addEventListener("click", e => {
        e.preventDefault();
        if (confirm(`¿Seguro que querés borrar "${h.title}"?`)) {
          const index = misHechos.findIndex(item => item.id === h.id);
          if (index > -1) {
            misHechos.splice(index, 1);
            renderList(misHechos);
          }
        }
      });

      right.appendChild(editBtn);
      right.appendChild(delBtn);

      row.appendChild(left);
      row.appendChild(right);
      container.appendChild(row);
    });
  }

  renderList(misHechos);
});
