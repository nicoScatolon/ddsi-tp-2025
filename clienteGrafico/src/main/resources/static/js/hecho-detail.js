// hecho-detail.js - Gestión de detalle de hechos con Leaflet (sin edición)

document.addEventListener('DOMContentLoaded', () => {
    // Verificar que tenemos datos del hecho
    if (typeof HECHO_DATA === 'undefined' || !HECHO_DATA.id) {
        console.warn('No hay datos del hecho disponibles');
        return;
    }

    // Referencias a elementos del DOM
    const mapEl = document.getElementById('hecho-map');

    // Variables para el mapa
    let map = null;
    let marker = null;

    // ===== INICIALIZAR MAPA CON LEAFLET =====
    const initMap = () => {
        if (!mapEl) return;

        const lat = HECHO_DATA.ubicacion.latitud;
        const lng = HECHO_DATA.ubicacion.longitud;

        if (lat && lng && !isNaN(lat) && !isNaN(lng)) {
            try {
                map = L.map('hecho-map').setView([lat, lng], 14);

                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
                    maxZoom: 19
                }).addTo(map);

                marker = L.marker([lat, lng]).addTo(map);

            } catch (error) {
                console.error('Error al inicializar el mapa:', error);
                mapEl.innerHTML = '<div class="placeholder">Error al cargar el mapa</div>';
            }
        } else {
            mapEl.innerHTML = '<div class="placeholder">No hay coordenadas disponibles para mostrar el mapa</div>';
        }
    };
    initMap();

    // ===== MODAL DE ELIMINACIÓN =====
    const setupDeleteModal = () => {
        const deleteBtn = document.getElementById('deleteBtn');
        const modal = document.getElementById('deleteModal');
        if (!deleteBtn || !modal) return;

        const closeBtn = modal.querySelector('.close');

        // Abrir modal al click
        deleteBtn.addEventListener('click', () => {
            modal.classList.add('open');
        });

        // Cerrar modal al click en la "X"
        if (closeBtn) {
            closeBtn.addEventListener('click', () => {
                modal.classList.remove('open');
            });
        }

        // Cerrar modal al hacer click fuera del contenido
        window.addEventListener('click', (e) => {
            if (e.target === modal) {
                modal.classList.remove('open');
            }
        });
    };

    // ===== DROPDOWN NAVBAR =====
    const setupDesktopDropdown = () => {
        const desktopUserBtn = document.getElementById('desktopUserBtn');
        const desktopUserDropdown = document.getElementById('desktopUserDropdown');
        if (!desktopUserBtn || !desktopUserDropdown) return;

        desktopUserBtn.addEventListener('click', e => {
            e.stopPropagation();
            const isVisible = desktopUserDropdown.style.display === 'block';
            desktopUserDropdown.style.display = isVisible ? 'none' : 'block';
        });

        document.addEventListener('click', () => {
            if (desktopUserDropdown.style.display === 'block') {
                desktopUserDropdown.style.display = 'none';
            }
        });
    };

    // ===== INICIALIZAR FUNCIONALIDADES =====
    if (USER_DATA?.loggedIn) {
        setupDeleteModal();
    }

    setupDesktopDropdown();

    console.log('Hecho cargado:', HECHO_DATA);
    console.log('Usuario:', USER_DATA);
});