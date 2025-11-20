package ar.edu.utn.frba.dds.schedulers;

import ar.edu.utn.frba.dds.services.IFuentesService;
import ar.edu.utn.frba.dds.services.impl.ColeccionesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class HechoScheduler {
    private final IFuentesService fuentesService;
    private final ColeccionesService coleccionesService;

    public HechoScheduler(IFuentesService fuentesService, ColeccionesService coleccionesService) {
        this.fuentesService = fuentesService;
        this.coleccionesService = coleccionesService;
    }

    @Scheduled(cron = "${cron.fuentes.actualizar}")
    public void actualizarHechos() {
        fuentesService.actualizarHechosFuentesScheduler();
    }

    @Scheduled(cron = "${cron.colecciones.actualizar}")
    public void actualizarColecciones() {
        coleccionesService.actualizarColeccionesScheduler();
    }
}
