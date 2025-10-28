// =========================
// GESTIÓN DE CATEGORÍAS - JavaScript DUAL SCROLL
// =========================

document.addEventListener('DOMContentLoaded', () => {
    // Elementos DOM
    const searchInput = document.getElementById('searchInput');
    const categoriasList = document.getElementById('categoriasList');
    const equivalentesList = document.getElementById('equivalentesList');
    const categoriasCount = document.getElementById('categoriasCount');
    const equivalentesCount = document.getElementById('equivalentesCount');

    // Modales
    const modalCategoria = document.getElementById('modalCategoria');
    const modalEquivalente = document.getElementById('modalEquivalente');

    // Botones
    const btnCrearCategoria = document.getElementById('btnCrearCategoria');
    const btnCerrarModal = document.getElementById('btnCerrarModal');
    const btnCancelar = document.getElementById('btnCancelar');
    const btnCerrarModalEquivalente = document.getElementById('btnCerrarModalEquivalente');
    const btnCancelarEquivalente = document.getElementById('btnCancelarEquivalente');

    // Formularios
    const formCategoria = document.getElementById('formCategoria');
    const formEquivalente = document.getElementById('formEquivalente');
    const modalTitulo = document.getElementById('modalTitulo');
    const btnGuardarTexto = document.getElementById('btnGuardarTexto');
    const modalEquivalenteTitulo = document.getElementById('modalEquivalenteTitulo');
    const btnGuardarEquivalenteTexto = document.getElementById('btnGuardarEquivalenteTexto');

    // CSRF Token
    const csrfToken = document.getElementById('csrfToken')?.value;
    const csrfHeader = document.getElementById('csrfHeader')?.value;

    // =========================
    // ACTUALIZAR CONTADORES
    // =========================
    function actualizarContadores() {
        const categoriasCards = categoriasList?.querySelectorAll('.categoria-card') || [];
        const equivalentesCards = equivalentesList?.querySelectorAll('.equivalente-card') || [];

        const categoriasVisibles = Array.from(categoriasCards).filter(card => card.style.display !== 'none').length;
        const equivalentesVisibles = Array.from(equivalentesCards).filter(card => card.style.display !== 'none').length;

        if (categoriasCount) {
            categoriasCount.textContent = categoriasVisibles;
        }
        if (equivalentesCount) {
            equivalentesCount.textContent = equivalentesVisibles;
        }
    }

    // Actualizar contadores al cargar
    actualizarContadores();

    // =========================
    // BÚSQUEDA DE CATEGORÍAS Y EQUIVALENTES
    // =========================
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase().trim();

            // Buscar en categorías
            const categoriasCards = categoriasList?.querySelectorAll('.categoria-card') || [];
            categoriasCards.forEach(card => {
                const nombre = card.getAttribute('data-nombre').toLowerCase();
                const matches = nombre.includes(searchTerm);
                card.style.display = matches ? 'flex' : 'none';
            });

            // Buscar en equivalentes
            const equivalentesCards = equivalentesList?.querySelectorAll('.equivalente-card') || [];
            equivalentesCards.forEach(card => {
                const nombre = card.getAttribute('data-nombre').toLowerCase();
                const matches = nombre.includes(searchTerm);
                card.style.display = matches ? 'flex' : 'none';
            });

            // Actualizar contadores
            actualizarContadores();
        });
    }

    // =========================
    // CREAR NUEVA CATEGORÍA
    // =========================
    const abrirModalCrear = () => {
        modalTitulo.textContent = 'Nueva Categoría';
        btnGuardarTexto.textContent = 'Crear';

        // Configurar action para POST
        formCategoria.setAttribute('action', '/admin/categorias');
        document.getElementById('formMethod').value = '';

        // Limpiar todos los campos
        document.getElementById('codigoCategoria').value = '';
        document.getElementById('categoriaNombreOriginal').value = '';
        document.getElementById('categoriaNuevoNombre').value = '';
        document.getElementById('categoriaNombreInput').value = '';

        abrirModal(modalCategoria);
    };

    if (btnCrearCategoria) {
        btnCrearCategoria.addEventListener('click', abrirModalCrear);
    }

    // =========================
    // EDITAR CATEGORÍA
    // =========================
    if (categoriasList) {
        categoriasList.addEventListener('click', (e) => {
            const btnEditar = e.target.closest('.btn-editar');
            if (btnEditar) {
                const codigo = btnEditar.getAttribute('data-codigo');
                const nombre = btnEditar.getAttribute('data-nombre');

                modalTitulo.textContent = 'Editar Categoría';
                btnGuardarTexto.textContent = 'Guardar Cambios';

                // Configurar action para PUT
                formCategoria.setAttribute('action', '/admin/categorias');
                document.getElementById('formMethod').value = 'PUT';

                // Llenar los campos para editar
                document.getElementById('codigoCategoria').value = codigo;
                document.getElementById('categoriaNombreOriginal').value = nombre;
                document.getElementById('categoriaNuevoNombre').value = nombre;
                document.getElementById('categoriaNombreInput').value = nombre;

                abrirModal(modalCategoria);
            }
        });
    }

    // =========================
    // SINCRONIZAR Y VALIDAR ANTES DE ENVIAR CATEGORÍA
    // =========================
    if (formCategoria) {
        formCategoria.addEventListener('submit', (e) => {
            e.preventDefault();

            const inputVisible = document.getElementById('categoriaNombreInput').value.trim();
            const method = document.getElementById('formMethod').value;

            if (!inputVisible) {
                alert('Por favor, ingresa un nombre para la categoría');
                return;
            }

            if (method === 'PUT') {
                // EDITAR: enviar codigoCategoria, nombre original y nuevoNombre
                document.getElementById('categoriaNuevoNombre').value = inputVisible;
            } else {
                // CREAR: solo enviar el nombre
                document.getElementById('categoriaNombreOriginal').value = inputVisible;
                // Asegurar que los demás campos estén vacíos
                document.getElementById('codigoCategoria').value = '';
                document.getElementById('categoriaNuevoNombre').value = '';
            }

            // Enviar el formulario
            formCategoria.submit();
        });
    }

    // =========================
    // AGREGAR EQUIVALENTE
    // =========================
    if (categoriasList) {
        categoriasList.addEventListener('click', (e) => {
            const btnAgregarEquiv = e.target.closest('.btn-agregar-equivalente');
            if (btnAgregarEquiv) {
                const codigo = btnAgregarEquiv.getAttribute('data-codigo');
                const nombre = btnAgregarEquiv.getAttribute('data-nombre');

                modalEquivalenteTitulo.textContent = `Agregar Equivalente - ${nombre}`;
                btnGuardarEquivalenteTexto.textContent = 'Agregar';

                // Configurar para POST
                formEquivalente.setAttribute('action', '/admin/categorias/equivalente');
                document.getElementById('formEquivalenteMethod').value = '';

                // Limpiar y configurar campos
                document.getElementById('equivalenteCodigoCategoria').value = codigo;
                document.getElementById('equivalenteNombreOriginal').value = '';
                document.getElementById('equivalenteNuevoNombre').value = '';
                document.getElementById('equivalenteNombreInput').value = '';

                abrirModal(modalEquivalente);
            }
        });
    }

    // =========================
    // EDITAR EQUIVALENTE
    // =========================
    if (equivalentesList) {
        equivalentesList.addEventListener('click', (e) => {
            const btnEditarEquiv = e.target.closest('.btn-editar-equivalente');
            if (btnEditarEquiv) {
                const codigo = btnEditarEquiv.getAttribute('data-codigo');
                const nombre = btnEditarEquiv.getAttribute('data-nombre');

                modalEquivalenteTitulo.textContent = 'Editar Equivalente';
                btnGuardarEquivalenteTexto.textContent = 'Guardar Cambios';

                // Configurar para PUT
                formEquivalente.setAttribute('action', '/admin/categorias/equivalente');
                document.getElementById('formEquivalenteMethod').value = 'PUT';

                // Llenar los campos
                document.getElementById('equivalenteCodigoCategoria').value = codigo;
                document.getElementById('equivalenteNombreOriginal').value = nombre;
                document.getElementById('equivalenteNuevoNombre').value = nombre;
                document.getElementById('equivalenteNombreInput').value = nombre;

                abrirModal(modalEquivalente);
            }
        });
    }

    // =========================
    // SINCRONIZAR Y VALIDAR ANTES DE ENVIAR EQUIVALENTE
    // =========================
    if (formEquivalente) {
        formEquivalente.addEventListener('submit', (e) => {
            e.preventDefault();

            const inputVisible = document.getElementById('equivalenteNombreInput').value.trim();
            const method = document.getElementById('formEquivalenteMethod').value;

            if (!inputVisible) {
                alert('Por favor, ingresa un nombre para el equivalente');
                return;
            }

            if (method === 'PUT') {
                // EDITAR: enviar codigoCategoria, nombre original y nuevoNombre
                document.getElementById('equivalenteNuevoNombre').value = inputVisible;
            } else {
                // CREAR: solo enviar nombre y codigoCategoria
                document.getElementById('equivalenteNombreOriginal').value = inputVisible;
                document.getElementById('equivalenteNuevoNombre').value = '';
            }

            // Enviar el formulario
            formEquivalente.submit();
        });
    }

    // =========================
    // ELIMINAR EQUIVALENTE (DELETE con formulario)
    // =========================
    if (equivalentesList) {
        equivalentesList.addEventListener('click', (e) => {
            const btnEliminar = e.target.closest('.btn-eliminar');
            if (btnEliminar) {
                const nombre = btnEliminar.getAttribute('data-nombre');

                if (!confirm(`¿Estás seguro de eliminar el equivalente "${nombre}"?`)) {
                    return;
                }

                // Crear formulario temporal para DELETE
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = `/admin/categorias/equivalente/${encodeURIComponent(nombre)}`;

                // Agregar _method para DELETE
                const methodInput = document.createElement('input');
                methodInput.type = 'hidden';
                methodInput.name = '_method';
                methodInput.value = 'DELETE';
                form.appendChild(methodInput);

                // Agregar CSRF token
                const csrfInput = document.createElement('input');
                csrfInput.type = 'hidden';
                csrfInput.name = '_csrf';
                csrfInput.value = csrfToken;
                form.appendChild(csrfInput);

                // Enviar formulario
                document.body.appendChild(form);
                form.submit();
            }
        });
    }

    // =========================
    // GESTIÓN DE MODALES
    // =========================
    function abrirModal(modal) {
        if (!modal) return;

        modal.classList.remove('hidden');
        document.body.style.overflow = 'hidden';

        setTimeout(() => {
            if (modal === modalCategoria) {
                const input = document.getElementById('categoriaNombreInput');
                if (input) input.focus();
            } else if (modal === modalEquivalente) {
                const input = document.getElementById('equivalenteNombreInput');
                if (input) input.focus();
            }
        }, 100);
    }

    function cerrarModal(modal) {
        if (!modal) return;

        modal.classList.add('hidden');
        document.body.style.overflow = '';
    }

    // Cerrar modal categoría
    if (btnCerrarModal) {
        btnCerrarModal.addEventListener('click', () => cerrarModal(modalCategoria));
    }
    if (btnCancelar) {
        btnCancelar.addEventListener('click', () => cerrarModal(modalCategoria));
    }

    // Cerrar modal equivalente
    if (btnCerrarModalEquivalente) {
        btnCerrarModalEquivalente.addEventListener('click', () => cerrarModal(modalEquivalente));
    }
    if (btnCancelarEquivalente) {
        btnCancelarEquivalente.addEventListener('click', () => cerrarModal(modalEquivalente));
    }

    // Cerrar modal al hacer clic fuera
    if (modalCategoria) {
        modalCategoria.addEventListener('click', (e) => {
            if (e.target === modalCategoria) {
                cerrarModal(modalCategoria);
            }
        });
    }

    if (modalEquivalente) {
        modalEquivalente.addEventListener('click', (e) => {
            if (e.target === modalEquivalente) {
                cerrarModal(modalEquivalente);
            }
        });
    }

    // Cerrar modal con tecla ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            if (modalCategoria && !modalCategoria.classList.contains('hidden')) {
                cerrarModal(modalCategoria);
            }
            if (modalEquivalente && !modalEquivalente.classList.contains('hidden')) {
                cerrarModal(modalEquivalente);
            }
        }
    });
});