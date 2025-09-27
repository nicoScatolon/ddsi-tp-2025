package ar.edu.utn.frba.dds.fuenteproxy.schedulers;

import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteService;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import org.springframework.scheduling.annotation.Scheduled;

public class HechoScheduler {
    IHechosService hechosService;

    @Scheduled(cron = "${cron.expression.prueba}")
    public void actualizarHechos() {
        hechosService.actualizarHechosScheduler();
    }

}
