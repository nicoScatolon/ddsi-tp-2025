package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.*;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IFuentesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuentesService implements IFuentesService {
    private final IFuentesRepository fuentesRepository;
    private final IHechosService hechosService;
    private final IColeccionesService coleccionesService;


    private static final Logger logger = LoggerFactory.getLogger(FuentesService.class);

    public FuentesService(IFuentesRepository fuentesRepository, IHechosService hechosService, IColeccionesService coleccionesService) {
        this.fuentesRepository = fuentesRepository;
        this.hechosService = hechosService;
        this.coleccionesService = coleccionesService;
    }

    @Override
    public List<Fuente> buscarFuentes() {
        return fuentesRepository.findAll();
    }

    @Override
    public ResponseEntity<Void> agregarFuente(FuenteInputDTO fuenteDTO) {
        Fuente nuevaFuente = DTOConverter.fuenteDTOToFuente(fuenteDTO);
        this.loguearFuenteCargada(nuevaFuente);
        fuentesRepository.save(nuevaFuente);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> eliminarFuente(Long id) {
        Fuente fuente = this.buscarFuentePorId(id);
        if (fuente == null) {
            return ResponseEntity.notFound().build();
        }

        coleccionesService.notificarFuenteEliminada(this.buscarFuentePorId(id));
        fuentesRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public Fuente buscarFuentePorId(Long id) {
        return fuentesRepository.getById(id);
    }

    @Override
    public List<Fuente> buscarFuentePorTipo(TipoFuente tipoFuente){
        return this.buscarFuentes().stream().filter(fuente -> fuente.getTipo().equals(tipoFuente)).collect(Collectors.toList());
    }

    @Override
    public List<Fuente> buscarFuentePorTipo(List<TipoFuente> tiposFuente){
        return this.buscarFuentes().stream().filter(fuente -> tiposFuente.contains(fuente.getTipo())).collect(Collectors.toList());
    }

    @Override
    public void notificarEliminaciones (List<Hecho> hechosAEliminar){
        //TODO
        /*
        Map<Long, List<Hecho>> hechoBasesMap = hechosAEliminar.stream().collect(Collectors.groupingBy(Hecho::getIdFuente));

        for (Long idFuente : hechoBasesMap.keySet()){
            List <Long> idHechosAEliminar = hechoBasesMap.get(idFuente).stream().map(Hecho::getOrigenId).toList();
            //ToDO: fuente.notificareliminacion( idHechosAEliminar )
        }
        */
    }

    @Override
    public void actualizarHechosFuentesScheduler() {
        logger.info("Actualizar fuentes Scheduler");
        List<TipoFuente> listaTipos = new ArrayList<>();
        listaTipos.add(TipoFuente.DINAMICA);
        listaTipos.add(TipoFuente.ESTATICA);
        List<Fuente> fuentes = this.buscarFuentePorTipo(listaTipos);

        List<Fuente> fuentesActualizadas = new ArrayList<>();
        List<Hecho> hechosAActualizar = new ArrayList<>();
        for (Fuente fuente : fuentes){
            List<Hecho> hechosFuente = fuente.getTipo().crearAdapter(fuente).actualizarHechos();
            logger.info("Fuente actualizada {}", fuente.getTipo());
            if (hechosFuente != null && !hechosFuente.isEmpty()){
                //this.hechosService.actualizarHechosRepository(hechosFuente);
                hechosAActualizar.addAll(hechosFuente);
                fuentesActualizadas.add(fuente);
            }
        }
        this.hechosService.actualizarHechosRepository(hechosAActualizar);

        coleccionesService.notificarActualizacionFuentes(fuentesActualizadas);
    }

    private void loguearFuenteCargada(IFuente fuente){
        logger.info("Fuente cargada - ID: {} - URL: {} - Tipo de Fuente: {}"
                    ,fuente.getId(), fuente.getUrl(), fuente.getUrl());
    }
}
