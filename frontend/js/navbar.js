document.addEventListener("DOMContentLoaded", () => {
  // --- cached DOM ---
  const desktopNav = document.querySelector(".desktop-nav");
  const mobileNav = document.querySelector(".mobile-nav");
  const zoomNav = document.querySelector(".zoom-nav");
  const burgerBtn = document.getElementById("burgerBtn");
  const sidebar = document.getElementById("sidebar");

  const authButtons = document.getElementById("authButtons");
  const desktopUserContainer = document.getElementById("desktopUserContainer");
  const desktopUserBtn = document.getElementById("desktopUserBtn");
  const desktopUserLabel = document.getElementById("desktopUserLabel");
  const desktopUserDropdown = document.getElementById("desktopUserDropdown");
  const desktopAvatar = document.getElementById("desktopAvatar");

  const mobileProfileMenu = document.getElementById("mobileProfileMenu");
  const mobileProfileDropdown = document.getElementById("mobileProfileDropdown");
  const sidebarUserArea = document.getElementById("sidebarUserArea");

  // --- configuration / state ---
  const isMobileUA = /Android|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
  // selector list for which elements we want to apply padding-top for the nav (keeps it limited)
  const contentSections = document.querySelectorAll(".form-container, .collections, .about, .login-section, .signup-section, .map, .explore, .hecho-detail");

  // Rol guardado: "guest" | "user" | "admin"
  let userRole = localStorage.getItem("userRole") || "admin";

  function getAvatarSrc() {
    return localStorage.getItem("userAvatar") || "images/user.ico";
  }

  // --- small helpers to create DOM nodes consistently ---
  function makeLink(href, text, options = {}) {
    const a = document.createElement("a");
    a.href = href;
    a.textContent = text;
    a.className = options.className || "";
    if (options.attrs) {
      Object.entries(options.attrs).forEach(([k, v]) => a.setAttribute(k, v));
    }
    if (options.onclick) a.addEventListener("click", options.onclick);
    return a;
  }

  function makeBadge(n) {
    const s = document.createElement("span");
    s.className = "badge";
    s.textContent = String(n);
    return s;
  }

  // --- build dropdowns / areas ---
  function buildDesktopDropdown() {
    if (!desktopUserDropdown) return;
    desktopUserDropdown.innerHTML = "";

    if (userRole === "user") {
      const items = [
        { href: "profile.html", text: "Mi perfil" },
        { href: "mis-hechos.html", text: "Mis Hechos", badge: parseInt(localStorage.getItem("misHechosBadge") || "0", 10) || 0 },
        { href: "mis-solicitudes.html", text: "Mis Solicitudes" }
      ];

      items.forEach(it => {
        const a = makeLink(it.href, it.text);
        a.style.display = "flex";
        a.style.justifyContent = "space-between";
        a.style.alignItems = "center";
        a.style.padding = "8px 12px";
        a.style.color = "white";
        a.style.textDecoration = "none";
        a.style.borderBottom = "1px solid rgba(255,255,255,0.08)";
        if (it.badge && it.badge > 0) a.appendChild(makeBadge(it.badge));
        desktopUserDropdown.appendChild(a);
      });

      const logout = makeLink("#", "Log out", {
        onclick: (e) => {
          e.preventDefault();
          localStorage.removeItem("userRole");
          userRole = "guest";
          updateRoleUI();
          closeAllDropdowns();
        }
      });
      logout.style.display = "block";
      logout.style.padding = "8px 12px";
      logout.style.color = "white";
      logout.style.textDecoration = "none";
      desktopUserDropdown.appendChild(logout);
    } else if (userRole === "admin") {
      const panel = makeLink("admin-panel.html", "Panel de administrador");
      panel.className = "admin-option";
      // keep a few inline styles as before so it looks identical
      panel.style.display = "block";
      panel.style.padding = "10px 12px";
      panel.style.color = "var(--color-accent)";
      panel.style.background = "white";
      panel.style.margin = "6px";
      panel.style.borderRadius = "6px";
      desktopUserDropdown.appendChild(panel);

      const rest = [
        { href: "profile.html", text: "Mi Perfil" },
        { href: "mis-hechos.html", text: "Mis Hechos" },
        { href: "mis-solicitudes.html", text: "Mis Solicitudes" }
      ];
      rest.forEach(it => {
        const a = makeLink(it.href, it.text);
        a.style.display = "block";
        a.style.padding = "8px 12px";
        a.style.color = "white";
        a.style.textDecoration = "none";
        a.style.borderBottom = "1px solid rgba(255,255,255,0.08)";
        desktopUserDropdown.appendChild(a);
      });

      const logout = makeLink("#", "Log out", {
        onclick: (e) => {
          e.preventDefault();
          localStorage.removeItem("userRole");
          userRole = "guest";
          updateRoleUI();
          closeAllDropdowns();
        }
      });
      logout.style.display = "block";
      logout.style.padding = "8px 12px";
      logout.style.color = "white";
      logout.style.textDecoration = "none";
      desktopUserDropdown.appendChild(logout);
    }
  }

  function buildSidebarUserArea() {
    if (!sidebarUserArea) return;
    sidebarUserArea.innerHTML = "";

    if (userRole === "guest") {
      const aLogin = makeLink("login.html", "Log in");
      aLogin.className = "btn-secondary-custom";
      const aSignup = makeLink("signup.html", "Sign up");
      aSignup.className = "btn-secondary-custom";
      sidebarUserArea.appendChild(aLogin);
      sidebarUserArea.appendChild(aSignup);
      return;
    }

    // user/admin button
    const btn = document.createElement("button");
    btn.type = "button";
    btn.id = "sidebarUserBtn";
    btn.className = "btn-secondary-custom";
    btn.style.display = "flex";
    btn.style.alignItems = "center";
    btn.style.justifyContent = "flex-start";
    btn.style.gap = "8px";

    const label = document.createElement("span");
    label.textContent = (userRole === "admin") ? "Admin" : "User";
    btn.appendChild(label);

    const img = document.createElement("img");
    img.src = getAvatarSrc();
    img.alt = "avatar";
    img.style.width = "28px";
    img.style.height = "28px";
    img.style.borderRadius = "50%";
    img.style.objectFit = "cover";
    img.style.marginLeft = "auto";
    img.style.border = "2px solid white";
    btn.appendChild(img);

    sidebarUserArea.appendChild(btn);

    // submenu
    const submenu = document.createElement("div");
    submenu.id = "sidebarUserSubmenu";
    submenu.style.display = "none";
    submenu.style.marginTop = "8px";
    submenu.style.marginLeft = "6px";
    submenu.style.flexDirection = "column";
    submenu.style.gap = "6px";

    if (userRole === "user") {
      const links = [
        { href: "profile.html", text: "Mi perfil" },
        { href: "mis-hechos.html", text: "Mis Hechos" },
        { href: "mis-solicitudes.html", text: "Mis Solicitudes" }
      ];
      links.forEach(it => {
        const a = makeLink(it.href, it.text);
        a.style.color = "white";
        a.style.textDecoration = "none";
        a.style.padding = "8px 8px";
        submenu.appendChild(a);
      });
    } else if (userRole === "admin") {
      const panel = makeLink("admin-panel.html", "Panel de administrador");
      panel.style.color = "#1746D2";
      panel.style.background = "white";
      panel.style.padding = "8px";
      panel.style.borderRadius = "6px";
      panel.style.textDecoration = "none";
      submenu.appendChild(panel);

      const rest = [
        { href: "profile.html", text: "Mi Perfil" },
        { href: "mis-hechos.html", text: "Mis Hechos" },
        { href: "mis-solicitudes.html", text: "Mis Solicitudes" }
      ];
      rest.forEach(it => {
        const a = makeLink(it.href, it.text);
        a.style.color = "white";
        a.style.textDecoration = "none";
        a.style.padding = "8px 8px";
        submenu.appendChild(a);
      });
    }

    const logout = makeLink("#", "Log out", {
      onclick: (e) => {
        e.preventDefault();
        localStorage.removeItem("userRole");
        userRole = "guest";
        updateRoleUI();
        closeAllDropdowns();
      }
    });
    logout.style.color = "white";
    logout.style.textDecoration = "none";
    logout.style.padding = "8px 8px";
    submenu.appendChild(logout);

    sidebarUserArea.appendChild(submenu);

    // toggle submenu
    btn.addEventListener("click", (e) => {
      e.stopPropagation();
      submenu.style.display = submenu.style.display === "flex" ? "none" : "flex";
    });
  }

  function buildMobileProfileDropdown() {
    if (!mobileProfileDropdown) return;
    mobileProfileDropdown.innerHTML = "";

    if (userRole === "guest") {
      mobileProfileDropdown.appendChild(makeLink("login.html", "Log in"));
      mobileProfileDropdown.appendChild(makeLink("signup.html", "Sign up"));
      return;
    }

    if (userRole === "user") {
      const items = [
        { href: "profile.html", text: "Mi perfil" },
        { href: "mis-hechos.html", text: "Mis Hechos", badge: parseInt(localStorage.getItem("misHechosBadge") || "0", 10) || 0 },
        { href: "mis-solicitudes.html", text: "Mis Solicitudes" }
      ];
      items.forEach(it => {
        const a = makeLink(it.href, it.text);
        if (it.badge && it.badge > 0) a.appendChild(makeBadge(it.badge));
        mobileProfileDropdown.appendChild(a);
      });

      const logout = makeLink("#", "Log out", {
        onclick: (e) => {
          e.preventDefault();
          localStorage.removeItem("userRole");
          userRole = "guest";
          updateRoleUI();
          closeAllDropdowns();
        }
      });
      mobileProfileDropdown.appendChild(logout);
      return;
    }

    // admin
    const adminLinks = [
      { href: "admin-panel.html", text: "Panel de administrador", adminHighlight: true },
      { href: "profile.html", text: "Perfil" },
      { href: "hechos.html", text: "Hechos" },
      { href: "admin-solicitudes.html", text: "Gestionar Solicitudes" }
    ];
    adminLinks.forEach(it => {
      const a = makeLink(it.href, it.text);
      if (it.adminHighlight) {
        a.className = "admin-option";
      }
      mobileProfileDropdown.appendChild(a);
    });

    const logout = makeLink("#", "Log out", {
      onclick: (e) => {
        e.preventDefault();
        localStorage.removeItem("userRole");
        userRole = "guest";
        updateRoleUI();
        closeAllDropdowns();
      }
    });
    mobileProfileDropdown.appendChild(logout);
  }

  // --- UI update based on role ---
  function updateRoleUI() {
    if (authButtons) authButtons.style.display = (userRole === "guest") ? "flex" : "none";
    if (desktopUserContainer) desktopUserContainer.style.display = (userRole === "guest") ? "none" : "block";

    if (desktopUserLabel) desktopUserLabel.textContent = (userRole === "admin") ? "Admin" : "User";
    if (desktopAvatar) desktopAvatar.src = getAvatarSrc();

    buildDesktopDropdown();
    buildMobileProfileDropdown();
    buildSidebarUserArea();
  }

  // --- helpers to open/close dropdowns ---
  function closeAllDropdowns() {
    if (desktopUserDropdown) desktopUserDropdown.style.display = "none";
    if (mobileProfileDropdown) mobileProfileDropdown.style.display = "none";
    const sbSub = document.getElementById("sidebarUserSubmenu");
    if (sbSub) sbSub.style.display = "none";
  }

  // --- nav visibility & content padding logic ---
  function updateNav() {
    const isNarrow = window.innerWidth <= 768;

    if (isMobileUA) {
      if (desktopNav) desktopNav.style.display = "none";
      if (mobileNav) mobileNav.style.display = "flex";
      if (zoomNav) zoomNav.style.display = "none";
    } else {
      if (isNarrow) {
        if (desktopNav) desktopNav.style.display = "none";
        if (mobileNav) mobileNav.style.display = "none";
        if (zoomNav) zoomNav.style.display = "flex";
      } else {
        if (desktopNav) desktopNav.style.display = "flex";
        if (mobileNav) mobileNav.style.display = "none";
        if (zoomNav) zoomNav.style.display = "none";
      }
    }

    // apply padding-top only to the specific contentSections we selected
    let activeNav = null;
    if (desktopNav && getComputedStyle(desktopNav).display !== "none") activeNav = desktopNav;
    else if (zoomNav && getComputedStyle(zoomNav).display !== "none") activeNav = zoomNav;

    const paddingTopValue = activeNav ? `${Math.ceil(activeNav.getBoundingClientRect().height)}px` : "0px";
    contentSections.forEach(sec => {
      sec.style.paddingTop = paddingTopValue;
    });
  }

  // --- event wiring ---
  if (burgerBtn && sidebar) {
    burgerBtn.addEventListener("click", () => sidebar.classList.toggle("active"));
  }

  if (desktopUserBtn && desktopUserDropdown) {
    desktopUserBtn.addEventListener("click", (e) => {
      e.stopPropagation();
      const isOpen = desktopUserDropdown.style.display === "block";
      closeAllDropdowns();
      desktopUserDropdown.style.display = isOpen ? "none" : "block";
    });
  }

  if (mobileProfileMenu && mobileProfileDropdown) {
    mobileProfileMenu.addEventListener("click", (e) => {
      e.stopPropagation();
      const isOpen = mobileProfileDropdown.style.display === "flex";
      closeAllDropdowns();
      mobileProfileDropdown.style.display = isOpen ? "none" : "flex";
      if (mobileProfileDropdown.style.display === "flex") mobileProfileDropdown.style.flexDirection = "column";
    });

    // close mobile dropdown if a link inside is clicked
    mobileProfileDropdown.addEventListener("click", (e) => {
      if (e.target && e.target.tagName === "A") mobileProfileDropdown.style.display = "none";
    });
  }

  // hide dropdowns on outside click
  document.addEventListener("click", (e) => {
    if (!desktopUserContainer || !desktopUserContainer.contains(e.target)) {
      if (desktopUserDropdown) desktopUserDropdown.style.display = "none";
    }
    if (!mobileProfileMenu || !mobileProfileMenu.contains(e.target)) {
      if (mobileProfileDropdown) mobileProfileDropdown.style.display = "none";
    }
    if (!sidebarUserArea || !sidebarUserArea.contains(e.target)) {
      const sbSub = document.getElementById("sidebarUserSubmenu");
      if (sbSub) sbSub.style.display = "none";
    }
  });

  // --- init ---
  updateRoleUI();
  updateNav();

  window.addEventListener("resize", updateNav);

  // helper for testing / runtime switching
  window.__metaMapa_setRole = (newRole) => {
    localStorage.setItem("userRole", newRole);
    userRole = newRole;
    updateRoleUI();
  };
});
