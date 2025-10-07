/* ---------- Helpers ---------- */
function headerHeight() {
    const hdr = document.querySelector("header.desktop-nav") || document.querySelector("header");
    return hdr ? hdr.offsetHeight : 0;
}
function calcTargetY(el) {
    const rect = el.getBoundingClientRect();
    const scrollTop = window.scrollY || window.pageYOffset;
    const hdr = headerHeight();
    const extraOffset = -50;
    return Math.max(0, Math.round(scrollTop + rect.top - hdr - extraOffset));
}

/* ---------- Horizontal scroll (buttons + teclado) ---------- */
function setupHorizontalScroll() {
    // botones
    document.querySelectorAll(".scroll-btn").forEach(btn => {
        const targetId = btn.dataset.target;
        const target = document.getElementById(targetId);
        if (!target) return;
        btn.addEventListener("click", (ev) => {
            ev.preventDefault();
            const scrollAmount = 300;
            const dir = btn.classList.contains("left") ? -1 : 1;
            target.scrollBy({ left: scrollAmount * dir, behavior: "smooth" });
        });
    });

    // detectar contenedor activo al pasar el mouse (para flechas)
    let activeContainer = null;
    document.querySelectorAll(".cards-row").forEach(container => {
        container.addEventListener("mouseenter", () => activeContainer = container);
        container.addEventListener("mouseleave", () => activeContainer = null);
    });

    document.addEventListener("keydown", (e) => {
        if (!activeContainer) return;
        if (e.key !== "ArrowLeft" && e.key !== "ArrowRight") return;
        const amount = e.key === "ArrowLeft" ? -300 : 300;
        activeContainer.scrollBy({ left: amount, behavior: "smooth" });
    });
}

/* ---------- Vertical indicator + click (robusto) ---------- */
function setupVerticalIndicatorAndScroll() {
    const indicator = document.getElementById("scrollIndicator");
    const lower = document.getElementById("lower-content");
    const home = document.getElementById("home");

    if (!indicator || !lower || !home) {
        // si faltara algo, salimos silenciosamente
        return;
    }

    // Estado
    let scrolledDown = false;

    // Actualiza icono y posición del indicador; agrega/remueve clase 'up'
    function updateIndicator() {
        const currentScroll = window.scrollY || window.pageYOffset;
        const hdr = headerHeight();

        // Usamos getBoundingClientRect para una comparación robusta.
        const lowerRect = lower.getBoundingClientRect();
        // Si la parte superior de lower llegó a estar cerca del header => estamos "abajo"
        const threshold = hdr + 6; // margen
        const nowDown = (lowerRect.top <= threshold);

        scrolledDown = nowDown;

        if (scrolledDown) {
            indicator.classList.add("up");
            indicator.setAttribute("aria-label", "Volver arriba");
            indicator.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true"><polyline points="18 15 12 9 6 15"/></svg>`;
            // Colocamos el indicador justo debajo del header (evita solapamientos)
            indicator.style.top = (hdr + 8) + "px";
            indicator.style.bottom = "auto";
        } else {
            indicator.classList.remove("up");
            indicator.setAttribute("aria-label", "Ir al contenido");
            indicator.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true"><polyline points="6 9 12 15 18 9"/></svg>`;
            indicator.style.bottom = "30px";
            indicator.style.top = "auto";
        }
    }

    // Click / teclado (Enter / Space) sobre indicador
    function activateIndicatorAction(e) {
        // permitir Enter o Space o click
        if (e.type === "keydown" && e.key !== "Enter" && e.key !== " ") return;
        e.preventDefault();

        if (!scrolledDown) {
            const targetY = calcTargetY(lower);
            window.scrollTo({ top: targetY, behavior: "smooth" });
        } else {
            window.scrollTo({ top: 0, behavior: "smooth" });
        }
    }

    indicator.addEventListener("click", activateIndicatorAction);
    indicator.addEventListener("keydown", activateIndicatorAction);

    // IntersectionObserver para detectar entrada de la sección inferior de forma eficiente
    let io;
    try {
        io = new IntersectionObserver((entries) => {
            // observamos la entrada de la parte superior de 'lower' con rootMargin para compensar header
            entries.forEach(entry => {
                // cuando la parte superior alcanza el header, entry.boundingClientRect.top será <= headerHeight
                updateIndicator(); // recalc simple (lo hace robusto)
            });
        }, {
            root: null,
            threshold: [0, 0.01],
            rootMargin: `-${headerHeight()}px 0px 0px 0px`
        });
        io.observe(lower);
    } catch (err) {
        // fallbacks: si no hay soporte para IO, nos apoyamos en scroll/resize
    }

    // Recalcular en resize/load/scroll
    let rafId = null;
    function onScrollOrResize() {
        if (rafId) cancelAnimationFrame(rafId);
        rafId = requestAnimationFrame(() => updateIndicator());
    }
    window.addEventListener("resize", onScrollOrResize, { passive: true });
    window.addEventListener("load", updateIndicator);
    window.addEventListener("scroll", onScrollOrResize, { passive: true });

    // primer run
    updateIndicator();
}

/* ---------- Inicialización general ---------- */
document.addEventListener("DOMContentLoaded", () => {
    try {
        setupHorizontalScroll();
    } catch (err) {
        console.warn("setupHorizontalScroll falló:", err);
    }
    try {
        setupVerticalIndicatorAndScroll();
    } catch (err) {
        console.warn("setupVerticalIndicatorAndScroll falló:", err);
    }
});
