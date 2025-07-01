package ar.edu.utn.frba.dds.schedulers;

import ar.edu.utn.frba.dds.services.IFuentesService;
import ar.edu.utn.frba.dds.services.impl.ColeccionesService;
import org.springframework.scheduling.annotation.Scheduled;

public class ConsensoScheduler {
    private final ColeccionesService coleccionesService;

    public ConsensoScheduler(ColeccionesService coleccionesService) {
        this.coleccionesService = coleccionesService;
    }

    @Scheduled(cron = "${curacionColecciones}")
    public void actualizarHechos() {
        coleccionesService.curarColeccionesScheduler();
    }

}
