package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.*;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.services.IFuentesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuentesService implements IFuentesService {
    private final IFuentesRepository fuentesRepository;

    public FuentesService(IFuentesRepository fuentesRepository) {
        this.fuentesRepository = fuentesRepository;
    }

    @Override
    public List<Fuente> buscarFuentes() {
        return fuentesRepository.findAll();
    }

    @Override
    public void agregarFuente(FuenteDTO fuenteDTO) {
        Fuente nuevaFuente = this.fuenteDTOToFuente(fuenteDTO);
        fuentesRepository.agregarFuente(nuevaFuente);
    }

    @Override
    public void eliminarFuente(Long id) {
        fuentesRepository.eliminarFuente(id);
    }

    @Override
    public Fuente buscarFuentePorId(Long id) {
        return fuentesRepository.findById(id);
    }

    @Override
    public List<Fuente> buscarFuentePorTipo(TipoFuente tipoFuente){
        return this.buscarFuentes().stream().filter(fuente -> fuente.getTipo().equals(tipoFuente)).collect(Collectors.toList());
    }

    public List<Fuente> buscarFuentePorTipo(List<TipoFuente> tiposFuente){
        return this.buscarFuentes().stream().filter(fuente -> tiposFuente.contains(fuente.getTipo())).collect(Collectors.toList());
    }

    private Fuente fuenteDTOToFuente(FuenteDTO fuenteDTO) {
        Fuente fuente;
        switch (fuenteDTO.getTipoFuente()){
            case ESTATICA -> fuente = new FuenteEstatica();
            case PROXY -> fuente = new FuenteProxy();
            case DINAMICA -> fuente = new FuenteDinamica();
            default -> throw new IllegalArgumentException("Tipo de fuente no valido");
        }

        fuente.setUrl(fuenteDTO.getUrl());
        fuente.setNombre(fuenteDTO.getNombre());
        return fuente;
    }
}
