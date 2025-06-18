package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.*;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.services.IFuentesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FuentesService implements IFuentesService {
    private final IFuentesRepository fuentesRepository;

    public FuentesService(IFuentesRepository fuentesRepository) {
        this.fuentesRepository = fuentesRepository;
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


    private IFuente fuenteDTOToFuente(FuenteInputDTO fuenteDTO) {
        IFuente fuente = fuenteDTO.getTipoFuente().crearFuente(fuenteDTO.getUrl());
        fuente.setNombre(fuenteDTO.getNombre());
        return fuente;
    }
}
