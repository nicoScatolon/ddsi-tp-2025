// hecho-detail.js - Gestión de detalle de hechos con Carrusel Multimedia

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

    // ===== CARRUSEL MULTIMEDIA =====
    const initCarousel = () => {
        const track = document.querySelector('.carousel-track');
        const prevBtn = document.querySelector('.carousel-btn.prev');
        const nextBtn = document.querySelector('.carousel-btn.next');
        const indicators = document.querySelectorAll('.indicator');
        const currentCounter = document.querySelector('.carousel-counter .current');
        const items = document.querySelectorAll('.carousel-item');

        if (!track || items.length === 0) {
            console.log('No hay carrusel o items para mostrar');
            return;
        }

        const totalItems = items.length;
        let currentIndex = 0;

        // Asegurar que todas las imágenes se carguen
        const images = track.querySelectorAll('img');
        images.forEach(img => {
            if (img.loading === 'lazy') {
                img.loading = 'eager';
            }
        });

        const updateCarousel = () => {
            // Mover el track
            track.style.transform = `translateX(-${currentIndex * 100}%)`;

            // Actualizar indicadores
            indicators.forEach((ind, idx) => {
                ind.classList.toggle('active', idx === currentIndex);
            });

            // Actualizar contador
            if (currentCounter) {
                currentCounter.textContent = currentIndex + 1;
            }

            // Pausar videos cuando no están visibles
            const videos = track.querySelectorAll('video');
            videos.forEach((video, idx) => {
                if (idx !== currentIndex) {
                    video.pause();
                }
            });
        };

        const goToSlide = (index) => {
            currentIndex = ((index % totalItems) + totalItems) % totalItems;
            updateCarousel();
        };

        // Botones prev/next
        if (prevBtn) {
            prevBtn.addEventListener('click', () => goToSlide(currentIndex - 1));
        }

        if (nextBtn) {
            nextBtn.addEventListener('click', () => goToSlide(currentIndex + 1));
        }

        // Click en indicadores
        indicators.forEach((indicator) => {
            indicator.addEventListener('click', () => {
                const index = parseInt(indicator.dataset.index);
                goToSlide(index);
            });
        });

        // Teclado
        document.addEventListener('keydown', (e) => {
            if (e.key === 'ArrowLeft') {
                goToSlide(currentIndex - 1);
            } else if (e.key === 'ArrowRight') {
                goToSlide(currentIndex + 1);
            }
        });

        // Swipe táctil
        let touchStartX = 0;
        let touchEndX = 0;

        track.addEventListener('touchstart', (e) => {
            touchStartX = e.changedTouches[0].screenX;
        }, { passive: true });

        track.addEventListener('touchend', (e) => {
            touchEndX = e.changedTouches[0].screenX;
            handleSwipe();
        }, { passive: true });

        const handleSwipe = () => {
            const swipeThreshold = 50;
            const diff = touchStartX - touchEndX;

            if (Math.abs(diff) > swipeThreshold) {
                if (diff > 0) {
                    goToSlide(currentIndex + 1);
                } else {
                    goToSlide(currentIndex - 1);
                }
            }
        };

        // Inicializar
        updateCarousel();
        console.log(`✅ Carrusel inicializado con ${totalItems} items`);
    };

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
                mapEl.innerHTML = '<div class="placeholder"><i class="fas fa-map-marked-alt"></i><p>Error al cargar el mapa</p></div>';
            }
        } else {
            mapEl.innerHTML = '<div class="placeholder"><i class="fas fa-map-marked-alt"></i><p>No hay coordenadas disponibles para mostrar el mapa</p></div>';
        }
    };

    // ===== MODAL DE ELIMINACIÓN =====
    const setupDeleteModal = () => {
        const deleteBtn = document.getElementById('deleteBtn');
        const modal = document.getElementById('deleteModal');

        if (!deleteBtn || !modal) {
            console.log('Botón o modal no encontrado');
            return;
        }

        const closeBtn = modal.querySelector('.close');
        const modalContent = modal.querySelector('.modal-content');

        deleteBtn.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();
            modal.classList.add('open');
        });

        if (closeBtn) {
            closeBtn.addEventListener('click', (e) => {
                e.preventDefault();
                e.stopPropagation();
                modal.classList.remove('open');
            });
        }

        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                modal.classList.remove('open');
            }
        });

        if (modalContent) {
            modalContent.addEventListener('click', (e) => {
                e.stopPropagation();
            });
        }

        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && modal.classList.contains('open')) {
                modal.classList.remove('open');
            }
        });
    };

    // ===== INICIALIZAR FUNCIONALIDADES =====
    console.log('🚀 Inicializando hecho-detail.js con carrusel');

    initCarousel();
    initMap();
    setupDeleteModal();
    console.log(document.querySelectorAll('.carousel-item').length);
    console.log('📊 Hecho cargado:', HECHO_DATA);
    console.log('👤 Usuario:', USER_DATA);
});