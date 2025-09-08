package ar.edu.utn.frba.dds.schedulers;

import ar.edu.utn.frba.dds.services.IFuentesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.impl.ColeccionesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class HechoScheduler {
    private final IFuentesService fuentesService;
    private final ColeccionesService coleccionesService;

    private final Object lock = new Object();

    public HechoScheduler(IFuentesService fuentesService, ColeccionesService coleccionesService) {
        this.fuentesService = fuentesService;
        this.coleccionesService = coleccionesService;
    }

    @Scheduled(cron = "${cron.expression.prueba1}")
    public void actualizarHechos() {
        synchronized (lock){
            fuentesService.actualizarHechosFuentesScheduler();
        }

    }
    @Scheduled(cron = "${cron.expression.prueba2}")
    public void actualizarColecciones() {
        synchronized (lock){
            coleccionesService.actualizarColeccionesScheduler();
        }

    }
}
