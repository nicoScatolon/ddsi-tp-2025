package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.entities.Fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISchedulerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerService implements ISchedulerService {
    private IHechosService hechosService;
    private IFuentesRepository fuentesRepository;

    public SchedulerService(IHechosService hechosService, IFuentesRepository fuentesRepository) {
        this.hechosService = hechosService;
        this.fuentesRepository = fuentesRepository;
    }

    @Override
    @Scheduled(cron = "0 0 * * * *")
    public void actualizarHechos() {
        List<Fuente> fuentes = fuentesRepository.obtenerFuentes();;
        hechosService.obtenerTodosLasHechos(fuentes);
    }
}
