document.addEventListener('DOMContentLoaded', () => {

    // ===== ELEMENTOS DEL DOM =====
    const editToggleBtn = document.getElementById('editToggleBtn');
    const profileForm = document.getElementById('profileForm');
    const profileView = document.getElementById('profileView');
    const cancelEditBtn = document.getElementById('cancelEditBtn');

    const passwordModal = document.getElementById('passwordModal');
    const changePasswordBtn = document.getElementById('changePasswordBtn');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const cancelPasswordBtn = document.getElementById('cancelPasswordBtn');
    const changePasswordForm = document.getElementById('changePasswordForm');

    // ===== CAMPOS COMPATIBLES (ES/EN) =====
    const fieldIds = {
        nombre: document.getElementById('nombre') || document.getElementById('firstName'),
        apellido: document.getElementById('apellido') || document.getElementById('lastName'),
        email: document.getElementById('email'),
        fechaNacimiento: document.getElementById('fechaNacimiento') || document.getElementById('birthDate')
    };

    // ===== TOGGLE EDIT MODE =====
    if (editToggleBtn && profileForm && profileView) {

        const originalValues = {
            nombre: fieldIds.nombre?.value,
            apellido: fieldIds.apellido?.value,
            email: fieldIds.email?.value,
            fechaNacimiento: fieldIds.fechaNacimiento?.value
        };

        editToggleBtn.addEventListener('click', () => {
            const isEditing = profileForm.style.display !== 'none';

            if (isEditing) {
                profileForm.style.display = 'none';
                profileView.style.display = 'block';
                editToggleBtn.innerHTML = '<i class="fas fa-edit"></i><span>Editar</span>';
            } else {
                profileForm.style.display = 'block';
                profileView.style.display = 'none';
                editToggleBtn.innerHTML = '<i class="fas fa-eye"></i><span>Ver perfil</span>';
            }
        });

        // CANCELAR EDICIÓN
        cancelEditBtn?.addEventListener('click', () => {
            if (fieldIds.nombre) fieldIds.nombre.value = originalValues.nombre;
            if (fieldIds.apellido) fieldIds.apellido.value = originalValues.apellido;
            if (fieldIds.email) fieldIds.email.value = originalValues.email;
            if (fieldIds.fechaNacimiento) fieldIds.fechaNacimiento.value = originalValues.fechaNacimiento;

            profileForm.style.display = 'none';
            profileView.style.display = 'block';
            editToggleBtn.innerHTML = '<i class="fas fa-edit"></i><span>Editar</span>';
        });

        // Actualizar valores originales al guardar
        profileForm.addEventListener('submit', () => {
            setTimeout(() => {
                originalValues.nombre = fieldIds.nombre?.value;
                originalValues.apellido = fieldIds.apellido?.value;
                originalValues.email = fieldIds.email?.value;
                originalValues.fechaNacimiento = fieldIds.fechaNacimiento?.value;
            }, 100);
        });
    }

    // ===== TOGGLE PASSWORD VISIBILITY =====
    const togglePasswordButtons = document.querySelectorAll('.toggle-password');

    togglePasswordButtons.forEach(button => {
        button.addEventListener('click', () => {
            const targetId = button.getAttribute('data-target');
            const passwordInput = document.getElementById(targetId);
            const icon = button.querySelector('i');

            if (!passwordInput) return;

            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                passwordInput.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    });

    // ===== MODAL CAMBIAR CONTRASEÑA =====
    const openModal = () => passwordModal?.classList.add('active');
    const closeModal = () => {
        passwordModal?.classList.remove('active');
        clearPasswordForm();
        resetPasswordVisibility();
    };

    changePasswordBtn?.addEventListener('click', openModal);
    closeModalBtn?.addEventListener('click', closeModal);
    cancelPasswordBtn?.addEventListener('click', closeModal);

    passwordModal?.addEventListener('click', (e) => {
        if (e.target === passwordModal) closeModal();
    });

    // ===== FUNCIONES AUXILIARES =====
    function clearPasswordForm() {
        changePasswordForm?.reset();
    }

    function resetPasswordVisibility() {
        ['currentPassword', 'newPassword', 'confirmNewPassword'].forEach(id => {
            const input = document.getElementById(id);
            if (input) input.type = 'password';
        });

        togglePasswordButtons.forEach(button => {
            const icon = button.querySelector('i');
            if (icon) {
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    }
});
