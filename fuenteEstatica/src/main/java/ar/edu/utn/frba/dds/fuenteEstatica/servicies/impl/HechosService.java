package ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl;


import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.ImportadorHechos;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.IHechosService;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {
    private IHechosRepository hechosRepository;
    private Map<String, ImportadorHechos> importadores;
    public HechosService(IHechosRepository hechosRepository, List <ImportadorHechos> ListaImportadores) {
        this.hechosRepository = hechosRepository;
        this.importadores = ListaImportadores.stream()
                .collect(Collectors.toMap(
                        ImportadorHechos::getFormato,
                        Function.identity()
                ));
    }

    @Override
    public List<Hecho> importarArchivoHechos(String path){
        String ext = FilenameUtils.getExtension(path).toLowerCase();
        ImportadorHechos imp = importadores.get(ext);
        if (imp == null) {
            throw new IllegalArgumentException("Formato no soportado: ." + ext);
        }
        List<Hecho> hechos = imp.importarHechosArchivo(path);
        hechosRepository.saveAll(hechos);
        return hechos;
    }


    @Override
    public List<HechoOutputDTO> getAllHechosPorFecha(LocalDateTime fechaDeCarga) {
        List<Hecho> hechos;
        if(fechaDeCarga == null){
            hechos = hechosRepository.findAll();
        } else {
            hechos = hechosRepository.findByFechaDeCarga(fechaDeCarga); // eze no explicó como sumar al repo de JPA todavía
            // esto sería agregarle un where al select que iguale el campo de fecha de un hecho a la que se pasa por parametro
            //throw new RuntimeException("No se encontró la fecha de carga para el hecho.");
        }

        return hechos.stream().map(this::hechoToDTO).toList();
    }

    private HechoOutputDTO hechoToDTO(Hecho hecho){
        var dto = new HechoOutputDTO();
        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setUbicacion(hecho.getUbicacion());
        dto.setCategoria(hecho.getCategoria());
        dto.setFechaDeCarga(hecho.getFechaDeCarga());
        dto.setFechaDeOcurrencia(hecho.getFechaDeOcurrencia());
        dto.setId(hecho.getId());
        return dto;
    }
}
