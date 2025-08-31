document.addEventListener("DOMContentLoaded", () => {
    const ua = navigator.userAgent || navigator.vendor || window.opera;

    const desktopNav = document.querySelector(".desktop-nav");
    const mobileNav = document.querySelector(".mobile-nav");
    const zoomNav = document.querySelector(".zoom-nav");
    const contentSection = document.querySelector("home, .form-container, about, collections, login-section, signup-section, map, explore, hecho-detail");

    const isMobileUA = /Android|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(ua);

    function updateNav() {
        const isNarrow = window.innerWidth <= 768;

        if (isMobileUA) {
            // --- Mobile real ---
            desktopNav.style.display = "none";
            mobileNav.style.display = "flex";
            zoomNav.style.display = "none";
        } else {
            if (isNarrow) {
                // --- PC con ventana chica = Zoom navbar ---
                desktopNav.style.display = "none";
                mobileNav.style.display = "none";
                zoomNav.style.display = "flex";
            } else {
                // --- PC normal ---
                desktopNav.style.display = "flex";
                mobileNav.style.display = "none";
                zoomNav.style.display = "none";
            }
        }

        if (contentSection) {
            if (isMobileUA) contentSection.style.paddingTop = "5px";
            else if (isNarrow) contentSection.style.paddingTop = "10rem";
            else contentSection.style.paddingTop = "30px";
        }
    }

    // Run on load + resize
    updateNav();
    window.addEventListener("resize", updateNav);

    // Burger toggle
    const burgerBtn = document.getElementById("burgerBtn");
    const sidebar = document.getElementById("sidebar");
    if (burgerBtn && sidebar) {
        burgerBtn.addEventListener("click", () => {
            sidebar.classList.toggle("active");
        });
    }
});
