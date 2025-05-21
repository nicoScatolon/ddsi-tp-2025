package ar.edu.utn.frba.dds.schedulers;

import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HechoScheduler {
    private final IHechosService hechosService;
    private final List<String> fuentesAutomaticas;

    public HechoScheduler(
            IHechosService hechosService,
            @Value("#{'${fuentes.automaticas}'.split(',')}") List<String> fuentesAutomaticas
    ) {
        this.hechosService = hechosService;
        this.fuentesAutomaticas = fuentesAutomaticas;
    }

    @Scheduled(cron = "${cron.expression.prueba}")
    public void actualizarHechos() {
        for (String url : fuentesAutomaticas) {
            hechosService.actualizarHechosFuente(url);
        }
    }
}
