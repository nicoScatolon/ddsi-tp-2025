// ===== CONFIRMACIÓN DE ACCIONES =====
let formPendiente = null;

function confirmarAccion(accion) {
    // Guardar el formulario que se quiere enviar
    formPendiente = event.target;

    // Actualizar el mensaje del modal
    document.getElementById('mensajeConfirmacion').innerHTML =
        `¿Estás seguro de que deseas <strong>${accion}</strong>?<br><span class="text-muted">Esta acción se ejecutará inmediatamente.</span>`;

    // Mostrar el modal
    const modal = new bootstrap.Modal(document.getElementById('modalConfirmacion'));
    modal.show();

    // Prevenir el envío del formulario
    return false;
}

// Confirmar la acción cuando se hace clic en el botón
document.getElementById('btnConfirmarAccion')?.addEventListener('click', function() {
    if (formPendiente) {
        // Cerrar el modal
        const modal = bootstrap.Modal.getInstance(document.getElementById('modalConfirmacion'));
        modal.hide();

        // Enviar el formulario
        formPendiente.submit();
    }
});

// ===== VALIDACIÓN FORMULARIO CREAR ADMINISTRADOR =====
document.querySelector('form[action*="crear-admin"]')?.addEventListener('submit', function(e) {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (password !== confirmPassword) {
        e.preventDefault();
        alert('Las contraseñas no coinciden. Por favor, verifica los datos ingresados.');
        return false;
    }

    // Confirmar creación de administrador
    const confirmacion = confirm('¿Estás seguro de que deseas crear este nuevo administrador?');
    if (!confirmacion) {
        e.preventDefault();
        return false;
    }
});

// ===== CONFIRMACIÓN CREAR FUENTE =====
function confirmarCreacionFuente() {
    const nombre = document.getElementById('nombreFuente').value;
    const tipoFuente = document.getElementById('tipoFuente').value;

    // Validar que los campos no estén vacíos
    if (!nombre || !tipoFuente) {
        alert('Por favor, completa todos los campos antes de continuar.');
        return false;
    }

    return confirm(`¿Estás seguro de crear la fuente "${nombre}" de tipo ${tipoFuente}?`);
}