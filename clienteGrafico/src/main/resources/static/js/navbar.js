document.addEventListener('DOMContentLoaded', () => {
    // ===== DROPDOWN PARA USUARIO AUTENTICADO =====
    const userBtn = document.getElementById('desktopUserBtn');
    const userDropdown = document.getElementById('desktopUserDropdown');
    const body = document.body;

    if(userDropdown) userDropdown.style.display = 'none';

    if(userBtn && userDropdown) {
        userBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            // Cerrar el dropdown de login si está abierto
            const loginDropdown = document.getElementById('desktopLoginDropdown');
            if(loginDropdown) loginDropdown.style.display = 'none';

            // Toggle del dropdown de usuario
            if(userDropdown.style.display === 'none') {
                userDropdown.style.display = 'block';
                userBtn.setAttribute('aria-expanded', 'true');
            } else {
                userDropdown.style.display = 'none';
                userBtn.setAttribute('aria-expanded', 'false');
            }
        });
    }

    // ===== DROPDOWN PARA LOGIN (NO AUTENTICADO) =====
    const loginBtn = document.getElementById('desktopLoginBtn');
    const loginDropdown = document.getElementById('desktopLoginDropdown');

    if(loginDropdown) loginDropdown.style.display = 'none';

    if(loginBtn && loginDropdown) {
        loginBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            // Cerrar el dropdown de usuario si está abierto
            if(userDropdown) userDropdown.style.display = 'none';

            // Toggle del dropdown de login
            if(loginDropdown.style.display === 'none') {
                loginDropdown.style.display = 'block';
                loginBtn.setAttribute('aria-expanded', 'true');
            } else {
                loginDropdown.style.display = 'none';
                loginBtn.setAttribute('aria-expanded', 'false');
            }
        });
    }

    // ===== CERRAR DROPDOWNS AL HACER CLICK FUERA =====
    document.addEventListener('click', () => {
        if(userDropdown && userDropdown.style.display === 'block') {
            userDropdown.style.display = 'none';
            if(userBtn) userBtn.setAttribute('aria-expanded', 'false');
        }
        if(loginDropdown && loginDropdown.style.display === 'block') {
            loginDropdown.style.display = 'none';
            if(loginBtn) loginBtn.setAttribute('aria-expanded', 'false');
        }
    });

    // ===== PREVENIR CIERRE AL HACER CLICK DENTRO DEL DROPDOWN =====
    if(userDropdown) {
        userDropdown.addEventListener('click', (e) => {
            e.stopPropagation();
        });
    }

    if(loginDropdown) {
        loginDropdown.addEventListener('click', (e) => {
            e.stopPropagation();
        });
    }

    const navToggle = document.getElementById('navToggle');
    const navLinks = document.getElementById('primaryNav');

    function closeMobileMenu() {
        if (!navLinks || !navToggle) return;
        navLinks.classList.remove('is-open');
        navToggle.classList.remove('is-open');
        navToggle.setAttribute('aria-expanded', 'false');
        body.classList.remove('mobile-menu-open');
    }

    if (navToggle && navLinks) {
        navToggle.addEventListener('click', function (e) {
            e.stopPropagation();
            const isOpen = navLinks.classList.toggle('is-open');
            navToggle.classList.toggle('is-open', isOpen);
            navToggle.setAttribute('aria-expanded', String(isOpen));
            body.classList.toggle('mobile-menu-open', isOpen);
        });

        // cerrar al hacer click en un link
        navLinks.querySelectorAll('a').forEach(function (link) {
            link.addEventListener('click', function () {
                closeMobileMenu();
            });
        });
    }
});