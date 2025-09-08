window.coleccionesDB = [
  {
    id: 101,
    titulo: "Robos y Asaltos Relevantes",
    descripcion: "Colección de los robos y asaltos más destacados en distintas ciudades argentinas. Incluye análisis de modus operandi y consecuencias legales.",
    ultimaActualizacion: "2025-09-01",
    tipo: "Mayoría Simple",
    hechos: [1, 7, 10, 15, 21],
    listaFuentes: ["fuente1", "fuente2"],
    listaCriterios: [
      { id: "CriterioUbicacion", baseId: "CriterioUbicacion", name: "Ubicación", values: { region: "Buenos Aires" } },
      { id: "CriterioTitulo", baseId: "CriterioTitulo", name: "Título", values: { match: "Robo" } }
    ],
    algoritmoConsenso: "MayoríaSimple"
  },
  {
    id: 102,
    titulo: "Accidentes Impactantes en Argentina",
    descripcion: "En esta colección se registran los accidentes más impactantes ocurridos en distintas provincias argentinas. Se incluyen choques de tránsito con múltiples víctimas, accidentes náuticos y ferroviarios, y situaciones de emergencia que han requerido la intervención de equipos especializados. Cada hecho está acompañado de un análisis sobre las causas, el desarrollo y las medidas tomadas posteriormente.",
    ultimaActualizacion: "2025-09-10",
    tipo: "Mayoría Simple",
    hechos: [2, 3, 6, 9, 12, 14, 20, 25],
    listaFuentes: ["fuente1", "fuente3"],
    listaCriterios: [
      { id: "CriterioUbicacion", baseId: "CriterioUbicacion", name: "Ubicación", values: { region: "Todo el país" } },
      { id: "CriterioTitulo", baseId: "CriterioTitulo", name: "Título", values: { match: "Accidente" } }
    ],
    algoritmoConsenso: "MayoríaSimple"
  },
  {
    id: 103,
    titulo: "Incendios y Emergencias Naturales",
    descripcion: "Reúne incidentes relacionados con incendios y emergencias naturales, incluyendo forestales y urbanos, con foco en medidas de prevención y respuesta.",
    ultimaActualizacion: "2025-08-28",
    tipo: "Concenso Absoluto",
    hechos: [3, 14, 19, 17, 23],
    listaFuentes: ["fuente3", "fuente4"],
    listaCriterios: [
      { id: "CriterioCategoria", baseId: "CriterioCategoria", name: "Categoría", values: { categoria: "Accidente" } }
    ],
    algoritmoConsenso: "ConcensoAbsoluto"
  },
  {
    id: 104,
    titulo: "Sucesos Policiales Destacados",
    descripcion: "Colección de hechos policiales relevantes, desde allanamientos hasta secuestros y estafas. Analiza patrones de criminalidad y respuestas institucionales.",
    ultimaActualizacion: "2025-09-02",
    tipo: "Mayoría Simple",
    hechos: [1, 4, 5, 8, 11, 13, 15, 16, 18, 21, 22, 24],
    listaFuentes: ["fuente2", "fuente5"],
    listaCriterios: [
      { id: "CriterioUbicacion", baseId: "CriterioUbicacion", name: "Ubicación", values: { region: "Gran Buenos Aires" } }
    ],
    algoritmoConsenso: "MayoríaSimple"
  },
  {
    id: 105,
    titulo: "Tránsito y Transporte",
    descripcion: "Registra accidentes y choques en rutas y avenidas principales, incluyendo estadísticas de heridos y medidas de prevención vial.",
    ultimaActualizacion: "2025-09-03",
    tipo: "MultipleMencion",
    hechos: [2, 6, 9, 12, 20, 25],
    listaFuentes: ["fuente1", "fuente6"],
    listaCriterios: [
      { id: "CriterioUbicacion", baseId: "CriterioUbicacion", name: "Ubicación", values: { region: "Todo el país" } },
      { id: "CriterioTitulo", baseId: "CriterioTitulo", name: "Título", values: { match: "Tránsito" } }
    ],
    algoritmoConsenso: "MultipleMencion"
  }
];


// Colores de fallback
const fallbackColors = ["#2F60F4", "#1746D2", "#1A3FA0", "#3B6EFF", "#4A79FF", "#6A6AFF"];
