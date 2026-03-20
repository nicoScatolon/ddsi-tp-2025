document.addEventListener('DOMContentLoaded', function () {

    // --- Colapsables: expandir automáticamente los que tienen valor ---
    document.querySelectorAll('.filter-group.collapsible').forEach(function (collapsible) {
        const label = collapsible.querySelector('label');
        const inputs = collapsible.querySelectorAll('input, select');

        const hasValue = Array.from(inputs).some(function (input) {
            return input.value && input.value.trim() !== '' && input.value !== 'false';
        });

        if (hasValue) collapsible.classList.remove('collapsed');

        if (label) {
            label.style.cursor = 'pointer';
            label.addEventListener('click', function () {
                collapsible.classList.toggle('collapsed');
            });
        }
    });

    // --- Burbujas: generadas desde los params de la URL ---
    const FILTER_LABELS = {
        curado:        'Visualización',
        provincia:     'Provincia',
        categoria:     'Categoría',
        etiqueta:      'Etiqueta',
        fuenteId:      'Fuente',
        fReporteDesde: 'Reporte desde',
        fReporteHasta: 'Reporte hasta',
        fAconDesde:    'Acontec. desde',
        fAconHasta:    'Acontec. hasta'
    };

    const DATE_PARAMS = ['fReporteDesde', 'fReporteHasta', 'fAconDesde', 'fAconHasta'];

    function formatDate(val) {
        const parts = val.split('-');
        if (parts.length === 3) return parts[2] + '/' + parts[1] + '/' + parts[0];
        return val;
    }

    const bar = document.getElementById('active-filters');
    const params = new URLSearchParams(window.location.search);
    const fuenteSelect = document.querySelector('[name="fuenteId"]');

    let hayFiltros = false;

    Object.keys(FILTER_LABELS).forEach(function (param) {
        const valor = params.get(param);

        // Ignorar vacíos, cero, y curado=false (es el valor por defecto)
        if (!valor || valor.trim() === '' || valor === '0') return;
        if (param === 'curado' && valor === 'false') return;

        hayFiltros = true;

        let valorMostrado = valor;
        if (DATE_PARAMS.includes(param)) {
            valorMostrado = formatDate(valor);
        } else if (param === 'fuenteId' && fuenteSelect) {
            const opt = fuenteSelect.options[fuenteSelect.selectedIndex];
            valorMostrado = opt ? opt.text : valor;
        } else if (param === 'curado') {
            valorMostrado = valor === 'true' ? 'Curada' : 'Irrestricta';
        }

        const bubble = document.createElement('span');
        bubble.className = 'filter-bubble';
        bubble.dataset.param = param;
        bubble.innerHTML =
            '<span class="bubble-label">' + FILTER_LABELS[param] + '</span>' +
            '<span class="bubble-value">' + valorMostrado + '</span>' +
            '<button type="button" class="bubble-remove" aria-label="Quitar filtro ' + param + '">&times;</button>';

        bubble.querySelector('.bubble-remove').addEventListener('click', function () {
            const field = document.querySelector('[name="' + param + '"]');
            if (field) field.value = param === 'curado' ? 'false' : '';

            bubble.remove();

            if (bar.querySelectorAll('.filter-bubble').length === 0) {
                bar.style.display = 'none';
            }

            const form = document.querySelector('.sidebar form');
            if (form) {
                const pageInput = form.querySelector('[name="page"]');
                if (pageInput) pageInput.value = '0';
                form.submit();
            }
        });

        bar.appendChild(bubble);
    });

    if (hayFiltros) bar.style.display = 'flex';
});