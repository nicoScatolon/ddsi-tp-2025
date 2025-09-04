// Base de datos de colecciones
const coleccionesDB = [
    { 
        id: 1, 
        titulo: "Colección Historia", 
        descripcion: "Esta colección reúne los hechos históricos más relevantes que han marcado un antes y un después en la sociedad argentina y mundial. Incluye acontecimientos políticos, sociales y culturales que permiten comprender la evolución de la historia reciente y pasada, proporcionando contexto y análisis sobre sus consecuencias y repercusiones en la actualidad.",
        ultimaActualizacion: "2025-08-21",
        tipo: "Concenso Absoluto",
        hechos: [1]
    },
    // Nuevas colecciones generadas usando todos los hechos de HECHOS
    {
        id: 101,
        titulo: "Policía de Córdoba: Casos Relevantes",
        descripcion: "Esta colección agrupa los hechos policiales más destacados ocurridos en la provincia de Córdoba y sus alrededores. Incluye robos de alto impacto, allanamientos importantes, operativos de seguridad y casos que han tenido repercusión mediática. Cada hecho está documentado con detalles sobre la investigación, el accionar policial y las consecuencias para la comunidad.",
        ultimaActualizacion: "2025-09-10",
        tipo: "Concenso Absoluto",
        hechos: [2, 4, 5, 7, 11, 13]
    },
    {
        id: 102,
        titulo: "Accidentes Impactantes en Argentina",
        descripcion: "En esta colección se registran los accidentes más impactantes ocurridos en distintas provincias argentinas. Se incluyen choques de tránsito con múltiples víctimas, accidentes náuticos y ferroviarios, y situaciones de emergencia que han requerido la intervención de equipos especializados. Cada hecho está acompañado de un análisis sobre las causas, el desarrollo y las medidas tomadas posteriormente.",
        ultimaActualizacion: "2025-09-10",
        tipo: "Mayoria Simple",
        hechos: [3, 6, 9, 12, 14]
    },
    {
        id: 103,
        titulo: "Operativos y Allanamientos Policiales",
        descripcion: "Esta colección reúne hechos relacionados con operativos policiales, allanamientos y procedimientos de seguridad realizados en diferentes ciudades argentinas. Se detallan las estrategias utilizadas por las fuerzas de seguridad, los resultados obtenidos, el impacto en la comunidad y las repercusiones legales de cada intervención.",
        ultimaActualizacion: "2025-09-10",
        tipo: "Multiple Mencion",
        hechos: [1, 4, 10, 15]
    },
    {
        id: 104,
        titulo: "Desapariciones y Secuestros",
        descripcion: "Colección dedicada a hechos relacionados con desapariciones de personas y secuestros ocurridos en distintas regiones del país. Incluye detalles sobre los operativos de búsqueda, el trabajo de las fuerzas de seguridad, el impacto en las familias y la sociedad, así como el desenlace de cada caso y las acciones judiciales posteriores.",
        ultimaActualizacion: "2025-09-10",
        tipo: "Concenso Absoluto",
        hechos: [5, 8, 15]
    },
    {
        id: 105,
        titulo: "Incendios y Emergencias",
        descripcion: "Esta colección documenta hechos sobre incendios urbanos y forestales, emergencias y la labor de bomberos en diferentes ciudades argentinas. Se incluyen relatos sobre la magnitud de los siniestros, la respuesta de los equipos de emergencia, las pérdidas materiales y humanas, y las acciones preventivas implementadas para evitar futuros incidentes.",
        ultimaActualizacion: "2025-09-10",
        tipo: "Mayoria Simple",
        hechos: [3, 14]
    },
    {
        id: 106,
        titulo: "Criminalidad Urbana",
        descripcion: "Registro exhaustivo de hechos criminales urbanos, incluyendo robos, asaltos, estafas y tiroteos en las principales ciudades del país. Cada hecho está acompañado de información sobre el modus operandi de los delincuentes, la respuesta policial, el impacto en las víctimas y las medidas adoptadas para mejorar la seguridad ciudadana.",
        ultimaActualizacion: "2025-09-10",
        tipo: "Multiple Mencion",
        hechos: [1, 7, 10, 13]
    },
    {
        id: 107,
        titulo: "Accidentes de Transporte",
        descripcion: "Esta colección reúne hechos relacionados con accidentes de transporte público y privado, incluyendo colectivos, trenes y vehículos particulares. Se analizan las circunstancias de cada accidente, la intervención de los servicios de emergencia, las consecuencias para los involucrados y las acciones tomadas para mejorar la seguridad vial y ferroviaria.",
        ultimaActualizacion: "2025-09-10",
        tipo: "Concenso Absoluto",
        hechos: [2, 6, 9, 12]
    }
];
window.coleccionesDB = coleccionesDB;


// Colores de fallback
const fallbackColors = ["#2F60F4", "#1746D2", "#1A3FA0", "#3B6EFF", "#4A79FF", "#6A6AFF"];
