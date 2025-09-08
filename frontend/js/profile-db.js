/**
 * profile-db.js
 * Simple "database" hardcodeado + almacenamiento en localStorage como persistencia local.
 * Provee:
 *   ProfileDB.getUser(id) -> Promise<user>
 *   ProfileDB.saveUser(user) -> Promise<user>
 *
 * Estructura user:
 * {
 *   id: 'me',
 *   firstName,
 *   lastName,
 *   birthDate,      // YYYY-MM-DD
 *   email,
 *   residence,
 *   avatar          // URL o dataURL
 * }
 */

(function () {
  const STORAGE_KEY = 'metamapa_profile_db_v1';

  // default hardcoded user
  const DEFAULT = {
    id: 'me',
    firstName: 'Juan',
    lastName: 'Pérez',
    birthDate: '1990-08-10',
    email: 'juan.perez@example.com',
    residence: 'Provincia X, Ciudad Y',
    avatar: 'images/user.ico'
  };

  function readStorage() {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      if (!raw) return Object.assign({}, DEFAULT);
      return Object.assign({}, DEFAULT, JSON.parse(raw));
    } catch (e) {
      console.warn('ProfileDB read error', e);
      return Object.assign({}, DEFAULT);
    }
  }

  function writeStorage(obj) {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(obj));
    } catch (e) {
      console.warn('ProfileDB write error', e);
    }
  }

  window.ProfileDB = {
    getUser: function (id) {
      // returns a promise to mimic async DB call
      return new Promise((resolve) => {
        const user = readStorage();
        resolve(Object.assign({}, user));
      });
    },

    saveUser: function (userUpdate) {
      return new Promise((resolve) => {
        const current = readStorage();
        const next = Object.assign({}, current, userUpdate);
        writeStorage(next);
        resolve(Object.assign({}, next));
      });
    },

    // util: clear DB (for testing)
    _reset: function () {
      localStorage.removeItem(STORAGE_KEY);
    }
  };
})();
