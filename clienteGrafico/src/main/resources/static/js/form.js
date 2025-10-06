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

    // Botones / acciones
    const submitBtn = form.querySelector('button[type="submit"]');

    // Variable para el mapa de Leaflet
    let map = null;
    let marker = null;

    // Mostrar/ocultar campo de nueva categoría
    categoriaSelect.addEventListener('change', function() {
        if (this.value === 'other') {
            categoriaNuevaInput.style.display = 'block';
            categoriaNuevaInput.required = true;
        } else {
            categoriaNuevaInput.style.display = 'none';
            categoriaNuevaInput.required = false;
            categoriaNuevaInput.value = '';
        }
    });

    // Inicializar el mapa de Leaflet
    function initMap() {
        if (!map) {
            // Centrar el mapa en Argentina (Buenos Aires)
            map = L.map('mapContainer').setView([-34.6037, -58.3816], 13);

            // Agregar capa de OpenStreetMap
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
                maxZoom: 19
            }).addTo(map);

            // Evento de clic en el mapa
            map.on('click', function(e) {
                const lat = e.latlng.lat.toFixed(7);
                const lng = e.latlng.lng.toFixed(7);

                // Actualizar coordenadas
                latitudInput.value = lat;
                longitudInput.value = lng;

                // Mostrar coordenadas
                latDisplay.textContent = lat;
                lngDisplay.textContent = lng;
                coordenadasDisplay.style.display = 'block';

                // Agregar o mover el marcador
                if (marker) {
                    marker.setLatLng(e.latlng);
                } else {
                    marker = L.marker(e.latlng, {
                        draggable: true
                    }).addTo(map);

                    // Evento cuando se arrastra el marcador
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

        // Forzar actualización del tamaño del mapa
        setTimeout(() => {
            map.invalidateSize();
        }, 100);
    }

    // Manejar campos de ubicación según el tipo seleccionado
    tipoUbicacionSelect.addEventListener('change', function() {
        const tipo = this.value;

        // Resetear todos los campos
        resetUbicacionFields();

        if (tipo === 'direccion') {
            // Mostrar y requerir campos de dirección
            provinciaInput.required = true;
            localidadInput.required = true;
            calleInput.required = true;
            numeroInput.required = true;

            provinciaGroup.style.display = 'flex';
            localidadGroup.style.display = 'flex';
            calleGroup.style.display = 'flex';
            numeroGroup.style.display = 'flex';

        } else if (tipo === 'mapa') {
            // Mostrar el mapa
            mapaGroup.style.display = 'flex';
            latitudInput.required = true;
            longitudInput.required = true;

            // Inicializar el mapa
            initMap();
        }
    });

    // Función para resetear campos de ubicación
    function resetUbicacionFields() {
        // Ocultar todos los grupos
        provinciaGroup.style.display = 'none';
        localidadGroup.style.display = 'none';
        calleGroup.style.display = 'none';
        numeroGroup.style.display = 'none';
        mapaGroup.style.display = 'none';
        coordenadasDisplay.style.display = 'none';

        // Quitar requeridos
        provinciaInput.required = false;
        localidadInput.required = false;
        calleInput.required = false;
        numeroInput.required = false;
        latitudInput.required = false;
        longitudInput.required = false;

        // Limpiar valores
        provinciaInput.value = '';
        localidadInput.value = '';
        calleInput.value = '';
        numeroInput.value = '';
        latitudInput.value = '';
        longitudInput.value = '';

        // Limpiar marcador del mapa
        if (marker && map) {
            try { map.removeLayer(marker); } catch (err) { /* ignore */ }
            marker = null;
        }
    }

    // Ocultar todos los campos de ubicación al inicio
    resetUbicacionFields();

    // Manejar envío del formulario (AJAX con FormData)
    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        // Validar categoría "other"
        if (categoriaSelect.value === 'other' && !categoriaNuevaInput.value.trim()) {
            alert('Por favor escribí la nueva categoría.');
            categoriaNuevaInput.focus();
            return;
        }

        // Validar que si es tipo "mapa", se haya seleccionado una ubicación
        if (tipoUbicacionSelect.value === 'mapa' && (!latitudInput.value || !longitudInput.value)) {
            alert('Por favor, seleccione una ubicación en el mapa haciendo clic sobre él.');
            return;
        }

        // Desactivar botón para evitar envíos múltiples
        const originalBtnText = submitBtn ? submitBtn.textContent : null;
        if (submitBtn) {
            submitBtn.disabled = true;
            submitBtn.textContent = 'Enviando...';
        }

        // Tomar la acción del form (th:action en el HTML)
        const actionUrl = form.getAttribute('action') || '/hechos/create';
        // Construir FormData desde el form (incluye file input)
        const fd = new FormData(form);

        // Si la categoria es 'other' reemplazo el campo por la nueva categoria
        if (categoriaSelect.value === 'other') {
            fd.set('categoria', categoriaNuevaInput.value.trim());
        }

        // Asegurarse de enviar lat/lng si tipoUbicacion=mapa (ya están en campos hidden)
        // No setear Content-Type: fetch lo manejará automáticamente
        try {
            const response = await fetch(actionUrl, {
                method: 'POST',
                body: fd,
                credentials: 'same-origin' // útil si usás sesiones/csrf
            });

            if (response.ok) {
                // Suponemos que el backend creó el hecho correctamente.
                // Redirigimos al listado o detalle según tu flujo
                window.location.href = '/hechos/list';
                return;
            } else {
                // Intentar leer mensaje de error del cuerpo (si lo devuelve)
                let text = '';
                try { text = await response.text(); } catch (err) { /* ignore */ }
                console.error('Error al crear hecho:', response.status, text);
                alert('No se pudo crear el hecho. Estado: ' + response.status);
            }
        } catch (err) {
            console.error('Fetch error al crear hecho:', err);
            // Fallback: intentar enviar el form de manera tradicional
            const doFallback = confirm('No se pudo conectar con el servidor vía AJAX. Intentar envío tradicional (recarga de página)?');
            if (doFallback) {
                // quitar el preventDefault y enviar normalmente
                form.removeEventListener('submit', arguments.callee);
                form.submit();
                return;
            }
        } finally {
            if (submitBtn) {
                submitBtn.disabled = false;
                submitBtn.textContent = originalBtnText;
            }
        }
    });

    // Manejar botón cancelar / reset
    form.addEventListener('reset', function() {
        setTimeout(() => {
            resetUbicacionFields();
            categoriaNuevaInput.style.display = 'none';
            categoriaNuevaInput.required = false;
            tipoUbicacionSelect.value = '';
        }, 0);
    });
});
