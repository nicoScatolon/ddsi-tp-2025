package ar.edu.utn.frba.dds.schedulers;

import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.impl.ColeccionesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class HechoScheduler {
    private final IHechosService hechosService;
    private final ColeccionesService coleccionesService;

    public HechoScheduler(IHechosService hechosService, ColeccionesService coleccionesService) {
        this.hechosService = hechosService;
        this.coleccionesService = coleccionesService;
    }

    @Scheduled(cron = "${cron.expression.prueba}")
    public void actualizarHechos() {
            hechosService.actualizarHechosScheduler();
    }
    @Scheduled(cron = "${cron.expression.prueba}")
    public void actualizarColecciones() {
        coleccionesService.actualizarColeccionesScheduler();
    }
}
