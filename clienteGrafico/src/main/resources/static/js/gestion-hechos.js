// =========================
// GESTIÓN DE HECHOS - JavaScript
// =========================

document.addEventListener('DOMContentLoaded', () => {
    // Elementos del modal
    const modalSugerencia = document.getElementById('modalSugerencia');
    const btnCerrarModalSugerencia = document.getElementById('btnCerrarModalSugerencia');
    const btnCancelarSugerencia = document.getElementById('btnCancelarSugerencia');
    const formSugerencia = document.getElementById('formSugerencia');
    const sugerenciaTexto = document.getElementById('sugerenciaTexto');

    // =========================
    // ABRIR MODAL DE SUGERENCIA
    // =========================
    document.addEventListener('click', (e) => {
        const btnSuggest = e.target.closest('.btn-action-icon.suggest');
        if (btnSuggest) {
            const hechoId = btnSuggest.getAttribute('data-hecho-id');
            const hechoTitulo = btnSuggest.getAttribute('data-hecho-titulo');

            // Llenar el formulario
            document.getElementById('sugerenciaIdHecho').value = hechoId;
            document.getElementById('sugerenciaHechoTitulo').textContent = hechoTitulo;
            document.getElementById('sugerenciaTexto').value = '';

            // Abrir modal
            abrirModal(modalSugerencia);
        }
    });

    // =========================
    // GESTIÓN DE MODALES
    // =========================
    function abrirModal(modal) {
        if (!modal) return;

        modal.classList.remove('hidden');
        document.body.style.overflow = 'hidden';

        setTimeout(() => {
            if (sugerenciaTexto) sugerenciaTexto.focus();
        }, 100);
    }

    function cerrarModal(modal) {
        if (!modal) return;

        modal.classList.add('hidden');
        document.body.style.overflow = '';
    }

    // Cerrar modal sugerencia
    if (btnCerrarModalSugerencia) {
        btnCerrarModalSugerencia.addEventListener('click', () => cerrarModal(modalSugerencia));
    }
    if (btnCancelarSugerencia) {
        btnCancelarSugerencia.addEventListener('click', () => cerrarModal(modalSugerencia));
    }

    // Cerrar modal al hacer clic fuera
    if (modalSugerencia) {
        modalSugerencia.addEventListener('click', (e) => {
            if (e.target === modalSugerencia) {
                cerrarModal(modalSugerencia);
            }
        });
    }

    // Cerrar modal con tecla ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            if (modalSugerencia && !modalSugerencia.classList.contains('hidden')) {
                cerrarModal(modalSugerencia);
            }
        }
    });
});