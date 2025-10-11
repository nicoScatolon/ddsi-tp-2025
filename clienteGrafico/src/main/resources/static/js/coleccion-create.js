document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("fuentesToggle");
    const panel = document.getElementById("fuentesPanel");
    const chipsContainer = document.getElementById("fuentesChips");
    const countSpan = document.getElementById("fuentesCount");
    const hiddenInput = document.getElementById("listaIdsFuentes");

    const criterioSelect = document.getElementById('criterioSelect');
    const addCriterioBtn = document.getElementById('addCriterioBtn');
    const criteriosContainer = document.getElementById('criteriosContainer');

    // Plantillas/clonables (renders by Thymeleaf in hidden selects)
    const categoriaTemplate = document.getElementById('categoriaTemplate');
    const etiquetaTemplate = document.getElementById('etiquetaTemplate');
    const fuenteTemplate = document.getElementById('fuenteTemplate');
    const provinciaTemplate = document.getElementById('provinciaTemplate');

    let index = 0; // índice para cada criterio -> criterios[index].<prop>
    const added = new Set();

    addCriterioBtn.addEventListener('click', () => {
        const tipo = criterioSelect.value;
        if (!tipo) return;

        if (added.has(tipo)) {
            alert('El criterio "' + tipo + '" ya fue agregado. No se permiten repetidos.');
            return;
        }

        const bloque = crearBloqueCriterio(tipo, index);
        criteriosContainer.appendChild(bloque);
        added.add(tipo);
        index++;
    });

    function crearBloqueCriterio(tipo, idx) {
        const wrapper = document.createElement('div');
        wrapper.className = 'criterio-block';
        wrapper.dataset.tipo = tipo;

        // encabezado con botón eliminar
        const header = document.createElement('div');
        header.className = 'criterio-header';
        header.innerHTML = `<strong>${tipo}</strong> `;
        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.textContent = 'Eliminar';
        removeBtn.className = 'btn btn-link btn-remove-criterio';
        removeBtn.addEventListener('click', () => {
            wrapper.remove();
            added.delete(tipo);
        });
        header.appendChild(removeBtn);
        wrapper.appendChild(header);

        // siempre incluir un input hidden con el tipo
        const tipoInput = document.createElement('input');
        tipoInput.type = 'hidden';
        tipoInput.name = `criterios[${idx}].tipo`;
        tipoInput.value = tipo;
        wrapper.appendChild(tipoInput);

        // según el tipo, agregar campos específicos
        switch (tipo) {
            case 'CargaEntreFechas':
            case 'OcurrenciaEntreFechas':
                wrapper.appendChild(crearLabel('Primera fecha'));
                wrapper.appendChild(crearInputDatetime(`criterios[${idx}].primeraFecha`));
                wrapper.appendChild(crearLabel('Segunda fecha'));
                wrapper.appendChild(crearInputDatetime(`criterios[${idx}].segundaFecha`));
                break;

            case 'Categoria':
                wrapper.appendChild(crearLabel('Categoría'));
                wrapper.appendChild(clonarInputTemplate(categoriaTemplate, `criterios[${idx}].categoria`));
                break;

            case 'Etiqueta':
                wrapper.appendChild(crearLabel('Etiqueta'));
                wrapper.appendChild(clonarSelectTemplate(etiquetaTemplate, `criterios[${idx}].etiqueta.id`));
                break;

            case 'Fuente':
                wrapper.appendChild(crearLabel('Fuente'));
                wrapper.appendChild(clonarSelectTemplate(fuenteTemplate, `criterios[${idx}].fuente.id`));
                break;

            case 'ContenidoMultimedia':
                wrapper.appendChild(crearLabel('¿Contiene multimedia?'));
                const sel = document.createElement('select');
                sel.name = `criterios[${idx}].contenidoMultimedia`;
                sel.innerHTML = `<option value="">-- elegir --</option>
                         <option value="true">Sí</option>
                         <option value="false">No</option>`;
                wrapper.appendChild(sel);
                break;

            case 'Provincia':
                wrapper.appendChild(crearLabel('Provincia'));
                if (provinciaTemplate && provinciaTemplate.options && provinciaTemplate.options.length > 1) {
                    wrapper.appendChild(clonarSelectTemplate(provinciaTemplate, `criterios[${idx}].provincia`));
                } else {
                    const inp = document.createElement('input');
                    inp.type = 'text';
                    inp.name = `criterios[${idx}].provincia`;
                    inp.placeholder = 'Nombre de la provincia';
                    wrapper.appendChild(inp);
                }
                break;

            case 'Titulo':
                wrapper.appendChild(crearLabel('Título exacto'));
                const tit = document.createElement('input');
                tit.type = 'text';
                tit.name = `criterios[${idx}].titulo`;
                tit.placeholder = 'Título';
                wrapper.appendChild(tit);
                break;

            default:
                wrapper.appendChild(document.createTextNode('Criterio desconocido. Ajustar implementación en el JS.'));
        }

        return wrapper;
    }

    function crearLabel(texto) {
        const label = document.createElement('label');
        label.className = 'criterio-label';
        label.textContent = texto;
        return label;
    }

    function crearInputDatetime(name) {
        const input = document.createElement('input');
        input.type = 'datetime-local';
        input.name = name;
        return input;
    }

    function clonarSelectTemplate(templateSelect, name) {
        if (!templateSelect) {
            const inp = document.createElement('input');
            inp.type = 'text';
            inp.name = name;
            return inp;
        }
        const select = document.createElement('select');
        select.name = name;

        Array.from(templateSelect.options).forEach(opt => {
            const copy = document.createElement('option');
            copy.value = opt.value;
            copy.text = opt.text;
            select.appendChild(copy);
        });

        return select;
    }

    function clonarInputTemplate(templateDiv, name) {
        const clone = templateDiv.cloneNode(true);
        clone.style.display = 'block';
        const input = clone.querySelector('input');
        if (input) input.name = name;
        return clone;
    }

    toggle.addEventListener("click", () => {
        const expanded = panel.style.display === "block";
        panel.style.display = expanded ? "none" : "block";
        toggle.setAttribute("aria-expanded", !expanded);
        panel.setAttribute("aria-hidden", expanded);
    });

    const checkboxes = panel.querySelectorAll(".fuente-checkbox");
    checkboxes.forEach(cb => cb.addEventListener("change", updateFuentes));

    function updateFuentes() {
        const selected = Array.from(panel.querySelectorAll(".fuente-checkbox:checked"));
        const ids = selected.map(cb => cb.value);
        const nombres = selected.map(cb => cb.dataset.nombre);

        // Crear chips con botón X
        chipsContainer.innerHTML = ''; // limpiar antes
        selected.forEach(cb => {
            const chip = document.createElement('span');
            chip.className = 'chip';
            chip.textContent = cb.dataset.nombre;

            // botón eliminar
            const closeBtn = document.createElement('button');
            closeBtn.type = 'button';
            closeBtn.textContent = '×';
            closeBtn.className = 'chip-close';
            closeBtn.addEventListener('click', () => {
                cb.checked = false; // desmarcar la checkbox correspondiente
                updateFuentes(); // actualizar chips y contador
            });

            chip.appendChild(closeBtn);
            chipsContainer.appendChild(chip);
        });

        // Actualizar contador
        countSpan.textContent = `(${ids.length})`;

        // Actualizar input oculto para enviar al backend
        hiddenInput.value = ids.join(",");
    }


    document.getElementById('createCollectionForm').addEventListener('submit', function(e) {
        const select = document.getElementById('algoritmo');
        if (select.value === "") {
            select.removeAttribute('name');
        }

        // 🔒 VALIDACIÓN: asegurar que la categoría elegida es válida (de la lista)
        const categoriasInputs = this.querySelectorAll('input[list="listaCategorias"]');
        const listaCategorias = Array.from(document.querySelectorAll('#listaCategorias option')).map(o => o.value);

        for (const input of categoriasInputs) {
            if (input.value && !listaCategorias.includes(input.value)) {
                e.preventDefault();
                alert(`La categoría "${input.value}" no es válida. 
                Seleccioná una de la lista.`);
                input.focus();
                return;
            }
        }
    });
});
