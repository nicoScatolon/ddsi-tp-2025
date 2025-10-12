/* ---------- Helpers ---------- */
function headerHeight() {
    const hdr = document.querySelector("header.desktop-nav") || document.querySelector("header");
    return hdr ? hdr.offsetHeight : 0;
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

/* ---------- Vertical indicator + click ---------- */
function setupVerticalIndicatorAndScroll() {
    const indicator = document.getElementById("scrollIndicator");
    const lower = document.getElementById("lower-content");
    const home = document.getElementById("home");

    if (!indicator || !lower || !home) {
        return;
    }

    let scrolledDown = false;

    function updateIndicator() {
        const currentScroll = window.scrollY || window.pageYOffset;
        const hdr = headerHeight();
        const lowerRect = lower.getBoundingClientRect();
        const threshold = hdr + 6;
        const nowDown = (lowerRect.top <= threshold);

        scrolledDown = nowDown;

        if (scrolledDown) {
            indicator.classList.add("up");
            indicator.setAttribute("aria-label", "Volver arriba");
            indicator.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true"><polyline points="18 15 12 9 6 15"/></svg>`;
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

    function activateIndicatorAction(e) {
        if (e.type === "keydown" && e.key !== "Enter" && e.key !== " ") return;
        e.preventDefault();

        if (!scrolledDown) {
            const lowerRect = lower.getBoundingClientRect();
            const scrollTop = window.scrollY || window.pageYOffset;
            const hdr = headerHeight();
            const targetY = Math.max(0, Math.round(scrollTop + lowerRect.top - hdr - 50));
            window.scrollTo({ top: targetY, behavior: "smooth" });
        } else {
            window.scrollTo({ top: 0, behavior: "smooth" });
        }
    }

    indicator.addEventListener("click", activateIndicatorAction);
    indicator.addEventListener("keydown", activateIndicatorAction);

    let rafId = null;
    function onScrollOrResize() {
        if (rafId) cancelAnimationFrame(rafId);
        rafId = requestAnimationFrame(() => updateIndicator());
    }

    window.addEventListener("resize", onScrollOrResize, { passive: true });
    window.addEventListener("load", updateIndicator);
    window.addEventListener("scroll", onScrollOrResize, { passive: true });

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