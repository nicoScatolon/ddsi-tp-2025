package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.FuentePreviewOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.*;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IFuentesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<FuentePreviewOutputDTO> getFuentesPreview(){
        return this.fuentesRepository.findAll().stream().map(DTOConverter::convertirFuentePreviewOutputDTO).toList();
    }

    @Override
    public List<Fuente> buscarFuentes() {
        return fuentesRepository.findAll();
    }

    @Override
    public ResponseEntity<Void> agregarFuente(FuenteInputDTO fuenteDTO) {
        Fuente nuevaFuente = DTOConverter.fuenteOutputDTOToFuente(fuenteDTO);
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
        return fuentesRepository.findById(id).orElse(null);
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

    @Transactional
    @Override
    public void actualizarHechosFuentesScheduler() {
        logger.info("Actualizar fuentes Scheduler");
        List<Fuente> fuentes = this.fuentesRepository.findAll();

        List<Fuente> fuentesActualizadas = new ArrayList<>();
        List<Hecho> hechosAActualizar = new ArrayList<>();
        for (Fuente fuente : fuentes){
            try {
                List<Hecho> hechosPersistidosFuenteActual = this.hechosService.findByFuente(fuente);

                List<Hecho> hechosFuente = fuente.getTipo().crearAdapter(fuente).actualizarHechos(hechosPersistidosFuenteActual);
                logger.info("Fuente {} actualizada con {} hechos", fuente.getId(), hechosFuente.size());

                if (!hechosFuente.isEmpty()){
                    hechosAActualizar.addAll(hechosFuente);
                    fuentesActualizadas.add(fuente);
                }
            } catch (Exception e) {
                logger.info("No se pudo conectar a la fuente, error: {}", e.getMessage());
            }
        }
        if (!hechosAActualizar.isEmpty()){
            this.hechosService.actualizarHechosRepository(hechosAActualizar);
        }
        if (!fuentesActualizadas.isEmpty()){
            coleccionesService.notificarActualizacionFuentes(fuentesActualizadas);
        }

    }

    public List<HechoOutputDTO> testActualizarFuente(Long idFuente){
        Fuente fuente = fuentesRepository.findById(idFuente).orElseThrow();
        List<Hecho> hechosPersistidosFuenteActual = this.hechosService.findByFuente(fuente);
        List<Hecho> hechosFuente = fuente.getTipo().crearAdapter(fuente).actualizarHechos(hechosPersistidosFuenteActual);
        logger.info("Fuente actualizada {}", fuente.getTipo());

        if (hechosFuente != null && !hechosFuente.isEmpty()){
            this.hechosService.actualizarHechosRepository(hechosFuente);
        }
        return DTOConverter.hechoOutputDTO(hechosFuente);
    }

    private void loguearFuenteCargada(IFuente fuente){
        logger.info("Fuente cargada - ID: {} - URL: {} - Tipo de Fuente: {}"
                    ,fuente.getId(), fuente.getUrl(), fuente.getUrl());
    }
}
