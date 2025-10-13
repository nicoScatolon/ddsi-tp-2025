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

/* ---------- Inicialización general ---------- */
document.addEventListener("DOMContentLoaded", () => {
    try {
        setupHorizontalScroll();
    } catch (err) {
        console.warn("setupHorizontalScroll falló:", err);
    }
});