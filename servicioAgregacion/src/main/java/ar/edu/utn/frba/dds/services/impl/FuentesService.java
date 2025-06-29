package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.*;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IFuentesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FuentesService implements IFuentesService {
    private final IFuentesRepository fuentesRepository;
    private final IHechosService hechosService;
    private final IColeccionesService coleccionesService;

    public FuentesService(IFuentesRepository fuentesRepository, IHechosService hechosService, IColeccionesService coleccionesService) {
        this.fuentesRepository = fuentesRepository;
        this.hechosService = hechosService;
        this.coleccionesService = coleccionesService;
    }

    @Override
    public List<IFuente> buscarFuentes() {
        return fuentesRepository.findAll();
    }

    @Override
    public void agregarFuente(FuenteInputDTO fuenteDTO) {
        IFuente nuevaFuente = this.fuenteDTOToFuente(fuenteDTO);
        fuentesRepository.saveFuente(nuevaFuente);
    }

    @Override
    public void eliminarFuente(Long id) {
        fuentesRepository.deleteFuente(id);
        //ToDO eliminar de las colecciones las fuentes
    }

    @Override
    public IFuente buscarFuentePorId(Long id) {
        return fuentesRepository.findById(id);
    }

    @Override
    public List<IFuente> buscarFuentePorTipo(TipoFuente tipoFuente){
        return this.buscarFuentes().stream().filter(fuente -> fuente.getTipo().equals(tipoFuente)).collect(Collectors.toList());
    }

    @Override
    public List<IFuente> buscarFuentePorTipo(List<TipoFuente> tiposFuente){
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
        List<TipoFuente> listaTipos = new ArrayList<>();
        listaTipos.add(TipoFuente.DINAMICA);
        listaTipos.add(TipoFuente.ESTATICA);
        List<IFuente> fuentes = this.buscarFuentePorTipo(listaTipos);
        //TODO ver como setearlo en las properties
        //TODO REHACER
        List<IFuente> fuentesActualizadas = new ArrayList<>();
        for (IFuente fuente : fuentes){
            List<Hecho> hechosFuente = fuente.getTipo().crearAdapter().actualizarHechos();
            if (!hechosFuente.isEmpty()){
                this.hechosService.actualizarHechos(hechosFuente);
                fuentesActualizadas.add(fuente);
            }
        }
        coleccionesService.notificarActualizacionFuentes(fuentesActualizadas);
    }


    private IFuente fuenteDTOToFuente(FuenteInputDTO fuenteDTO) {
        IFuente fuente = fuenteDTO.getTipoFuente().crearFuente(fuenteDTO.getUrl());
        fuente.setNombre(fuenteDTO.getNombre());
        return fuente;
    }
}
