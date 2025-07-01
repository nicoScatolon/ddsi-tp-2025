package ar.edu.utn.frba.dds.schedulers;

import ar.edu.utn.frba.dds.services.impl.ColeccionesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ConsensoScheduler {
    private final ColeccionesService coleccionesService;

    public ConsensoScheduler(ColeccionesService coleccionesService) {
        this.coleccionesService = coleccionesService;
    }

    @Scheduled(cron = "${cron.curacionColecciones}")
    public void actualizarHechos() {
        coleccionesService.curarColeccionesScheduler();
    }

}
