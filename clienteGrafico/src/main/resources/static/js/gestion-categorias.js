// =========================
// GESTIÓN DE CATEGORÍAS - JavaScript
// =========================

document.addEventListener('DOMContentLoaded', () => {
    // Elementos DOM
    const searchInput = document.getElementById('searchInput');
    const categoriasGrid = document.getElementById('categoriasGrid');
    const noResultados = document.getElementById('noResultados');
    const resultadosCount = document.getElementById('resultadosCount');

    // Modales
    const modalCategoria = document.getElementById('modalCategoria');
    const modalEliminar = document.getElementById('modalEliminar');

    // Botones principales
    const btnCrearCategoria = document.getElementById('btnCrearCategoria');
    const btnCerrarModal = document.getElementById('btnCerrarModal');
    const btnCancelar = document.getElementById('btnCancelar');
    const btnCerrarEliminar = document.getElementById('btnCerrarEliminar');
    const btnCancelarEliminar = document.getElementById('btnCancelarEliminar');

    // Formularios
    const formCategoria = document.getElementById('formCategoria');
    const formEliminar = document.getElementById('formEliminar');
    const modalTitulo = document.getElementById('modalTitulo');
    const btnGuardarTexto = document.getElementById('btnGuardarTexto');

    // Variables de estado
    let modoEdicion = false;

    // =========================
    // BÚSQUEDA DE CATEGORÍAS
    // =========================
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase().trim();
            const cards = categoriasGrid.querySelectorAll('.categoria-card');
            let visibleCount = 0;

            cards.forEach(card => {
                const nombre = card.getAttribute('data-nombre').toLowerCase();
                const matches = nombre.includes(searchTerm);

                if (matches) {
                    card.style.display = 'flex';
                    visibleCount++;
                } else {
                    card.style.display = 'none';
                }
            });

            // Actualizar contador y mensaje de no resultados
            if (visibleCount === 0 && searchTerm !== '') {
                noResultados.classList.remove('hidden');
                resultadosCount.textContent = 'No se encontraron resultados';
            } else {
                noResultados.classList.add('hidden');
                resultadosCount.innerHTML = `Mostrando <strong>${visibleCount}</strong> categoría${visibleCount !== 1 ? 's' : ''}`;
            }
        });
    }

    // =========================
    // CREAR NUEVA CATEGORÍA
    // =========================
    btnCrearCategoria.addEventListener('click', () => {
        modoEdicion = false;
        modalTitulo.textContent = 'Nueva Categoría';
        btnGuardarTexto.textContent = 'Crear';
        formCategoria.reset();
        formCategoria.action = '/admin/categorias';
        document.getElementById('categoriaId').value = '';
        abrirModal(modalCategoria);
    });

    // =========================
    // EDITAR CATEGORÍA
    // =========================
    categoriasGrid.addEventListener('click', (e) => {
        const btnEditar = e.target.closest('.btn-editar');
        if (btnEditar) {
            modoEdicion = true;
            const id = btnEditar.getAttribute('data-id');
            const nombre = btnEditar.getAttribute('data-nombre');

            modalTitulo.textContent = 'Editar Categoría';
            btnGuardarTexto.textContent = 'Guardar Cambios';
            formCategoria.action = `/admin/categorias/${id}`;
            document.getElementById('categoriaId').value = id;
            document.getElementById('categoriaNombre').value = nombre;

            abrirModal(modalCategoria);
        }
    });

    // =========================
    // ELIMINAR CATEGORÍA
    // =========================
    categoriasGrid.addEventListener('click', (e) => {
        const btnEliminar = e.target.closest('.btn-eliminar');
        if (btnEliminar) {
            const id = btnEliminar.getAttribute('data-id');
            const nombre = btnEliminar.getAttribute('data-nombre');

            formEliminar.action = `/admin/categorias/${id}`;
            document.getElementById('categoriaIdEliminar').value = id;
            document.getElementById('categoriaEliminarNombre').textContent = nombre;

            abrirModal(modalEliminar);
        }
    });

    // =========================
    // GESTIÓN DE MODALES
    // =========================
    function abrirModal(modal) {
        modal.classList.remove('hidden');
        document.body.style.overflow = 'hidden';

        // Focus en el input si es el modal de categoría
        if (modal === modalCategoria) {
            setTimeout(() => {
                document.getElementById('categoriaNombre').focus();
            }, 100);
        }
    }

    function cerrarModal(modal) {
        modal.classList.add('hidden');
        document.body.style.overflow = '';
    }

    // Cerrar modal con botón X
    btnCerrarModal.addEventListener('click', () => cerrarModal(modalCategoria));
    btnCancelar.addEventListener('click', () => cerrarModal(modalCategoria));
    btnCerrarEliminar.addEventListener('click', () => cerrarModal(modalEliminar));
    btnCancelarEliminar.addEventListener('click', () => cerrarModal(modalEliminar));

    // Cerrar modal al hacer clic fuera
    modalCategoria.addEventListener('click', (e) => {
        if (e.target === modalCategoria) {
            cerrarModal(modalCategoria);
        }
    });

    modalEliminar.addEventListener('click', (e) => {
        if (e.target === modalEliminar) {
            cerrarModal(modalEliminar);
        }
    });

    // Cerrar modal con tecla ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            if (!modalCategoria.classList.contains('hidden')) {
                cerrarModal(modalCategoria);
            }
            if (!modalEliminar.classList.contains('hidden')) {
                cerrarModal(modalEliminar);
            }
        }
    });

    console.log('✅ Gestión de Categorías inicializada');
});