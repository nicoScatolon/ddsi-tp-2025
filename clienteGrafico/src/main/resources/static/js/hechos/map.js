// =========================
// MAPA DE HECHOS - JavaScript
// =========================

document.addEventListener('DOMContentLoaded', () => {
    // Inicializar mapa centrado en Argentina
    const map = L.map('map', {
        minZoom: 4,
        maxZoom: 18
    }).setView([-38.0, -63.5], 5);

    // Tile layer
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 18
    }).addTo(map);

    // Cluster de marcadores
    const markers = L.markerClusterGroup({
        maxClusterRadius: 50,
        spiderfyOnMaxZoom: true,
        showCoverageOnHover: false,
        disableClusteringAtZoom: 15
    });
    map.addLayer(markers);

    // =========================
    // Variables de control
    // =========================
    let provinciaActual = null;

    // =========================
    // Elementos DOM
    // =========================
    const provinciaSelect = document.getElementById('provinciaSelect');
    const cargarProvinciaBtn = document.getElementById('cargarProvinciaBtn');
    const cargarTodosBtn = document.getElementById('cargarTodosBtn');
    const loadingIndicator = document.getElementById('loadingIndicator');
    const loadingText = document.getElementById('loadingText');
    const statsInfo = document.getElementById('statsInfo');
    const warningBox = document.getElementById('warningBox');
    const statProvincia = document.getElementById('statProvincia');
    const statHechos = document.getElementById('statHechos');

    // =========================
    // Event Listeners
    // =========================

    // Habilitar botón cuando se selecciona provincia
    provinciaSelect.addEventListener('change', (e) => {
        cargarProvinciaBtn.disabled = !e.target.value;
        if (e.target.value) {
            cargarProvinciaBtn.classList.add('active');
        } else {
            cargarProvinciaBtn.classList.remove('active');
        }
    });

    // Cargar provincia al hacer clic
    cargarProvinciaBtn.addEventListener('click', () => {
        const provincia = provinciaSelect.value;
        if (provincia) {
            cargarProvincia(provincia);
        }
    });

    // Cargar todas las provincias con confirmación
    cargarTodosBtn.addEventListener('click', () => {
        const confirmacion = confirm(
            '¿Estás seguro de que quieres cargar TODOS los hechos?\n\n' +
            'Esto puede tardar varios segundos y consumir más datos.\n' +
            'Se recomienda cargar por provincia.'
        );

        if (confirmacion) {
            cargarTodosLosHechos();
        }
    });

    // Enter en el select para cargar
    provinciaSelect.addEventListener('keypress', (e) => {
        if (e.key === 'Enter' && provinciaSelect.value) {
            cargarProvincia(provinciaSelect.value);
        }
    });

    // =========================
    // Funciones de UI
    // =========================

    /**
     * Mostrar/ocultar indicador de carga
     */
    function setLoading(show, text = 'Cargando...') {
        if (show) {
            loadingIndicator.classList.remove('hidden');
            loadingText.textContent = text;
        } else {
            loadingIndicator.classList.add('hidden');
        }
    }

    /**
     * Actualizar estadísticas
     */
    function updateStats(provincia, cantidad) {
        statProvincia.textContent = provincia;
        statHechos.textContent = cantidad;
        statsInfo.classList.remove('hidden');
    }

    // =========================
    // Funciones del Mapa
    // =========================

    /**
     * Agregar marcadores al mapa
     */
    function agregarMarcadores(hechos) {
        markers.clearLayers();

        hechos.forEach(h => {
            if (!h.latitud || !h.longitud) return;

            const popupContent = `
                <div style="max-width: 200px;">
                    <strong>${h.titulo}</strong><br>
                    <p style="margin: 4px 0; color: #6b7280; font-size: 0.9rem;">${h.categoria || ''}</p>
                    <a href="/hechos/${h.id}" class="btn-popup">Ver más</a>
                </div>
            `;

            const marker = L.marker([h.latitud, h.longitud]);
            marker.bindPopup(popupContent);
            markers.addLayer(marker);
        });
    }

    /**
     * Centrar y limitar mapa a provincia
     */
    function centrarEnProvincia(provincia) {
        const coords = PROVINCIAS[provincia];
        if (!coords) return;

        // Centrar en la provincia
        map.setView([coords.lat, coords.lng], 8);

        // Establecer límites amplios para incluir provincias limítrofes
        const radio = 6; // grados - permite ver provincias vecinas completas
        const limites = L.latLngBounds(
            L.latLng(coords.lat - radio, coords.lng - radio),
            L.latLng(coords.lat + radio, coords.lng + radio)
        );

        // Configurar límites con bounce back muy suave
        map.setMaxBounds(limites);
        map.options.maxBoundsViscosity = 0.3; // Resistencia muy suave
    }

    /**
     * Restablecer vista general de Argentina
     */
    function restablecerVista() {
        map.setMaxBounds(null);
        map.setView([-38.0, -63.5], 5);
    }

    // =========================
    // Funciones de Carga de Datos
    // =========================

    /**
     * Cargar hechos de una provincia
     */
    async function cargarProvincia(provincia) {
        setLoading(true, `Cargando ${provincia}...`);

        try {
            const response = await fetch(`/hechos/mapa-por-provincia?provincia=${encodeURIComponent(provincia)}`);

            if (!response.ok) {
                throw new Error(`Error HTTP: ${response.status}`);
            }

            const hechos = await response.json();
            agregarMarcadores(hechos);
            centrarEnProvincia(provincia);
            updateStats(provincia, hechos.length);
            provinciaActual = provincia;

            console.log(`✅ ${provincia}: ${hechos.length} hechos cargados`);

        } catch (error) {
            console.error('❌ Error cargando provincia:', error);
            alert(`Error al cargar los datos de ${provincia}. Por favor, intenta nuevamente.`);
        } finally {
            setLoading(false);
        }
    }

    /**
     * Cargar todos los hechos de una sola vez
     */
    async function cargarTodosLosHechos() {
        // Mostrar advertencia
        warningBox.classList.remove('hidden');
        setLoading(true, 'Cargando todos los hechos...');

        // Restablecer vista
        restablecerVista();

        try {
            const response = await fetch('/hechos/map-all');

            if (!response.ok) {
                throw new Error(`Error HTTP: ${response.status}`);
            }

            const hechos = await response.json();
            agregarMarcadores(hechos);
            updateStats('Todas las provincias', hechos.length);
            provinciaActual = 'Todas';

            console.log(`✅ Total: ${hechos.length} hechos cargados`);

        } catch (error) {
            console.error('❌ Error cargando todos los hechos:', error);
            alert('Error al cargar todos los datos. Por favor, intenta nuevamente.');
        } finally {
            setLoading(false);
            // Ocultar advertencia después de 3 segundos
            setTimeout(() => warningBox.classList.add('hidden'), 3000);
        }
    }

    // =========================
    // Inicialización
    // =========================
    console.log('🗺️ Mapa inicializado. Selecciona una provincia para comenzar.');
});