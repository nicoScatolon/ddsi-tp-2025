document.addEventListener('DOMContentLoaded', () => {
    const userBtn = document.getElementById('desktopUserBtn');
    const userDropdown = document.getElementById('desktopUserDropdown');

    // Inicialmente ocultamos el dropdown
    if(userDropdown) userDropdown.style.display = 'none';

    if(userBtn && userDropdown) {
        userBtn.addEventListener('click', (e) => {
            e.stopPropagation(); // Evita que el click se propague al body
            if(userDropdown.style.display === 'none') {
                userDropdown.style.display = 'block';
            } else {
                userDropdown.style.display = 'none';
            }
        });

        // Cerrar el dropdown al hacer click fuera
        document.addEventListener('click', () => {
            if(userDropdown.style.display === 'block') {
                userDropdown.style.display = 'none';
            }
        });
    }
});