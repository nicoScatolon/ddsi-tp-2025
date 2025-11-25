package ar.edu.utn.frba.dds.fuenteDinamica.schedulers;

import ar.edu.utn.frba.dds.fuenteDinamica.services.impl.HechosService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HechoScheduler {
    private final HechosService hechosService;

    public HechoScheduler(HechosService hechosService) { this.hechosService = hechosService; }

    @Scheduled(cron = "${cron.hechos.rechazarSugerencia}")
    public void rechazarHechosSugerencias(){ hechosService.rechazarHechosSugerenciasScheduler(); }
}
