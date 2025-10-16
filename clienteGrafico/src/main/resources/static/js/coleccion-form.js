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

    // Mapeo de tipos backend a frontend
    const tipoMap = {
        'titulo': 'Titulo',
        'categoria': 'Categoria',
        'etiqueta': 'Etiqueta',
        'provincia': 'Provincia',
        'cargaEntreFechas': 'CargaEntreFechas',
        'ocurrenciaEntreFechas': 'OcurrenciaEntreFechas',
        'contenidoMultimedia': 'ContenidoMultimedia'
    };

    const tipoMapReverse = Object.fromEntries(
        Object.entries(tipoMap).map(([k, v]) => [v, k])
    );

    // Cargar criterios existentes en modo edición
    const coleccionDataDiv = document.getElementById('coleccionData');
    if (coleccionDataDiv) {
        const esNueva = coleccionDataDiv.dataset.esNueva === 'true';
        const criteriosJson = coleccionDataDiv.dataset.criteriosJson;

        console.log('Es nueva:', esNueva);
        console.log('Criterios JSON:', criteriosJson);

        if (!esNueva && criteriosJson) {
            try {
                const criteriosData = JSON.parse(criteriosJson);
                console.log('Criterios parseados:', criteriosData);

                if (Array.isArray(criteriosData) && criteriosData.length > 0) {
                    criteriosData.forEach(criterio => {
                        const tipoFrontend = tipoMap[criterio.tipo] || capitalizeFirst(criterio.tipo);
                        console.log('Creando bloque para:', tipoFrontend, 'con params:', criterio.parametros);
                        const bloque = crearBloqueCriterio(tipoFrontend, criterioIndex, criterio.parametros || {});
                        criteriosContainer.appendChild(bloque);
                        added.add(tipoFrontend);
                        criterioIndex++;
                    });
                    console.log(`Se cargaron ${criteriosData.length} criterios`);
                } else {
                    console.log('No hay criterios para cargar');
                }
            } catch (e) {
                console.error('Error parseando criterios:', e);
            }
        }
    }

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

    function crearBloqueCriterio(tipo, index, parametrosExistentes = {}) {
        console.log(`Creando bloque criterio: ${tipo}, index: ${index}, params:`, parametrosExistentes);

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

        // Crear hidden para el tipo (convertir a camelCase para backend)
        const tipoBinding = tipoMapReverse[tipo] || (tipo.charAt(0).toLowerCase() + tipo.slice(1));
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
                primera.value = formatDatetimeLocal(parametrosExistentes.primeraFecha) || '';
                primera.addEventListener('change', (e) => actualizarParametro(e.target));
                wrapper.appendChild(primera);

                wrapper.appendChild(crearLabel('Segunda fecha'));
                const segunda = crearInputDatetime();
                segunda.classList.add('criterio-segunda-fecha');
                segunda.dataset.paramKey = 'segundaFecha';
                segunda.dataset.criterioIndex = index;
                segunda.value = formatDatetimeLocal(parametrosExistentes.segundaFecha) || '';
                segunda.addEventListener('change', (e) => actualizarParametro(e.target));
                wrapper.appendChild(segunda);

                // Crear hiddens iniciales si hay valores
                if (parametrosExistentes.primeraFecha) {
                    crearHiddenParam(index, 'primeraFecha', parametrosExistentes.primeraFecha);
                }
                if (parametrosExistentes.segundaFecha) {
                    crearHiddenParam(index, 'segundaFecha', parametrosExistentes.segundaFecha);
                }
                break;

            case 'Categoria':
                wrapper.appendChild(crearLabel('Categoría'));
                const cclone = clonarInputTemplate(categoriaTemplate);
                const inpCat = cclone.querySelector('input[type="text"]');
                if (inpCat) {
                    inpCat.classList.add('criterio-categoria-input');
                    inpCat.dataset.paramKey = 'categoria';
                    inpCat.dataset.criterioIndex = index;
                    inpCat.value = parametrosExistentes.categoria || '';
                    inpCat.addEventListener('change', (e) => actualizarParametro(e.target));
                }
                wrapper.appendChild(cclone);
                if (parametrosExistentes.categoria) {
                    crearHiddenParam(index, 'categoria', parametrosExistentes.categoria);
                }
                break;

            case 'Etiqueta':
                wrapper.appendChild(crearLabel('Etiqueta'));
                const selEt = clonarSelectTemplate(etiquetaTemplate);
                selEt.classList.add('criterio-etiqueta-select');
                selEt.dataset.paramKey = 'etiqueta';
                selEt.dataset.criterioIndex = index;
                selEt.value = parametrosExistentes.etiqueta || '';
                selEt.addEventListener('change', (e) => actualizarParametro(e.target));
                wrapper.appendChild(selEt);
                if (parametrosExistentes.etiqueta) {
                    crearHiddenParam(index, 'etiqueta', parametrosExistentes.etiqueta);
                }
                break;

            case 'Provincia':
                wrapper.appendChild(crearLabel('Provincia'));
                const selProv = provinciaTemplate ? clonarSelectTemplate(provinciaTemplate) : null;
                if (selProv) {
                    selProv.classList.add('criterio-provincia-select');
                    selProv.dataset.paramKey = 'provincia';
                    selProv.dataset.criterioIndex = index;
                    selProv.value = parametrosExistentes.provincia || '';
                    selProv.addEventListener('change', (e) => actualizarParametro(e.target));
                    wrapper.appendChild(selProv);
                } else {
                    const ip = document.createElement('input');
                    ip.type = 'text';
                    ip.placeholder = 'Provincia';
                    ip.classList.add('criterio-provincia-input');
                    ip.dataset.paramKey = 'provincia';
                    ip.dataset.criterioIndex = index;
                    ip.value = parametrosExistentes.provincia || '';
                    ip.addEventListener('change', (e) => actualizarParametro(e.target));
                    wrapper.appendChild(ip);
                }
                if (parametrosExistentes.provincia) {
                    crearHiddenParam(index, 'provincia', parametrosExistentes.provincia);
                }
                break;

            case 'Titulo':
                wrapper.appendChild(crearLabel('Título exacto'));
                const tit = document.createElement('input');
                tit.type = 'text';
                tit.classList.add('criterio-titulo');
                tit.dataset.paramKey = 'titulo';
                tit.dataset.criterioIndex = index;
                tit.value = parametrosExistentes.titulo || '';
                tit.addEventListener('change', (e) => actualizarParametro(e.target));
                wrapper.appendChild(tit);
                if (parametrosExistentes.titulo) {
                    crearHiddenParam(index, 'titulo', parametrosExistentes.titulo);
                }
                break;

            case 'ContenidoMultimedia':
                // No tiene parámetros, solo el tipo
                break;
        }
        return wrapper;
    }

    function crearHiddenParam(criterioIndex, paramKey, value) {
        const hiddenParam = document.createElement('input');
        hiddenParam.type = 'hidden';
        hiddenParam.name = `listaCriterios[${criterioIndex}].parametros[${paramKey}]`;
        hiddenParam.value = value;
        hiddenParam.dataset.criterioIndex = criterioIndex;
        hiddenParam.dataset.paramKey = paramKey;
        generatedHiddenFields.appendChild(hiddenParam);
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
            crearHiddenParam(criterioIndex, paramKey, value);
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
        if (!templateSelect) return null;
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
        if (!templateDiv) return null;
        const clone = templateDiv.cloneNode(true);
        clone.style.display = 'block';
        clone.removeAttribute('id');
        return clone;
    }

    function formatDatetimeLocal(dateStr) {
        if (!dateStr) return '';
        // Convertir "2024-01-15T10:30:00" a "2024-01-15T10:30"
        return dateStr.substring(0, 16);
    }

    function capitalizeFirst(str) {
        if (!str) return '';
        return str.charAt(0).toUpperCase() + str.slice(1);
    }

    // Fuentes toggle + chips
    if (toggleFuentes && panelFuentes) {
        toggleFuentes.addEventListener("click", () => {
            const expanded = panelFuentes.style.display === "block";
            panelFuentes.style.display = expanded ? "none" : "block";
            toggleFuentes.setAttribute("aria-expanded", !expanded);
            panelFuentes.setAttribute("aria-hidden", expanded);
        });
    }

    const checkboxes = panelFuentes ? panelFuentes.querySelectorAll(".fuente-checkbox") : [];
    checkboxes.forEach(cb => cb.addEventListener("change", updateFuentes));

    function updateFuentes() {
        if (!panelFuentes || !chipsContainer) return;

        const selected = Array.from(panelFuentes.querySelectorAll(".fuente-checkbox:checked"));
        chipsContainer.innerHTML = '';

        // Limpiar hidden inputs anteriores de fuentes
        generatedHiddenFields.querySelectorAll('input[name="listaIdsFuentes"]').forEach(el => el.remove());

        selected.forEach((cb) => {
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

    // Inicializar fuentes (importante para modo edición)
    updateFuentes();

    // Botón cancelar
    const cancelBtn = document.getElementById('cancelBtn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', () => {
            window.location.href = '/colecciones';
        });
    }

    // Debug antes del submit
    const form = document.getElementById('collectionForm') || document.getElementById('createCollectionForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            console.log('=== DATOS A ENVIAR ===');
            const formData = new FormData(this);
            for (let [key, value] of formData.entries()) {
                console.log(`${key} = ${value}`);
            }
            console.log('======================');
        });
    }
});