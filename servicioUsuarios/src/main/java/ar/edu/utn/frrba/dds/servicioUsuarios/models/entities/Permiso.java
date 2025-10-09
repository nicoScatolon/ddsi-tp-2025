package ar.edu.utn.frrba.dds.servicioUsuarios.models.entities;

public enum Permiso {
    // Visualizador
    VER_COLECCIONES,
    VER_HECHOS,
    FILTRAR_HECHOS,
    VER_DETALLE_HECHO,

    SOLICITAR_ELIMINACION, // Visualizador y Contribuyente
    SELECCIONAR_MODO_NAVEGACION // Visualizador y Contribuyente

    // Contribuyente
    CREAR_HECHO,
    EDITAR_HECHO_PROPIO,
    SUBIR_MULTIMEDIA,

    // Admin
    VER_DASHBOARD_ADMIN,
    GESTIONAR_COLECCIONES,
    GESTIONAR_CATEGORIAS,
    CONFIGURAR_FUENTES,
    CONFIGURAR_CONSENSO,
    MODERAR_HECHO,
    GESTIONAR_SOLICITUDES,
    IMPORTAR_CSV,

    // Estadísticas
    VER_ESTADISTICAS
}
