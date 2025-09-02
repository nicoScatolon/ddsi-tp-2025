function setupScroll() {
    const buttons = document.querySelectorAll(".scroll-btn");
    buttons.forEach(btn => {
        const targetId = btn.dataset.target;
        const target = document.getElementById(targetId);

        btn.addEventListener("click", () => {
            const scrollAmount = 300;
            if (btn.classList.contains("left")) {
                target.scrollBy({ left: -scrollAmount, behavior: "smooth" });
            } else {
                target.scrollBy({ left: scrollAmount, behavior: "smooth" });
            }
        });
    });

    // navegación con teclado
    document.addEventListener("keydown", e => {
        if (e.key === "ArrowLeft" || e.key === "ArrowRight") {
            const active = document.querySelector(".cards-row:hover");
            if (!active) return;
            const amount = e.key === "ArrowLeft" ? -300 : 300;
            active.scrollBy({ left: amount, behavior: "smooth" });
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    setupScroll();

    const scrollIndicator = document.getElementById("scrollIndicator");
    const homeSection = document.getElementById("home");

    let scrolledDown = false;

    function updateIndicator() {
        if (window.scrollY > homeSection.offsetHeight - 10) {
            // flecha hacia arriba
            scrollIndicator.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                <polyline points="18 15 12 9 6 15" /></svg>`;
            scrolledDown = true;
            // mover arriba
            scrollIndicator.style.top = "90px";
            scrollIndicator.style.bottom = "auto";
        } else {
            // flecha hacia abajo
            scrollIndicator.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                <polyline points="6 9 12 15 18 9" /></svg>`;
            scrolledDown = false;
            // posición inicial abajo
            scrollIndicator.style.bottom = "30px";
            scrollIndicator.style.top = "auto";
        }
    }

    // inicial
    updateIndicator();

    scrollIndicator.addEventListener("click", () => {
        if (!scrolledDown) {
            window.scrollTo({ top: homeSection.offsetHeight, behavior: "smooth" });
        } else {
            window.scrollTo({ top: 0, behavior: "smooth" });
        }
    });

    window.addEventListener("scroll", updateIndicator);
});
