document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('hechoForm');
    const categoriaSelect = document.getElementById('categoria');
    const categoriaNuevaInput = document.getElementById('categoriaNueva');
    const tipoUbicacionSelect = document.getElementById('tipoUbicacion');

    // Campos de ubicación
    const provinciaGroup = document.getElementById('provinciaGroup');
    const localidadGroup = document.getElementById('localidadGroup');
    const calleGroup = document.getElementById('calleGroup');
    const numeroGroup = document.getElementById('numeroGroup');
    const mapaGroup = document.getElementById('mapaGroup');

    const provinciaInput = document.getElementById('provincia');
    const localidadInput = document.getElementById('localidad');
    const calleInput = document.getElementById('calle');
    const numeroInput = document.getElementById('numero');
    const latitudInput = document.getElementById('latitudUbicacion');
    const longitudInput = document.getElementById('longitudUbicacion');

    // Elementos del mapa
    const coordenadasDisplay = document.getElementById('coordenadasDisplay');
    const latDisplay = document.getElementById('latDisplay');
    const lngDisplay = document.getElementById('lngDisplay');

    const submitBtn = form.querySelector('button[type="submit"]');

    // Variable para el mapa de Leaflet
    let map = null;
    let marker = null;

    // Guardamos el name original (establecido por Thymeleaf con th:field)
    const categoriaFieldName = categoriaSelect.getAttribute('name') || categoriaSelect.name || 'categoria.nombre';

    // Inicialización: mostramos el input oculto pero NO le dejamos el name para que no se envíe.
    categoriaNuevaInput.style.display = 'none';
    // removemos el name del input para que no llegue duplicado al servidor
    categoriaNuevaInput.removeAttribute('name');
    categoriaNuevaInput.required = false;

    // Cuando cambia el select: si es 'other' mostramos el input y transferimos el name; si no, aseguramos que solo se envie el select.
    categoriaSelect.addEventListener('change', function() {
        if (this.value === 'other') {
            // Mostrar input para nueva categoría y que sea el que se envíe
            categoriaNuevaInput.style.display = 'block';
            categoriaNuevaInput.required = true;
            categoriaNuevaInput.value = '';
            categoriaNuevaInput.focus();

            // Hacer que el input tenga el name que espera Thymeleaf, y quitarle el name al select
            categoriaNuevaInput.setAttribute('name', categoriaFieldName);
            categoriaSelect.removeAttribute('name');
        } else {
            // Ocultar input, quitarle el name y dejar el select con el name
            categoriaNuevaInput.style.display = 'none';
            categoriaNuevaInput.required = false;
            categoriaNuevaInput.value = '';
            categoriaNuevaInput.removeAttribute('name');

            // Restaurar el name del select (por si antes lo habíamos removido)
            categoriaSelect.setAttribute('name', categoriaFieldName);
        }
    });

    // Inicializar el mapa de Leaflet
    function initMap() {
        if (!map) {
            map = L.map('mapContainer').setView([-34.6037, -58.3816], 13);

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
                maxZoom: 19
            }).addTo(map);

            map.on('click', function(e) {
                const lat = e.latlng.lat.toFixed(7);
                const lng = e.latlng.lng.toFixed(7);

                latitudInput.value = lat;
                longitudInput.value = lng;

                latDisplay.textContent = lat;
                lngDisplay.textContent = lng;
                coordenadasDisplay.style.display = 'block';

                if (marker) {
                    marker.setLatLng(e.latlng);
                } else {
                    marker = L.marker(e.latlng, { draggable: true }).addTo(map);

                    marker.on('dragend', function() {
                        const position = marker.getLatLng();
                        const newLat = position.lat.toFixed(7);
                        const newLng = position.lng.toFixed(7);

                        latitudInput.value = newLat;
                        longitudInput.value = newLng;
                        latDisplay.textContent = newLat;
                        lngDisplay.textContent = newLng;
                    });
                }

                marker.bindPopup(`<b>Ubicación del hecho</b><br>Lat: ${lat}<br>Lng: ${lng}`).openPopup();
            });
        }

        setTimeout(() => map.invalidateSize(), 100);
    }

    // Manejar campos de ubicación según el tipo seleccionado
    tipoUbicacionSelect.addEventListener('change', function() {
        const tipo = this.value;
        resetUbicacionFields();

        if (tipo === 'direccion') {
            provinciaInput.required = true;
            localidadInput.required = true;
            calleInput.required = true;
            numeroInput.required = true;

            provinciaGroup.style.display = 'flex';
            localidadGroup.style.display = 'flex';
            calleGroup.style.display = 'flex';
            numeroGroup.style.display = 'flex';

        } else if (tipo === 'mapa') {
            mapaGroup.style.display = 'flex';
            latitudInput.required = true;
            longitudInput.required = true;
            initMap();
        }
    });

    // Función para resetear campos de ubicación
    function resetUbicacionFields() {
        provinciaGroup.style.display = 'none';
        localidadGroup.style.display = 'none';
        calleGroup.style.display = 'none';
        numeroGroup.style.display = 'none';
        mapaGroup.style.display = 'none';
        coordenadasDisplay.style.display = 'none';

        provinciaInput.required = false;
        localidadInput.required = false;
        calleInput.required = false;
        numeroInput.required = false;
        latitudInput.required = false;
        longitudInput.required = false;

        provinciaInput.value = '';
        localidadInput.value = '';
        calleInput.value = '';
        numeroInput.value = '';
        latitudInput.value = '';
        longitudInput.value = '';

        if (marker && map) {
            try { map.removeLayer(marker); } catch (err) {}
            marker = null;
        }
    }

    resetUbicacionFields();

    // Manejar envío del formulario
    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        // Validación: si usuario eligió Mapa, asegurarse que haya coordenadas
        if (tipoUbicacionSelect.value === 'mapa' && (!latitudInput.value || !longitudInput.value)) {
            alert('Por favor, seleccione una ubicación en el mapa haciendo clic sobre él.');
            return;
        }

        // Si select indica "other" pero el input (que debe enviar) está vacío -> pedir valor
        const sendingCategoriaIsInput = !!categoriaNuevaInput.getAttribute('name');
        if (sendingCategoriaIsInput && !categoriaNuevaInput.value.trim()) {
            alert('Por favor, ingrese la nueva categoría.');
            categoriaNuevaInput.focus();
            return;
        }

        const originalBtnText = submitBtn.textContent;
        submitBtn.disabled = true;
        submitBtn.textContent = 'Enviando...';

        const actionUrl = form.getAttribute('action') || '/hechos/create';
        const fd = new FormData(form);

        try {
            const response = await fetch(actionUrl, {
                method: 'POST',
                body: fd,
                credentials: 'same-origin'
            });

            if (response.ok) {
                window.location.href = '/hechos/list';
                return;
            } else {
                let text = '';
                try { text = await response.text(); } catch (err) {}
                console.error('Error al crear hecho:', response.status, text);
                alert('No se pudo crear el hecho. Estado: ' + response.status);
            }
        } catch (err) {
            console.error('Fetch error:', err);
            const doFallback = confirm('No se pudo conectar con el servidor vía AJAX. ¿Intentar envío tradicional?');
            if (doFallback) {
                // retirar este listener y hacer submit clásico
                form.removeEventListener('submit', arguments.callee);
                form.submit();
                return;
            }
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = originalBtnText;
        }
    });

    // Manejar botón reset
    form.addEventListener('reset', function() {
        setTimeout(() => {
            resetUbicacionFields();

            // Restaurar categoría a estado inicial:
            categoriaNuevaInput.style.display = 'none';
            categoriaNuevaInput.removeAttribute('name');
            categoriaNuevaInput.required = false;
            categoriaNuevaInput.value = '';

            // Restaurar name del select
            categoriaSelect.setAttribute('name', categoriaFieldName);
            categoriaSelect.value = '';

            tipoUbicacionSelect.value = '';
        }, 0);
    });
});
