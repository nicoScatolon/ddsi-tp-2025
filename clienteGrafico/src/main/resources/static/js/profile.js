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

    // ===== TOGGLE EDIT MODE =====
    if (editToggleBtn && profileForm && profileView) {
        // Guardar valores originales
        const originalValues = {
            firstName: document.getElementById('firstName')?.value,
            lastName: document.getElementById('lastName')?.value,
            email: document.getElementById('email')?.value,
            birthDate: document.getElementById('birthDate')?.value
        };

        editToggleBtn.addEventListener('click', () => {
            const isEditing = profileForm.style.display !== 'none';

            if (isEditing) {
                // Cambiar a modo visualización
                profileForm.style.display = 'none';
                profileView.style.display = 'block';
                editToggleBtn.innerHTML = '<i class="fas fa-edit"></i><span>Editar</span>';
            } else {
                // Cambiar a modo edición
                profileForm.style.display = 'block';
                profileView.style.display = 'none';
                editToggleBtn.innerHTML = '<i class="fas fa-eye"></i><span>Ver perfil</span>';
            }
        });

        // Botón cancelar edición
        if (cancelEditBtn) {
            cancelEditBtn.addEventListener('click', () => {
                // Restaurar valores originales
                if (document.getElementById('firstName')) {
                    document.getElementById('firstName').value = originalValues.firstName;
                }
                if (document.getElementById('lastName')) {
                    document.getElementById('lastName').value = originalValues.lastName;
                }
                if (document.getElementById('email')) {
                    document.getElementById('email').value = originalValues.email;
                }
                if (document.getElementById('birthDate')) {
                    document.getElementById('birthDate').value = originalValues.birthDate;
                }

                // Volver a modo visualización
                profileForm.style.display = 'none';
                profileView.style.display = 'block';
                editToggleBtn.innerHTML = '<i class="fas fa-edit"></i><span>Editar</span>';
            });
        }

        // Actualizar valores originales después de guardar exitosamente
        profileForm.addEventListener('submit', (e) => {
            // Si el formulario se envía con éxito, actualizar valores originales
            setTimeout(() => {
                originalValues.firstName = document.getElementById('firstName')?.value;
                originalValues.lastName = document.getElementById('lastName')?.value;
                originalValues.email = document.getElementById('email')?.value;
                originalValues.birthDate = document.getElementById('birthDate')?.value;
            }, 100);
        });
    }

    // ===== MODAL CAMBIAR CONTRASEÑA =====
    if (changePasswordBtn && passwordModal) {
        changePasswordBtn.addEventListener('click', () => {
            passwordModal.classList.add('active');
        });
    }

    if (closeModalBtn && passwordModal) {
        closeModalBtn.addEventListener('click', () => {
            passwordModal.classList.remove('active');
            clearPasswordForm();
        });
    }

    if (cancelPasswordBtn && passwordModal) {
        cancelPasswordBtn.addEventListener('click', () => {
            passwordModal.classList.remove('active');
            clearPasswordForm();
        });
    }

    // Cerrar modal al hacer click fuera
    if (passwordModal) {
        passwordModal.addEventListener('click', (e) => {
            if (e.target === passwordModal) {
                passwordModal.classList.remove('active');
                clearPasswordForm();
            }
        });
    }

    // Validación del formulario de contraseña
    if (changePasswordForm) {
        changePasswordForm.addEventListener('submit', (e) => {
            const newPassword = document.getElementById('newPassword').value;
            const confirmNewPassword = document.getElementById('confirmNewPassword').value;

            if (newPassword !== confirmNewPassword) {
                e.preventDefault();
                alert('Las contraseñas no coinciden. Por favor, verifica e intenta nuevamente.');
                return false;
            }

            if (newPassword.length < 8) {
                e.preventDefault();
                alert('La contraseña debe tener al menos 8 caracteres.');
                return false;
            }
        });
    }

    // ===== FUNCIONES AUXILIARES =====
    function clearPasswordForm() {
        if (changePasswordForm) {
            changePasswordForm.reset();
        }
    }

    // ===== VALIDACIÓN EN TIEMPO REAL =====
    const newPasswordInput = document.getElementById('newPassword');
    const confirmNewPasswordInput = document.getElementById('confirmNewPassword');

    if (newPasswordInput && confirmNewPasswordInput) {
        confirmNewPasswordInput.addEventListener('input', () => {
            if (confirmNewPasswordInput.value && newPasswordInput.value !== confirmNewPasswordInput.value) {
                confirmNewPasswordInput.setCustomValidity('Las contraseñas no coinciden');
            } else {
                confirmNewPasswordInput.setCustomValidity('');
            }
        });

        newPasswordInput.addEventListener('input', () => {
            if (confirmNewPasswordInput.value) {
                if (newPasswordInput.value !== confirmNewPasswordInput.value) {
                    confirmNewPasswordInput.setCustomValidity('Las contraseñas no coinciden');
                } else {
                    confirmNewPasswordInput.setCustomValidity('');
                }
            }
        });
    }
});