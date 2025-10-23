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
});
