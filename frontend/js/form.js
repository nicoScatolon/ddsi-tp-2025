document.addEventListener('DOMContentLoaded', () => {
  // --- FORMULARIO CATEGORÍA "OTRA" ---
  const selectCategoria = document.getElementById('categoria');
  const inputNuevaCategoria = document.getElementById('categoriaNueva');
  if (selectCategoria && inputNuevaCategoria) {
    inputNuevaCategoria.style.display = 'none';
    selectCategoria.addEventListener('change', () => {
      if (selectCategoria.value === 'other') {
        inputNuevaCategoria.style.display = 'block';
        inputNuevaCategoria.required = true;
      } else {
        inputNuevaCategoria.style.display = 'none';
        inputNuevaCategoria.required = false;
        inputNuevaCategoria.value = '';
      }
    });
  }

  // --- GESTIÓN SELECTOR TIPO DE UBICACIÓN ---
  const tipoUbicacion = document.getElementById('tipoUbicacion');
  const camposDireccion = ['provincia', 'localidad', 'calle', 'numero'];
  const contenedorCoordenadas = ['latitudUbicacion', 'longitudUbicacion'];
  const contenedorPuntoInteres = ['puntoInteres'];

  const actualizarCamposUbicacion = () => {
    const tipo = tipoUbicacion.value;
    [...camposDireccion, ...contenedorCoordenadas, ...contenedorPuntoInteres].forEach(id => {
      const el = document.getElementById(id);
      if (el) {
        el.closest('.form-group').style.display = 'none';
        el.required = false;
        el.value = '';
      }
    });
    if (tipo === 'direccion') camposDireccion.forEach(id => document.getElementById(id).closest('.form-group').style.display = '');
    else if (tipo === 'coordenadas') contenedorCoordenadas.forEach(id => document.getElementById(id).closest('.form-group').style.display = '');
    else if (tipo === 'puntoInteres') contenedorPuntoInteres.forEach(id => document.getElementById(id).closest('.form-group').style.display = '');
  };

  if (tipoUbicacion) {
    tipoUbicacion.addEventListener('change', actualizarCamposUbicacion);
    actualizarCamposUbicacion();
  }

  // --- FORMULARIO SUBMIT ---
  const formHecho = document.getElementById('hechoForm');
  if (formHecho) {
    formHecho.addEventListener('submit', (e) => {
      e.preventDefault();
      const formData = Object.fromEntries(new FormData(formHecho).entries());

      // Guardar en localStorage
      let hechos = JSON.parse(localStorage.getItem("hechos")) || [];
      const newId = hechos.length ? Math.max(...hechos.map(h => h.id)) + 1 : 1;
      hechos.push({ id: newId, ...formData });
      localStorage.setItem("hechos", JSON.stringify(hechos));

      alert('Hecho creado con éxito!');
      formHecho.reset();
      actualizarCamposUbicacion();
    });
  }
});
