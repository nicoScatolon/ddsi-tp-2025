package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import ar.edu.utn.frba.dds.domain.repository.IEtiquetasRepository;
import ar.edu.utn.frba.dds.services.IEtiquetasService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtiquetasService implements IEtiquetasService {
    public final IEtiquetasRepository etiquetasRepository;

    public  EtiquetasService(IEtiquetasRepository etiquetasRepository) {
        this.etiquetasRepository = etiquetasRepository;
    }

    @Override
    public Etiqueta crearEtiqueta(String nombreEtiqueta) {
        Etiqueta nuevaEtiqueta = new Etiqueta(nombreEtiqueta);
        etiquetasRepository.save(nuevaEtiqueta);
        return nuevaEtiqueta;
    }

    @Override
    public Etiqueta verificarEtiqueta(String nombreEtiqueta) {
        Optional<Etiqueta> optionalEtiqueta = etiquetasRepository.findAll().stream().filter(e -> e.getNombre().equals(nombreEtiqueta)).findFirst();
        return optionalEtiqueta.orElseGet(() -> crearEtiqueta(nombreEtiqueta));
    }

    @Override
    public List<Etiqueta> findAll() {
        return etiquetasRepository.findAll();
    }

    @Override
    public List<String> findAllNombres() {
        return etiquetasRepository.findAllNombres();
    }
}
