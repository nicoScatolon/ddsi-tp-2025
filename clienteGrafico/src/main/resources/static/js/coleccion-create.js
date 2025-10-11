document.addEventListener("DOMContentLoaded", () => {
    const criterioSelect = document.getElementById('criterioSelect');
    const addCriterioBtn = document.getElementById('addCriterioBtn');
    const criteriosContainer = document.getElementById('criteriosContainer');
    const toggleFuentes = document.getElementById("fuentesToggle");
    const panelFuentes = document.getElementById("fuentesPanel");
    const chipsContainer = document.getElementById("fuentesChips");
    const countSpan = document.getElementById("fuentesCount");
    const generatedHiddenFields = document.getElementById('generatedHiddenFields');

    const categoriaTemplate = document.getElementById('categoriaTemplate');
    const etiquetaTemplate = document.getElementById('etiquetaTemplate');
    const provinciaTemplate = document.getElementById('provinciaTemplate');

    let criterioIndex = 0;
    const added = new Set();

    // Agregar criterio
    addCriterioBtn.addEventListener('click', () => {
        const tipo = criterioSelect.value;
        if (!tipo) return;
        if (added.has(tipo)) {
            alert(`El criterio "${tipo}" ya fue agregado.`);
            return;
        }
        const bloque = crearBloqueCriterio(tipo, criterioIndex);
        criteriosContainer.appendChild(bloque);
        added.add(tipo);
        criterioIndex++;
    });

    function crearBloqueCriterio(tipo, index) {
        const wrapper = document.createElement('div');
        wrapper.className = 'criterio-block';
        wrapper.dataset.tipo = tipo;
        wrapper.dataset.index = index;

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
            reindexarCriterios();
        });
        header.appendChild(removeBtn);
        wrapper.appendChild(header);

        // Crear hidden para el tipo inmediatamente
        const tipoRaw = tipo;
        const tipoBinding = tipoRaw.charAt(0).toLowerCase() + tipoRaw.slice(1);
        const tipoInput = document.createElement('input');
        tipoInput.type = 'hidden';
        tipoInput.className = 'hidden-tipo';
        tipoInput.name = `listaCriterios[${index}].tipo`;
        tipoInput.value = tipoBinding;
        wrapper.appendChild(tipoInput);

        // Campos visibles + hidden inputs para parámetros
        switch (tipo) {
            case 'CargaEntreFechas':
            case 'OcurrenciaEntreFechas':
                wrapper.appendChild(crearLabel('Primera fecha'));
                const primera = crearInputDatetime();
                primera.classList.add('criterio-primera-fecha');
                primera.dataset.paramKey = 'primeraFecha';
                primera.dataset.criterioIndex = index;
                primera.addEventListener('change', (e) => actualizarParametro(e.target));
                wrapper.appendChild(primera);

                wrapper.appendChild(crearLabel('Segunda fecha'));
                const segunda = crearInputDatetime();
                segunda.classList.add('criterio-segunda-fecha');
                segunda.dataset.paramKey = 'segundaFecha';
                segunda.dataset.criterioIndex = index;
                segunda.addEventListener('change', (e) => actualizarParametro(e.target));
                wrapper.appendChild(segunda);
                break;

            case 'Categoria':
                wrapper.appendChild(crearLabel('Categoría'));
                const cclone = clonarInputTemplate(categoriaTemplate);
                const inpCat = cclone.querySelector('input[type="text"]');
                if (inpCat) {
                    inpCat.classList.add('criterio-categoria-input');
                    inpCat.dataset.paramKey = 'categoria';
                    inpCat.dataset.criterioIndex = index;
                    inpCat.addEventListener('change', (e) => actualizarParametro(e.target));
                }
                wrapper.appendChild(cclone);
                break;

            case 'Etiqueta':
                wrapper.appendChild(crearLabel('Etiqueta'));
                const selEt = clonarSelectTemplate(etiquetaTemplate);
                selEt.classList.add('criterio-etiqueta-select');
                selEt.dataset.paramKey = 'etiqueta';
                selEt.dataset.criterioIndex = index;
                selEt.addEventListener('change', (e) => actualizarParametro(e.target));
                wrapper.appendChild(selEt);
                break;

            case 'Provincia':
                wrapper.appendChild(crearLabel('Provincia'));
                const selProv = provinciaTemplate ? clonarSelectTemplate(provinciaTemplate) : null;
                if (selProv) {
                    selProv.classList.add('criterio-provincia-select');
                    selProv.dataset.paramKey = 'provincia';
                    selProv.dataset.criterioIndex = index;
                    selProv.addEventListener('change', (e) => actualizarParametro(e.target));
                    wrapper.appendChild(selProv);
                } else {
                    const ip = document.createElement('input');
                    ip.type = 'text';
                    ip.placeholder = 'Provincia';
                    ip.classList.add('criterio-provincia-input');
                    ip.dataset.paramKey = 'provincia';
                    ip.dataset.criterioIndex = index;
                    ip.addEventListener('change', (e) => actualizarParametro(e.target));
                    wrapper.appendChild(ip);
                }
                break;

            case 'Titulo':
                wrapper.appendChild(crearLabel('Título exacto'));
                const tit = document.createElement('input');
                tit.type = 'text';
                tit.classList.add('criterio-titulo');
                tit.dataset.paramKey = 'titulo';
                tit.dataset.criterioIndex = index;
                tit.addEventListener('change', (e) => actualizarParametro(e.target));
                wrapper.appendChild(tit);
                break;

            case 'ContenidoMultimedia':
                // No tiene parámetros, solo el tipo
                break;
        }
        return wrapper;
    }

    function actualizarParametro(input) {
        const criterioIndex = input.dataset.criterioIndex;
        const paramKey = input.dataset.paramKey;
        let value = input.value;

        // Para fechas, asegurar formato con segundos
        if (paramKey === 'primeraFecha' || paramKey === 'segundaFecha') {
            if (value && value.length === 16) {
                value += ':00';
            }
        }

        // Buscar si ya existe el hidden input
        const existingHidden = generatedHiddenFields.querySelector(
            `input[name="listaCriterios[${criterioIndex}].parametros[${paramKey}]"]`
        );

        if (existingHidden) {
            existingHidden.value = value;
        } else if (value) {
            // Crear nuevo hidden input
            const hiddenParam = document.createElement('input');
            hiddenParam.type = 'hidden';
            hiddenParam.name = `listaCriterios[${criterioIndex}].parametros[${paramKey}]`;
            hiddenParam.value = value;
            hiddenParam.dataset.criterioIndex = criterioIndex;
            hiddenParam.dataset.paramKey = paramKey;
            generatedHiddenFields.appendChild(hiddenParam);
        }
    }

    function reindexarCriterios() {
        const blocks = Array.from(criteriosContainer.querySelectorAll('.criterio-block'));

        blocks.forEach((block, newIndex) => {
            const oldIndex = block.dataset.index;
            block.dataset.index = newIndex;

            // Actualizar el hidden del tipo
            const tipoInput = block.querySelector('.hidden-tipo');
            if (tipoInput) {
                tipoInput.name = `listaCriterios[${newIndex}].tipo`;
            }

            // Actualizar los inputs de parámetros
            const paramInputs = block.querySelectorAll('[data-criterio-index]');
            paramInputs.forEach(input => {
                input.dataset.criterioIndex = newIndex;
            });

            // Actualizar los hidden de parámetros
            const hiddenParams = generatedHiddenFields.querySelectorAll(`[data-criterio-index="${oldIndex}"]`);
            hiddenParams.forEach(hidden => {
                hidden.dataset.criterioIndex = newIndex;
                const paramKey = hidden.dataset.paramKey;
                hidden.name = `listaCriterios[${newIndex}].parametros[${paramKey}]`;
            });
        });

        criterioIndex = blocks.length;
    }

    function crearLabel(texto) {
        const label = document.createElement('label');
        label.className = 'criterio-label';
        label.textContent = texto;
        return label;
    }

    function crearInputDatetime() {
        const input = document.createElement('input');
        input.type = 'datetime-local';
        return input;
    }

    function clonarSelectTemplate(templateSelect) {
        const select = document.createElement('select');
        Array.from(templateSelect.options).forEach(opt => {
            const copy = document.createElement('option');
            copy.value = opt.value;
            copy.text = opt.text;
            select.appendChild(copy);
        });
        return select;
    }

    function clonarInputTemplate(templateDiv) {
        const clone = templateDiv.cloneNode(true);
        clone.style.display = 'block';
        clone.removeAttribute('id');
        return clone;
    }

    // Fuentes toggle + chips
    toggleFuentes.addEventListener("click", () => {
        const expanded = panelFuentes.style.display === "block";
        panelFuentes.style.display = expanded ? "none" : "block";
        toggleFuentes.setAttribute("aria-expanded", !expanded);
        panelFuentes.setAttribute("aria-hidden", expanded);
    });

    const checkboxes = panelFuentes.querySelectorAll(".fuente-checkbox");
    checkboxes.forEach(cb => cb.addEventListener("change", updateFuentes));

    function updateFuentes() {
        const selected = Array.from(panelFuentes.querySelectorAll(".fuente-checkbox:checked"));
        chipsContainer.innerHTML = '';

        // Limpiar hidden inputs anteriores de fuentes
        generatedHiddenFields.querySelectorAll('input[name="listaIdsFuentes"]').forEach(el => el.remove());

        selected.forEach((cb, index) => {
            // Crear chip visual
            const chip = document.createElement('span');
            chip.className = 'chip';
            chip.setAttribute('role','listitem');
            const text = document.createElement('span');
            text.className='chip-label';
            text.textContent = cb.dataset.nombre || cb.value;
            const closeBtn = document.createElement('button');
            closeBtn.type='button';
            closeBtn.className='chip-close';
            closeBtn.textContent='×';
            closeBtn.addEventListener('click', ()=>{ cb.checked = false; updateFuentes(); });
            chip.appendChild(text);
            chip.appendChild(closeBtn);
            chipsContainer.appendChild(chip);

            // Crear hidden input
            const hiddenFuente = document.createElement('input');
            hiddenFuente.type = 'hidden';
            hiddenFuente.name = 'listaIdsFuentes';
            hiddenFuente.value = cb.value;
            generatedHiddenFields.appendChild(hiddenFuente);
        });

        if (countSpan) countSpan.textContent = `(${selected.length})`;
    }

    updateFuentes();

    // Botón cancelar
    const cancelBtn = document.getElementById('cancelBtn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', () => {
            window.location.href = '/colecciones';
        });
    }

    // Debug antes del submit
    document.getElementById('createCollectionForm').addEventListener('submit', function(e) {
        console.log('=== DATOS A ENVIAR ===');
        const formData = new FormData(this);
        for (let [key, value] of formData.entries()) {
            console.log(`${key} = ${value}`);
        }
        console.log('======================');
    });
});