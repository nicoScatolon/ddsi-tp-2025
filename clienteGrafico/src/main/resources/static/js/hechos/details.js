// details.js - Gestión de detalle de hechos con Carrusel Multimedia y Etiquetas Simplificadas

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

    // ===== MODAL DE ETIQUETAS SIMPLIFICADO =====
    const modalEtiquetas = document.getElementById('modalEtiquetas');
    const btnModificarEtiquetas = document.getElementById('btnModificarEtiquetas');
    const btnCerrarModalEtiquetas = document.getElementById('btnCerrarModalEtiquetas');
    const btnCancelarEtiquetas = document.getElementById('btnCancelarEtiquetas');
    const btnGuardarEtiquetas = document.getElementById('btnGuardarEtiquetas');
    const inputEtiqueta = document.getElementById('inputEtiqueta');
    const etiquetasSugerencias = document.getElementById('etiquetasSugerencias');
    const listaEtiquetasSeleccionadasHecho = document.getElementById('listaEtiquetasSeleccionadasHecho');
    const etiquetasSeleccionadasContainerHecho = document.getElementById('etiquetasSeleccionadasHecho');

    // Array para almacenar etiquetas seleccionadas
    let etiquetasSeleccionadas = [];

    // Obtener etiquetas disponibles del servidor
    const etiquetasDisponibles = typeof ETIQUETAS_DISPONIBLES !== 'undefined' ? ETIQUETAS_DISPONIBLES : [];

    // Inicializar etiquetas actuales del hecho
    if (HECHO_DATA.etiquetasActuales && Array.isArray(HECHO_DATA.etiquetasActuales)) {
        etiquetasSeleccionadas = HECHO_DATA.etiquetasActuales.map(etiq => etiq.nombre).filter(Boolean);
    }

    // Función para normalizar texto (para búsqueda)
    function normalizarTexto(texto) {
        return texto.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '').trim();
    }

    // Función para mostrar sugerencias
    function mostrarSugerencias(valor) {
        if (!valor || valor.length < 1) {
            etiquetasSugerencias.classList.add('hidden');
            return;
        }

        const valorNormalizado = normalizarTexto(valor);

        // Filtrar etiquetas disponibles que no estén ya seleccionadas
        const sugerencias = etiquetasDisponibles.filter(etiq => {
            const etiqNormalizada = normalizarTexto(etiq);
            return etiqNormalizada.includes(valorNormalizado) &&
                !etiquetasSeleccionadas.includes(etiq);
        });

        if (sugerencias.length === 0) {
            etiquetasSugerencias.innerHTML = `
                <div class="sugerencia-item nueva">
                    <i class="fas fa-plus-circle"></i>
                    <span>Crear nueva etiqueta: <strong>"${valor}"</strong></span>
                    <small>Presiona Enter</small>
                </div>
            `;
            etiquetasSugerencias.classList.remove('hidden');
        } else {
            etiquetasSugerencias.innerHTML = sugerencias.map(etiq => `
                <div class="sugerencia-item" data-etiqueta="${etiq}">
                    <i class="fas fa-tag"></i>
                    <span>${etiq}</span>
                </div>
            `).join('');
            etiquetasSugerencias.classList.remove('hidden');

            // Agregar eventos a las sugerencias
            etiquetasSugerencias.querySelectorAll('.sugerencia-item:not(.nueva)').forEach(item => {
                item.addEventListener('click', () => {
                    agregarEtiqueta(item.dataset.etiqueta);
                });
            });
        }
    }

    // Función para agregar etiqueta
    function agregarEtiqueta(etiqueta) {
        const etiquetaLimpia = etiqueta.trim();

        if (!etiquetaLimpia) {
            return;
        }

        if (etiquetasSeleccionadas.includes(etiquetaLimpia)) {
            inputEtiqueta.value = '';
            etiquetasSugerencias.classList.add('hidden');
            return;
        }

        etiquetasSeleccionadas.push(etiquetaLimpia);
        actualizarVistaEtiquetasHecho();

        // Limpiar input y ocultar sugerencias
        inputEtiqueta.value = '';
        etiquetasSugerencias.classList.add('hidden');
        inputEtiqueta.focus();
    }

    // Eventos del input de etiquetas
    if (inputEtiqueta) {
        // Buscar mientras escribe
        inputEtiqueta.addEventListener('input', (e) => {
            mostrarSugerencias(e.target.value);
        });

        // Agregar con Enter
        inputEtiqueta.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                const valor = inputEtiqueta.value.trim();
                if (valor) {
                    agregarEtiqueta(valor);
                }
            } else if (e.key === 'Escape') {
                etiquetasSugerencias.classList.add('hidden');
                inputEtiqueta.blur();
            }
        });

        // Ocultar sugerencias al perder foco (con delay para permitir click)
        inputEtiqueta.addEventListener('blur', () => {
            setTimeout(() => {
                etiquetasSugerencias.classList.add('hidden');
            }, 200);
        });

        // Mostrar sugerencias al hacer foco
        inputEtiqueta.addEventListener('focus', (e) => {
            if (e.target.value) {
                mostrarSugerencias(e.target.value);
            }
        });
    }

    // Abrir modal de etiquetas
    if (btnModificarEtiquetas) {
        btnModificarEtiquetas.addEventListener('click', () => {
            // Resetear etiquetas seleccionadas a las actuales
            if (HECHO_DATA.etiquetasActuales && Array.isArray(HECHO_DATA.etiquetasActuales)) {
                etiquetasSeleccionadas = HECHO_DATA.etiquetasActuales.map(etiq => etiq.nombre).filter(Boolean);
            } else {
                etiquetasSeleccionadas = [];
            }

            // Limpiar input
            if (inputEtiqueta) inputEtiqueta.value = '';

            // Ocultar sugerencias
            if (etiquetasSugerencias) etiquetasSugerencias.classList.add('hidden');

            // Actualizar vista
            actualizarVistaEtiquetasHecho();

            // Abrir modal
            abrirModalEtiquetas();

            // Enfocar input
            setTimeout(() => inputEtiqueta.focus(), 100);
        });
    }

    // Actualizar la vista de etiquetas seleccionadas
    function actualizarVistaEtiquetasHecho() {
        if (!listaEtiquetasSeleccionadasHecho) return;

        listaEtiquetasSeleccionadasHecho.innerHTML = '';

        if (etiquetasSeleccionadas.length === 0) {
            etiquetasSeleccionadasContainerHecho.classList.add('hidden');
            return;
        }

        etiquetasSeleccionadasContainerHecho.classList.remove('hidden');

        etiquetasSeleccionadas.forEach(etiqueta => {
            const pill = document.createElement('div');
            pill.className = 'etiqueta-pill-hecho';
            pill.innerHTML = `
                <span>${etiqueta}</span>
                <button type="button" data-etiqueta="${etiqueta}" title="Eliminar">
                    <i class="fas fa-times"></i>
                </button>
            `;
            listaEtiquetasSeleccionadasHecho.appendChild(pill);

            // Evento para eliminar etiqueta
            pill.querySelector('button').addEventListener('click', (e) => {
                const etiquetaAEliminar = e.currentTarget.getAttribute('data-etiqueta');
                etiquetasSeleccionadas = etiquetasSeleccionadas.filter(et => et !== etiquetaAEliminar);
                actualizarVistaEtiquetasHecho();
            });
        });
    }

    // Guardar etiquetas
    if (btnGuardarEtiquetas) {
        btnGuardarEtiquetas.addEventListener('click', async () => {
            try {
                const response = await fetch(`/hechos/${HECHO_DATA.id}/modificarEtiquetas`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [CSRF_HEADER]: CSRF_TOKEN
                    },
                    body: JSON.stringify(etiquetasSeleccionadas)
                });

                if (response.ok) {
                    // Recargar la página para mostrar los cambios
                    window.location.reload();
                } else {
                    alert('Error al guardar las etiquetas. Por favor, intenta nuevamente.');
                }
            } catch (error) {
                console.error('Error:', error);
                alert('Error al guardar las etiquetas. Por favor, intenta nuevamente.');
            }
        });
    }

    // Cerrar modal de etiquetas
    function cerrarModalEtiquetas() {
        if (modalEtiquetas) {
            modalEtiquetas.classList.remove('open');
            document.body.style.overflow = '';
        }
    }

    function abrirModalEtiquetas() {
        if (modalEtiquetas) {
            modalEtiquetas.classList.add('open');
            document.body.style.overflow = 'hidden';
        }
    }

    if (btnCerrarModalEtiquetas) {
        btnCerrarModalEtiquetas.addEventListener('click', cerrarModalEtiquetas);
    }

    if (btnCancelarEtiquetas) {
        btnCancelarEtiquetas.addEventListener('click', cerrarModalEtiquetas);
    }

    // Cerrar al hacer clic fuera
    if (modalEtiquetas) {
        modalEtiquetas.addEventListener('click', (e) => {
            if (e.target === modalEtiquetas) {
                cerrarModalEtiquetas();
            }
        });
    }

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
            if (e.key === 'Escape') {
                if (modal.classList.contains('open')) {
                    modal.classList.remove('open');
                }
                if (modalEtiquetas && modalEtiquetas.classList.contains('open')) {
                    cerrarModalEtiquetas();
                }
            }
        });
    };

    // ===== INICIALIZAR FUNCIONALIDADES =====
    console.log('🚀 Inicializando details.js con carrusel y etiquetas simplificadas');

    initCarousel();
    initMap();
    setupDeleteModal();

    console.log('📊 Hecho cargado:', HECHO_DATA);
    console.log('👤 Usuario:', USER_DATA);
});