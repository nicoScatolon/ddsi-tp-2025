package ar.edu.utn.frba.dds.schedulers;

import ar.edu.utn.frba.dds.services.IEstadisticasService;
import org.springframework.scheduling.annotation.Scheduled;

public class EstadisticasScheduler {
    private final IEstadisticasService estadisticasService;

    public EstadisticasScheduler(IEstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @Scheduled(cron = "${cron.calculoEstadisticas}")
    public void actualizarEstadisticas() {
        estadisticasService.generarEstadisticas();
    }
    @Scheduled(cron = "${cron.eliminarEstadisticas}")
    public void eliminarEstadisticasAntiguas() {
        estadisticasService.eliminarEstadisticasAntiguas();
    }
}
