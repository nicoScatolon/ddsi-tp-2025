document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("fuentesToggle");
    const panel = document.getElementById("fuentesPanel");
    const chipsContainer = document.getElementById("fuentesChips");
    const countSpan = document.getElementById("fuentesCount");
    const hiddenInput = document.getElementById("listaIdsFuentes");

    toggle.addEventListener("click", () => {
        const expanded = panel.style.display === "block";
        panel.style.display = expanded ? "none" : "block";
        toggle.setAttribute("aria-expanded", !expanded);
        panel.setAttribute("aria-hidden", expanded);
    });

    const checkboxes = panel.querySelectorAll(".fuente-checkbox");

    checkboxes.forEach(cb => {
        cb.addEventListener("change", () => {
            updateFuentes();
        });
    });

    function updateFuentes() {
        const selected = Array.from(panel.querySelectorAll(".fuente-checkbox:checked"));
        const ids = selected.map(cb => cb.value);
        const nombres = selected.map(cb => cb.dataset.nombre);

        // Actualizar chips
        chipsContainer.innerHTML = nombres.map(n => `<span class="chip">${n}</span>`).join("");

        // Actualizar contador
        countSpan.textContent = `(${ids.length})`;

        // Actualizar input oculto para enviar al backend
        hiddenInput.value = ids.join(","); // si usas Spring, podes recibirlo como String y luego convertir a Set<Long>
    }
    document.getElementById('createCollectionForm').addEventListener('submit', function() {
        const select = document.getElementById('algoritmo');
        if (select.value === "") {
            select.removeAttribute('name');
        }
    });
});
