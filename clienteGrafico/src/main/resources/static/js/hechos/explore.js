document.addEventListener('DOMContentLoaded', function() {
    console.log('Script de filtros cargado');

    const collapsibles = document.querySelectorAll('.filter-group.collapsible');
    console.log('Total de filtros colapsables:', collapsibles.length);

    collapsibles.forEach(function(collapsible) {
        const label = collapsible.querySelector('label');

        if (label) {
            label.style.cursor = 'pointer';

            label.addEventListener('click', function(e) {
                // Solo toggle, no prevenir el default para inputs
                collapsible.classList.toggle('collapsed');
            });
        }
    });

    console.log('Eventos de click configurados');

    // Verificar que el botón FAB existe
    const fabButton = document.querySelector('.fab-create');
    if (fabButton) {
        console.log('Botón FAB encontrado:', fabButton);
    } else {
        console.warn('Botón FAB no encontrado en el DOM');
    }
});