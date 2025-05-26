package ar.edu.utn.frba.dds.schedulers;

import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class HechoScheduler {
    private final IHechosService hechosService;

    public HechoScheduler(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @Scheduled(cron = "${cron.expression.prueba}")
    public void actualizarHechos() {
            hechosService.actualizarHechosScheduler();
    }
}
