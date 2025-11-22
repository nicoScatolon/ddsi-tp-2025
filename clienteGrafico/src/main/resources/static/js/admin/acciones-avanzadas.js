// ===== CONFIRMACIÓN DE ACCIONES =====
let formPendiente = null;

function confirmarAccion(accion) {
    formPendiente = event.target;

    document.getElementById('mensajeConfirmacion').innerHTML =
        `¿Estás seguro de que deseas <strong>${accion}</strong>?<br><span class="text-muted">Esta acción se ejecutará inmediatamente.</span>`;

    const modal = new bootstrap.Modal(document.getElementById('modalConfirmacion'));
    modal.show();

    return false;
}

document.getElementById('btnConfirmarAccion')?.addEventListener('click', function() {
    if (formPendiente) {
        const modal = bootstrap.Modal.getInstance(document.getElementById('modalConfirmacion'));
        modal.hide();
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

    if (!nombre || !tipoFuente) {
        alert('Por favor, completa todos los campos antes de continuar.');
        return false;
    }

    return confirm(`¿Estás seguro de crear la fuente "${nombre}" de tipo ${tipoFuente}?`);
}

// ===== CONFIRMACIÓN ELIMINAR FUENTE =====
function confirmarEliminarFuente(event, form) {
    event.preventDefault();

    const confirmacion = confirm('¿Estás seguro de que deseas eliminar esta fuente?\n\nEsta acción no se puede deshacer.');

    if (confirmacion) {
        form.submit();
    }

    return false;
}

// ===== GESTIÓN DE PROXY =====
function cargarDatosProxy(button) {
    const fuenteId = button.getAttribute('data-fuente-id');
    const fuenteNombre = button.getAttribute('data-fuente-nombre');

    document.getElementById('proxyNombre').textContent = fuenteNombre;
    document.getElementById('proxyId').textContent = fuenteId;
    document.getElementById('proxyFuenteId').value = fuenteId;
}

function guardarConfigProxy() {
    const fuenteId = document.getElementById('proxyFuenteId').value;
    const timeout = document.getElementById('proxyTimeout').value;
    const retries = document.getElementById('proxyRetries').value;
    const cache = document.getElementById('proxyCache').checked;

    // Aquí puedes hacer una petición AJAX para guardar la configuración
    console.log('Guardando configuración de proxy:', {
        fuenteId,
        timeout,
        retries,
        cache
    });

    // Ejemplo con fetch (ajusta la URL según tu backend)
    /*
    fetch(`/admin/adminsuperior/fuentes/${fuenteId}/config-proxy`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ timeout, retries, cache })
    })
    .then(response => response.json())
    .then(data => {
        alert('Configuración guardada exitosamente');
        const modal = bootstrap.Modal.getInstance(document.getElementById('modalGestionarProxy'));
        modal.hide();
    })
    .catch(error => {
        alert('Error al guardar la configuración');
        console.error('Error:', error);
    });
    */

    // Por ahora, solo cerramos el modal
    alert('Configuración guardada exitosamente');
    const modal = bootstrap.Modal.getInstance(document.getElementById('modalGestionarProxy'));
    modal.hide();
}

// ===== AUTO-HIDE ALERTS =====
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
});