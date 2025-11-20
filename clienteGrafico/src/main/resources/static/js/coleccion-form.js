document.addEventListener("DOMContentLoaded", () => {
    const criteriosGrid = document.querySelector('.criterios-grid');
    const criteriosContainer = document.getElementById('criteriosContainer');
    const toggleFuentes = document.getElementById("fuentesToggle");
    const panelFuentes = document.getElementById("fuentesPanel");
    const chipsContainer = document.getElementById("fuentesChips");
    const countSpan = document.getElementById("fuentesCount");
    const generatedHiddenFields = document.getElementById('generatedHiddenFields');

    const categoriaTemplate = document.getElementById('categoriaTemplate');
    const etiquetaTemplate = document.getElementById('etiquetaTemplate');
    const provinciaTemplate = document.getElementById('provinciaTemplate');
    const multimediaTemplate = document.getElementById('multimediaTemplate');

    let criterioIndex = 0;
    const added = new Set();

    // Iconos para cada tipo de criterio
    const iconos = {
        'Titulo': 'fa-heading',
        'Categoria': 'fa-tag',
        'Etiqueta': 'fa-tags',
        'Provincia': 'fa-map-marker-alt',
        'CargaEntreFechas': 'fa-calendar-plus',
        'OcurrenciaEntreFechas': 'fa-calendar-check',
        'ContenidoMultimedia': 'fa-image'
    };

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

                        // Parsear etiquetas si viene como JSON string
                        let params = criterio.parametros || {};
                        if (params.etiquetas && typeof params.etiquetas === 'string') {
                            try {
                                params.etiquetas = JSON.parse(params.etiquetas);
                            } catch (e) {
                                console.error('Error parseando etiquetas:', e);
                            }
                        }

                        const bloque = crearBloqueCriterio(tipoFrontend, criterioIndex, params);
                        criteriosContainer.appendChild(bloque);
                        added.add(tipoFrontend);
                        marcarCriterioSeleccionado(tipoFrontend);
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

    // Manejar clicks en las cards de criterios
    if (criteriosGrid) {
        criteriosGrid.addEventListener('click', (e) => {
            const card = e.target.closest('.criterio-card');
            if (!card) return;

            const tipo = card.dataset.tipo;

            if (card.classList.contains('selected')) {
                // Deseleccionar y eliminar criterio
                eliminarCriterio(tipo);
            } else {
                // Agregar criterio
                if (added.has(tipo)) return;

                const bloque = crearBloqueCriterio(tipo, criterioIndex);
                criteriosContainer.appendChild(bloque);
                added.add(tipo);
                card.classList.add('selected');
                criterioIndex++;
            }
        });
    }

    function marcarCriterioSeleccionado(tipo) {
        const card = criteriosGrid?.querySelector(`[data-tipo="${tipo}"]`);
        if (card) {
            card.classList.add('selected');
        }
    }

    function eliminarCriterio(tipo) {
        // Buscar el bloque del criterio
        const bloque = Array.from(criteriosContainer.children).find(
            el => el.dataset.tipo === tipo
        );

        if (bloque) {
            bloque.remove();
            added.delete(tipo);

            // Desmarcar la card
            const card = criteriosGrid?.querySelector(`[data-tipo="${tipo}"]`);
            if (card) {
                card.classList.remove('selected');
            }

            reindexarCriterios();
        }
    }

    function crearBloqueCriterio(tipo, index, parametrosExistentes = {}) {
        console.log(`Creando bloque criterio: ${tipo}, index: ${index}, params:`, parametrosExistentes);

        const wrapper = document.createElement('div');
        wrapper.className = 'criterio-item';
        wrapper.dataset.tipo = tipo;
        wrapper.dataset.index = index;

        const header = document.createElement('div');
        header.className = 'criterio-item-header';

        const title = document.createElement('div');
        title.className = 'criterio-item-title';
        title.innerHTML = `<i class="fas ${iconos[tipo]}"></i><span>${tipo}</span>`;

        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.className = 'btn-remove';
        removeBtn.innerHTML = '<i class="fas fa-times"></i>';
        removeBtn.addEventListener('click', () => {
            eliminarCriterio(tipo);
        });

        header.appendChild(title);
        header.appendChild(removeBtn);
        wrapper.appendChild(header);

        // Crear hidden para el tipo
        const tipoBinding = tipoMapReverse[tipo] || (tipo.charAt(0).toLowerCase() + tipo.slice(1));
        const tipoInput = document.createElement('input');
        tipoInput.type = 'hidden';
        tipoInput.className = 'hidden-tipo';
        tipoInput.name = `listaCriterios[${index}].tipo`;
        tipoInput.value = tipoBinding;
        wrapper.appendChild(tipoInput);

        const body = document.createElement('div');
        body.className = 'criterio-item-body';

        // Campos según tipo
        switch (tipo) {
            case 'CargaEntreFechas':
            case 'OcurrenciaEntreFechas':
                const fechaRow = document.createElement('div');
                fechaRow.className = 'fecha-row';

                const primeraGroup = document.createElement('div');
                primeraGroup.className = 'fecha-group';
                primeraGroup.innerHTML = '<label class="fecha-label">Desde</label>';
                const primera = crearInputDatetime();
                primera.dataset.paramKey = 'primeraFecha';
                primera.dataset.criterioIndex = index;
                primera.value = formatDatetimeLocal(parametrosExistentes.primeraFecha) || '';
                primera.addEventListener('change', (e) => actualizarParametro(e.target));
                primeraGroup.appendChild(primera);

                const segundaGroup = document.createElement('div');
                segundaGroup.className = 'fecha-group';
                segundaGroup.innerHTML = '<label class="fecha-label">Hasta</label>';
                const segunda = crearInputDatetime();
                segunda.dataset.paramKey = 'segundaFecha';
                segunda.dataset.criterioIndex = index;
                segunda.value = formatDatetimeLocal(parametrosExistentes.segundaFecha) || '';
                segunda.addEventListener('change', (e) => actualizarParametro(e.target));
                segundaGroup.appendChild(segunda);

                fechaRow.appendChild(primeraGroup);
                fechaRow.appendChild(segundaGroup);
                body.appendChild(fechaRow);

                if (parametrosExistentes.primeraFecha) {
                    crearHiddenParam(index, 'primeraFecha', parametrosExistentes.primeraFecha);
                }
                if (parametrosExistentes.segundaFecha) {
                    crearHiddenParam(index, 'segundaFecha', parametrosExistentes.segundaFecha);
                }
                break;

            case 'Categoria':
                const cclone = clonarInputTemplate(categoriaTemplate);
                const inpCat = cclone ? cclone.querySelector('input[type="text"]') : null;
                if (inpCat) {
                    inpCat.classList.add('criterio-input');
                    inpCat.dataset.paramKey = 'categoria';
                    inpCat.dataset.criterioIndex = index;
                    inpCat.value = parametrosExistentes.categoria || '';
                    inpCat.addEventListener('change', (e) => actualizarParametro(e.target));
                    body.appendChild(cclone);
                }
                if (parametrosExistentes.categoria) {
                    crearHiddenParam(index, 'categoria', parametrosExistentes.categoria);
                }
                break;

            case 'Etiqueta':
                const etiqContainer = clonarInputTemplate(etiquetaTemplate);
                if (etiqContainer) {
                    const toggle = etiqContainer.querySelector('.etiquetas-toggle-mini');
                    const panel = etiqContainer.querySelector('.etiquetas-panel-mini');
                    const chipsEtiq = etiqContainer.querySelector('.etiquetas-chips-mini');
                    const countEtiq = etiqContainer.querySelector('.etiquetas-count-mini');

                    if (toggle && panel) {
                        toggle.addEventListener('click', () => {
                            const visible = panel.style.display === 'block';
                            panel.style.display = visible ? 'none' : 'block';
                        });
                    }

                    const checkboxes = etiqContainer.querySelectorAll('.etiqueta-checkbox');
                    checkboxes.forEach(cb => {
                        cb.dataset.criterioIndex = index;

                        if (parametrosExistentes.etiquetas &&
                            Array.isArray(parametrosExistentes.etiquetas) &&
                            parametrosExistentes.etiquetas.includes(cb.value)) {
                            cb.checked = true;
                        }

                        cb.addEventListener('change', () => {
                            actualizarEtiquetas(index, checkboxes, chipsEtiq, countEtiq);
                        });
                    });

                    body.appendChild(etiqContainer);
                    actualizarEtiquetas(index, checkboxes, chipsEtiq, countEtiq);
                }
                break;

            case 'Provincia':
                const provClone = clonarInputTemplate(provinciaTemplate);
                const inpProv = provClone ? provClone.querySelector('input[type="text"]') : null;
                if (inpProv) {
                    inpProv.classList.add('criterio-input');
                    inpProv.dataset.paramKey = 'provincia';
                    inpProv.dataset.criterioIndex = index;
                    inpProv.value = parametrosExistentes.provincia || '';
                    inpProv.addEventListener('change', (e) => actualizarParametro(e.target));
                    body.appendChild(provClone);
                }
                if (parametrosExistentes.provincia) {
                    crearHiddenParam(index, 'provincia', parametrosExistentes.provincia);
                }
                break;

            case 'Titulo':
                const tit = document.createElement('input');
                tit.type = 'text';
                tit.className = 'criterio-input';
                tit.placeholder = 'Título exacto...';
                tit.dataset.paramKey = 'titulo';
                tit.dataset.criterioIndex = index;
                tit.value = parametrosExistentes.titulo || '';
                tit.addEventListener('change', (e) => actualizarParametro(e.target));
                body.appendChild(tit);
                if (parametrosExistentes.titulo) {
                    crearHiddenParam(index, 'titulo', parametrosExistentes.titulo);
                }
                break;

            case 'ContenidoMultimedia':
                const mmClone = clonarInputTemplate(multimediaTemplate);
                if (mmClone) {
                    const buttons = mmClone.querySelectorAll('.multimedia-btn');
                    const hiddenInput = mmClone.querySelector('.criterio-multimedia-hidden');

                    if (buttons && hiddenInput) {
                        hiddenInput.dataset.paramKey = 'tenerMultimedia';
                        hiddenInput.dataset.criterioIndex = index;

                        // Pre-seleccionar valor existente
                        if (parametrosExistentes.tenerMultimedia !== undefined) {
                            const valor = String(parametrosExistentes.tenerMultimedia);
                            buttons.forEach(btn => {
                                if (btn.dataset.value === valor) {
                                    btn.classList.add('active');
                                }
                            });
                            hiddenInput.value = valor;
                        }

                        // Agregar event listeners a los botones
                        buttons.forEach(btn => {
                            btn.addEventListener('click', () => {
                                // Quitar active de todos
                                buttons.forEach(b => b.classList.remove('active'));
                                // Agregar active al clickeado
                                btn.classList.add('active');
                                // Actualizar valor
                                hiddenInput.value = btn.dataset.value;
                                actualizarParametroMultimedia(hiddenInput);
                            });
                        });

                        body.appendChild(mmClone);
                    }
                }

                if (parametrosExistentes.tenerMultimedia !== undefined) {
                    crearHiddenParam(index, 'tenerMultimedia', String(parametrosExistentes.tenerMultimedia));
                }
                break;
        }

        wrapper.appendChild(body);
        return wrapper;
    }

    function actualizarParametroMultimedia(input) {
        const criterioIndex = input.dataset.criterioIndex;
        const paramKey = input.dataset.paramKey;
        const value = input.value;

        const existingHidden = generatedHiddenFields.querySelector(
            `input[name="listaCriterios[${criterioIndex}].parametros[${paramKey}]"]`
        );

        if (existingHidden) {
            existingHidden.value = value;
        } else if (value) {
            crearHiddenParam(criterioIndex, paramKey, value);
        }
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

        if (paramKey === 'primeraFecha' || paramKey === 'segundaFecha') {
            if (value && value.length === 16) {
                value += ':00';
            }
        }

        const existingHidden = generatedHiddenFields.querySelector(
            `input[name="listaCriterios[${criterioIndex}].parametros[${paramKey}]"]`
        );

        if (existingHidden) {
            existingHidden.value = value;
        } else if (value) {
            crearHiddenParam(criterioIndex, paramKey, value);
        }
    }

    function actualizarEtiquetas(criterioIndex, checkboxes, chipsContainer, countSpan) {
        const selected = Array.from(checkboxes).filter(cb => cb.checked);

        if (chipsContainer) {
            chipsContainer.innerHTML = '';
            selected.forEach(cb => {
                const chip = document.createElement('span');
                chip.className = 'chip';
                const text = document.createElement('span');
                text.textContent = cb.dataset.nombre || cb.value;
                const closeBtn = document.createElement('button');
                closeBtn.type = 'button';
                closeBtn.className = 'chip-close';
                closeBtn.textContent = '×';
                closeBtn.addEventListener('click', () => {
                    cb.checked = false;
                    actualizarEtiquetas(criterioIndex, checkboxes, chipsContainer, countSpan);
                });
                chip.appendChild(text);
                chip.appendChild(closeBtn);
                chipsContainer.appendChild(chip);
            });
        }

        if (countSpan) {
            countSpan.textContent = selected.length.toString();
        }

        const existingHidden = generatedHiddenFields.querySelector(
            `input[name="listaCriterios[${criterioIndex}].parametros[etiquetas]"]`
        );
        if (existingHidden) {
            existingHidden.remove();
        }

        if (selected.length > 0) {
            const etiquetasArray = selected.map(cb => cb.value);
            const hiddenEtiquetas = document.createElement('input');
            hiddenEtiquetas.type = 'hidden';
            hiddenEtiquetas.name = `listaCriterios[${criterioIndex}].parametros[etiquetas]`;
            hiddenEtiquetas.value = JSON.stringify(etiquetasArray);
            hiddenEtiquetas.dataset.criterioIndex = criterioIndex;
            hiddenEtiquetas.dataset.paramKey = 'etiquetas';
            generatedHiddenFields.appendChild(hiddenEtiquetas);
        }
    }

    function reindexarCriterios() {
        const blocks = Array.from(criteriosContainer.querySelectorAll('.criterio-item'));

        blocks.forEach((block, newIndex) => {
            const oldIndex = block.dataset.index;
            block.dataset.index = newIndex;

            const tipoInput = block.querySelector('.hidden-tipo');
            if (tipoInput) {
                tipoInput.name = `listaCriterios[${newIndex}].tipo`;
            }

            const paramInputs = block.querySelectorAll('[data-criterio-index]');
            paramInputs.forEach(input => {
                input.dataset.criterioIndex = newIndex;
            });

            const hiddenParams = generatedHiddenFields.querySelectorAll(
                `[data-criterio-index="${oldIndex}"]`
            );
            hiddenParams.forEach(hidden => {
                hidden.dataset.criterioIndex = newIndex;
                const paramKey = hidden.dataset.paramKey;
                hidden.name = `listaCriterios[${newIndex}].parametros[${paramKey}]`;
            });
        });

        criterioIndex = blocks.length;
    }

    function crearInputDatetime() {
        const input = document.createElement('input');
        input.type = 'datetime-local';
        return input;
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

        generatedHiddenFields.querySelectorAll('input[name="listaIdsFuentes"]').forEach(el => el.remove());

        selected.forEach((cb) => {
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

            const hiddenFuente = document.createElement('input');
            hiddenFuente.type = 'hidden';
            hiddenFuente.name = 'listaIdsFuentes';
            hiddenFuente.value = cb.value;
            generatedHiddenFields.appendChild(hiddenFuente);
        });

        if (countSpan) countSpan.textContent = selected.length.toString();
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
    const form = document.getElementById('collectionForm');
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