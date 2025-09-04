(function(){
  // -------------------------
  // Forzar favicon siempre a icon.ico
  // -------------------------
  (function ensureFavicon() {
    try {
      const path = 'images/icon.ico';
      // Eliminar cualquier favicon existente
      document.querySelectorAll('link[rel="icon"], link[rel="shortcut icon"]').forEach(el => el.remove());
      // Crear nuevo favicon
      const link = document.createElement('link');
      link.rel = 'icon';
      link.type = 'image/x-icon';
      link.href = path;
      document.head.appendChild(link);
    } catch (err) {
      console.warn('No se pudo establecer favicon:', err);
    }
  })();

  // -------------------------
  // Configuración
  // -------------------------
  const availableFuentes = [
    {id:'fuente1', label:'Fuente 1', meta:{origen:'API A'}},
    {id:'fuente2', label:'Fuente 2', meta:{origen:'API B'}},
    {id:'fuente3', label:'Fuente 3', meta:{origen:'RSS'}},
    {id:'fuente4', label:'Fuente 4', meta:{origen:'Usuario'}},
    {id:'fuente5', label:'Fuente 5', meta:{origen:'Sensor'}},
    {id:'fuente6', label:'Fuente 6', meta:{origen:'CSV'}}
  ];

  const availableCriterios = [
    { id:'CriterioUbicacion', name:'Ubicación', params:[ {key:'region', label:'Región / Zona', type:'text'} ] },
    { id:'CriterioTitulo', name:'Título', params:[ {key:'match', label:'Texto a buscar', type:'text'} ] },
    { id:'CriterioOcurrenciaEntreFechas', name:'Ocurrencia Entre Fechas', params:[ {key:'desde', label:'Desde (YYYY-MM-DD)', type:'date'}, {key:'hasta', label:'Hasta (YYYY-MM-DD)', type:'date'} ] },
    { id:'CriterioContenidoMultimedia', name:'Contenido Multimedia', params:[ {key:'tieneImagen', label:'Contiene imagen', type:'boolean'}, {key:'tieneVideo', label:'Contiene video', type:'boolean'} ] },
    { id:'CriterioCategoria', name:'Categoría', params:[ {key:'categoria', label:'Categoría', type:'select', options:['Tránsito','Seguridad','Servicios','Otro']} ] },
    { id:'CriterioCargaEntreFechas', name:'Carga Entre Fechas', params:[ {key:'cargaDesde', label:'Carga desde (YYYY-MM-DD)', type:'date'}, {key:'cargaHasta', label:'Carga hasta (YYYY-MM-DD)', type:'date'} ] }
  ];

  const consensusAlgorithms = [
    {value:'', label:'Ninguno'},
    {value:'ConcensoAbsoluto', label:'Concenso Absoluto'},
    {value:'MayoríaSimple', label:'Mayoría Simple'},
    {value:'MultipleMencion', label:'Múltiple Mención'}
  ];

  // -------------------------
  // Helpers
  // -------------------------
  const $ = (sel) => document.querySelector(sel);
  const $$ = (sel) => Array.from(document.querySelectorAll(sel));

  function ensureDB(){ if(!window.coleccionesDB) window.coleccionesDB = []; return window.coleccionesDB; }
  function getQueryParams(){ const qs = new URLSearchParams(window.location.search); const obj={}; for(const [k,v] of qs.entries()) obj[k]=v; return obj; }
  function showToast(msg, ms=2200){ const t = $('#toast'); if(!t) return console.log(msg); t.textContent = msg; t.style.display='block'; clearTimeout(showToast._t); showToast._t = setTimeout(()=> t.style.display='none', ms); }

  // -------------------------
  // Estado UI
  // -------------------------
  const selectedFuentes = new Set();
  const selectedCriterios = new Map();

  const fuentesToggle = $('#fuentesToggle');
  const fuentesPanel  = $('#fuentesPanel');
  const fuentesChips  = $('#fuentesChips');
  const fuentesCount  = $('#fuentesCount');

  const criterioSelect = $('#criterioSelect');
  const addCriterioBtn = $('#addCriterioBtn');
  const criteriosContainer = $('#criteriosContainer');

  const algoritmoSelect = $('#algoritmo');

  // -------------------------
  // Fuentes
  // -------------------------
  function renderFuentesPanel(){
    if (!fuentesPanel) return;
    fuentesPanel.innerHTML='';
    availableFuentes.forEach(f=>{
      const row = document.createElement('div'); row.className='fuente-row'; row.dataset.id=f.id;
      const cb = document.createElement('input'); cb.type='checkbox'; cb.id='chk_'+f.id; cb.checked = selectedFuentes.has(f.id);
      const label = document.createElement('label'); label.htmlFor=cb.id; label.textContent = f.label + (f.meta.origen ? ' · '+f.meta.origen:'');
      row.appendChild(cb); row.appendChild(label);
      row.addEventListener('click', e=>{ if(e.target.tagName!=='INPUT'){ cb.checked=!cb.checked; toggleFuente(f.id, cb.checked); } });
      cb.addEventListener('change', ()=>toggleFuente(f.id, cb.checked));
      fuentesPanel.appendChild(row);
    });
  }

  function toggleFuente(id, checked){ if(checked) selectedFuentes.add(id); else selectedFuentes.delete(id); updateFuentesUI(); }

  function updateFuentesUI(){
    if(fuentesChips){
      fuentesChips.innerHTML='';
      selectedFuentes.forEach(id=>{
        const f = availableFuentes.find(x=>x.id===id); if(!f) return;
        const chip = document.createElement('span'); chip.className='chip'; chip.textContent=f.label;
        const btn = document.createElement('button'); btn.type='button'; btn.className='btn'; btn.textContent='x';
        btn.style.padding='2px 6px'; btn.style.fontSize='0.8rem'; btn.style.borderRadius='6px';
        btn.addEventListener('click', e=>{ e.stopPropagation(); selectedFuentes.delete(id); renderFuentesPanel(); updateFuentesUI(); });
        chip.appendChild(btn);
        fuentesChips.appendChild(chip);
      });
    }
    if(fuentesCount) fuentesCount.textContent = `(${selectedFuentes.size})`;
    $$('.fuente-row input[type="checkbox"]').forEach(cb=>{ cb.checked = selectedFuentes.has(cb.id.replace('chk_','')); });
  }

  if(fuentesToggle && fuentesPanel){
    fuentesToggle.addEventListener('click', ()=>{
      const open = fuentesPanel.style.display==='block';
      fuentesPanel.style.display = open?'none':'block';
      fuentesToggle.setAttribute('aria-expanded', String(!open));
      fuentesPanel.setAttribute('aria-hidden', String(open));
    });
    document.addEventListener('click', e=>{
      if(!fuentesToggle.contains(e.target) && !fuentesPanel.contains(e.target)){
        fuentesPanel.style.display='none';
        fuentesToggle.setAttribute('aria-expanded','false');
        fuentesPanel.setAttribute('aria-hidden','true');
      }
    });
  }

  // -------------------------
  // Criterios
  // -------------------------
  function populateCriterioSelect(){
    if(!criterioSelect) return;
    criterioSelect.innerHTML='<option value="">-- escoger criterio --</option>';
    availableCriterios.forEach(c=>{
      const opt = document.createElement('option'); opt.value=c.id; opt.textContent=c.name; criterioSelect.appendChild(opt);
    });
  }

  function renderCriteriosContainer(){
    if(!criteriosContainer) return;
    criteriosContainer.innerHTML='';
    selectedCriterios.forEach(inst=>{
      const card = document.createElement('div'); card.className='criterio-card';
      const header = document.createElement('div'); header.style.display='flex'; header.style.justifyContent='space-between';
      const title = document.createElement('h4'); title.textContent=inst.name; header.appendChild(title);
      const actions = document.createElement('div');
      const cloneBtn = document.createElement('button'); cloneBtn.type='button'; cloneBtn.className='btn'; cloneBtn.textContent='Duplicar';
      const removeBtn = document.createElement('button'); removeBtn.type='button'; removeBtn.className='btn'; removeBtn.textContent='Eliminar';
      actions.appendChild(cloneBtn); actions.appendChild(removeBtn); header.appendChild(actions); card.appendChild(header);

      const paramsWrap = document.createElement('div'); paramsWrap.className='criterio-params';
      const def = availableCriterios.find(c=>c.id===inst.baseId);
      (def? def.params : []).forEach(p=>{
        const fDiv = document.createElement('div');
        const fieldId = `param_${inst.id}_${p.key}_${Math.floor(Math.random()*9999)}`;
        const label = document.createElement('label'); label.htmlFor=fieldId; label.className='small'; label.textContent=p.label;
        let el;
        if(p.type==='select'){ el=document.createElement('select'); el.id=fieldId; p.options.forEach(o=>{ const opt=document.createElement('option'); opt.value=o; opt.textContent=o; el.appendChild(opt); }); }
        else if(p.type==='boolean'){ el=document.createElement('select'); el.id=fieldId; ['','true','false'].forEach(v=>{ const o=document.createElement('option'); o.value=v; o.textContent=v===''?'--':(v==='true'?'Sí':'No'); el.appendChild(o); }); }
        else{ el=document.createElement('input'); el.type=p.type||'text'; el.id=fieldId; }
        if(inst.values && inst.values[p.key]!==undefined) el.value=inst.values[p.key];
        el.addEventListener('input', e=>{ if(!inst.values) inst.values={}; inst.values[p.key]=e.target.value; });
        fDiv.appendChild(label); fDiv.appendChild(el); paramsWrap.appendChild(fDiv);
      });
      card.appendChild(paramsWrap);
      criteriosContainer.appendChild(card);

      removeBtn.addEventListener('click', ()=>{ selectedCriterios.delete(inst.id); renderCriteriosContainer(); });
      cloneBtn.addEventListener('click', ()=>{
        const newId = inst.baseId+'_copy_'+Date.now();
        const clone={ baseId:inst.baseId, id:newId, name:inst.name, values:{...inst.values} };
        selectedCriterios.set(newId, clone); renderCriteriosContainer();
      });
    });
  }

  if(addCriterioBtn && criterioSelect){
    addCriterioBtn.addEventListener('click', ()=>{
      const sel = criterioSelect.value; if(!sel){ showToast('Seleccioná un criterio'); return; }
      if(Array.from(selectedCriterios.values()).some(x=>x.baseId===sel)){ showToast('Ya agregado'); return; }
      const def = availableCriterios.find(c=>c.id===sel);
      selectedCriterios.set(def.id, { baseId:def.id, id:def.id, name:def.name, values:{} });
      renderCriteriosContainer();
    });
  }

  // -------------------------
  // Algoritmos
  // -------------------------
  function populateAlgoritmos(){
    if(!algoritmoSelect) return;
    algoritmoSelect.innerHTML='';
    consensusAlgorithms.forEach(a=>{ const opt=document.createElement('option'); opt.value=a.value; opt.textContent=a.label; algoritmoSelect.appendChild(opt); });
  }

  // -------------------------
  // Inicialización / Prefill
  // -------------------------
  function init(){
    populateCriterioSelect();
    renderFuentesPanel();
    updateFuentesUI();
    renderCriteriosContainer();
    populateAlgoritmos();

    const q = getQueryParams();
    if(!q.id){ showToast('Falta id'); return; }
    const db = ensureDB(); const found = db.find(c=>String(c.id)===String(q.id));
    if(!found){ showToast('No encontrada'); return; }

    $('#titulo').value = found.titulo||'';
    $('#descripcion').value = found.descripcion||'';
    if(Array.isArray(found.listaFuentes)) found.listaFuentes.forEach(f=>selectedFuentes.add(f));
    if(Array.isArray(found.listaCriterios)) found.listaCriterios.forEach(c=>{
      selectedCriterios.set(c.id||c.baseId, { baseId:c.baseId, id:c.id||c.baseId, name:c.name, values:{...c.values} });
    });
    if(found.algoritmoConsenso && algoritmoSelect.querySelector(`option[value="${found.algoritmoConsenso}"]`)){
      algoritmoSelect.value = found.algoritmoConsenso;
    }
    renderFuentesPanel(); updateFuentesUI(); renderCriteriosContainer();
  }
  init();

  // -------------------------
  // Formulario: guardar / eliminar / cancelar
  // -------------------------
  const form = $('#modifyCollectionForm');
  const cancelBtn = $('#cancelBtn');
  const deleteBtn = $('#deleteBtn');

  if(cancelBtn) cancelBtn.addEventListener('click', ()=>{ window.location.href='collections.html'; });

  if(deleteBtn) deleteBtn.addEventListener('click', ()=>{
    const q=getQueryParams(); if(!q.id) return;
    const db = ensureDB(); const idx=db.findIndex(c=>String(c.id)===String(q.id));
    if(idx===-1){ showToast('No encontrada para eliminar'); return; }
    if(!confirm('¿Eliminar esta colección?')) return;
    db.splice(idx,1); showToast('Eliminada'); setTimeout(()=>window.location.href='collections.html',800);
  });

  if(form) form.addEventListener('submit', e=>{
    e.preventDefault();
    const q=getQueryParams(); if(!q.id){ showToast('ID ausente'); return; }
    const db=ensureDB(); const existing=db.find(c=>String(c.id)===String(q.id)); if(!existing){ showToast('No encontrada'); return; }

    existing.titulo = $('#titulo').value.trim();
    existing.descripcion = $('#descripcion').value.trim();
    existing.listaFuentes = Array.from(selectedFuentes);
    existing.listaCriterios = Array.from(selectedCriterios.values()).map(c=>({id:c.id, baseId:c.baseId, name:c.name, values:c.values}));
    existing.algoritmoConsenso = algoritmoSelect.value;

    showToast('Guardado correctamente');
  });

})();
