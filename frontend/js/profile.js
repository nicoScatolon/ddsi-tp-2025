/**
 * profile.js
 * - Intenta obtener el perfil real con GET /api/profile (espera JSON).
 * - Si falla o no responde, usa ProfileDB.getUser('me').
 * - Permite editar campos, cambiar imagen haciendo click en la imagen (preview).
 * - Al guardar intenta PUT /api/profile, si falla guarda en ProfileDB.
 */

document.addEventListener('DOMContentLoaded', () => {
  const avatarImg = document.getElementById('profileAvatar');
  const avatarInput = document.getElementById('avatarInput');

  const firstNameInput = document.getElementById('firstName');
  const lastNameInput = document.getElementById('lastName');
  const birthDateInput = document.getElementById('birthDate');
  const emailInput = document.getElementById('email');
  const residenceInput = document.getElementById('residence');

  const profileForm = document.getElementById('profileForm');
  const changePasswordBtn = document.getElementById('changePasswordBtn');
  const logoutBtn = document.getElementById('logoutBtn');
  const resetBtn = document.getElementById('resetBtn');

  let currentUser = null;

  // Utility: populate UI from user object
  function populateForm(user) {
    currentUser = user || {};
    firstNameInput.value = user.firstName || '';
    lastNameInput.value = user.lastName || '';
    birthDateInput.value = user.birthDate || '';
    emailInput.value = user.email || '';
    residenceInput.value = user.residence || '';

    // avatar: could be URL or dataURL; fallback to default icon
    const avatarSrc = user.avatar || 'images/user.ico';
    avatarImg.src = avatarSrc;
  }

  // Try to fetch "real" data from backend, fallback to ProfileDB
  async function loadProfile() {
    try {
      const resp = await fetch('/api/profile', { method: 'GET', credentials: 'include' });
      if (!resp.ok) throw new Error('No profile endpoint or not authorized');
      const json = await resp.json();
      populateForm(json);
      console.info('Profile loaded from server.');
    } catch (err) {
      console.info('Falling back to local ProfileDB:', err.message);
      // fallback to local DB
      if (window.ProfileDB && typeof window.ProfileDB.getUser === 'function') {
        const user = await window.ProfileDB.getUser('me');
        populateForm(user);
      } else {
        // Last fallback: minimal default
        populateForm({
          firstName: '',
          lastName: '',
          birthDate: '',
          email: '',
          residence: '',
          avatar: 'images/user.ico'
        });
      }
    }
  }

  // Save profile: tries server PUT, else ProfileDB.saveUser
  async function saveProfile(userData) {
    try {
      const resp = await fetch('/api/profile', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(userData)
      });
      if (!resp.ok) throw new Error('Server rejected save');
      const json = await resp.json();
      currentUser = json;
      populateForm(json);
      alert('Perfil actualizado en el servidor.');
      return;
    } catch (err) {
      console.warn('Save to server failed, saving locally:', err.message);
      if (window.ProfileDB && typeof window.ProfileDB.saveUser === 'function') {
        const saved = await window.ProfileDB.saveUser(userData);
        currentUser = saved;
        populateForm(saved);
        alert('Perfil guardado localmente (fallback).');
      } else {
        alert('No fue posible guardar los cambios.');
      }
    }
  }

  // Avatar change handling: clicking image triggers hidden file input
  avatarImg.addEventListener('click', () => avatarInput.click());
  avatarImg.addEventListener('keydown', (e) => { if (e.key === 'Enter' || e.key === ' ') avatarInput.click(); });

  avatarInput.addEventListener('change', (ev) => {
    const file = ev.target.files && ev.target.files[0];
    if (!file) return;
    if (!file.type.startsWith('image/')) {
      alert('Seleccioná una imagen válida.');
      return;
    }

    const reader = new FileReader();
    reader.onload = function (e) {
      const dataUrl = e.target.result;
      avatarImg.src = dataUrl; // preview immediately
      // update currentUser and try saving
      const updated = Object.assign({}, currentUser, { avatar: dataUrl });
      saveProfile(updated);
    };
    reader.readAsDataURL(file);
  });

  // Form submit - gather data and save
  profileForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const payload = {
      id: (currentUser && currentUser.id) || 'me',
      firstName: firstNameInput.value.trim(),
      lastName: lastNameInput.value.trim(),
      birthDate: birthDateInput.value || '',
      email: emailInput.value.trim(),
      residence: residenceInput.value.trim(),
      avatar: (currentUser && currentUser.avatar) || 'images/user.ico'
    };
    saveProfile(payload);
  });

  // Change password (UI stub) -> attempts POST to /api/change-password, falls back to prompt
  changePasswordBtn.addEventListener('click', async () => {
    const newPass = prompt('Ingresá la nueva contraseña: (demo)');
    if (!newPass) return;
    try {
      const resp = await fetch('/api/change-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ password: newPass })
      });
      if (!resp.ok) throw new Error('Server rejected');
      alert('Contraseña actualizada en el servidor.');
    } catch (err) {
      // fallback behaviour for demo
      alert('No se pudo actualizar en el servidor. (modo demo)');
    }
  });

  // Logout - tries server endpoint, else clears localStorage and redirect
  logoutBtn.addEventListener('click', async () => {
    try {
      const resp = await fetch('/api/logout', { method: 'POST', credentials: 'include' });
      // regardless of response, clear local and redirect
    } catch (err) {
      // ignore
    }
    // clear app-specific data (keeps other LS)
    if (window.ProfileDB && window.ProfileDB._reset) window.ProfileDB._reset();
    alert('Sesión cerrada (demo). Redirigiendo al inicio.');
    window.location.href = 'index.html';
  });

  // Reset button restores DB defaults (from ProfileDB) or empties fields
  resetBtn.addEventListener('click', async () => {
    if (!confirm('Restaurar datos por defecto/local?')) return;
    if (window.ProfileDB && window.ProfileDB.getUser) {
      const user = await window.ProfileDB.getUser('me');
      populateForm(user);
      alert('Datos restaurados desde la DB local (demo).');
    } else {
      populateForm({ firstName:'', lastName:'', birthDate:'', email:'', residence:'', avatar:'images/user.ico' });
    }
  });

  // init
  loadProfile();
});
