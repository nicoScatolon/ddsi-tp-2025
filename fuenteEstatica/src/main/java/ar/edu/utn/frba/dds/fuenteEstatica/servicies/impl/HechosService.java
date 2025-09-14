package ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl;


import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.FormatoFuente;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Fuente;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.ImportadorHechos;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.repository.IFuenteRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.IHechosService;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FilenameUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {
    private IHechosRepository hechosRepository;
    private final IFuenteRepository fuenteRepository;
    private final List<ImportadorHechos> importadores;
    public HechosService(IHechosRepository hechosRepository,
                         IFuenteRepository fuenteRepository,
                         List<ImportadorHechos> listaImportadores) {
        this.hechosRepository = hechosRepository;
        this.fuenteRepository = fuenteRepository;
        this.importadores = List.copyOf(listaImportadores);
    }

    @Override
    public List<Hecho> importarArchivoHechos(String path) {
        String uriAbs = toAbs(path);
        FormatoFuente formato = FormatoFuente.fromPath(uriAbs);

        // seleccionar importador con default supports(...)
        ImportadorHechos imp = importadores.stream()
                .filter(i -> i.supports(formato))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No hay importador para " + formato));

        // get-or-create de Fuente por (tipo, uri)
        Fuente fuente = fuenteRepository.findByTipoAndUri(formato, uriAbs)
                .orElseGet(() -> {
                    Fuente f = new Fuente();
                    f.setTipo(formato);
                    f.setUri(uriAbs);
                    f.setEstrategia(formato.estrategiaKey());
                    f.setNombre(normalizarNombreDesdeUri(uriAbs));
                    return fuenteRepository.save(f);
                });
        List<Hecho> hechos = imp.importarHechosArchivo(uriAbs);

        for (Hecho h : hechos) {
            h.setArchivoOrigen(uriAbs);
            h.setFuente(fuente);
        }

        return hechosRepository.saveAll(hechos);
    }


    private String toAbs(String p) {
        try { return java.nio.file.Paths.get(p).toAbsolutePath().toString(); }
        catch (java.nio.file.InvalidPathException e) { return p; } // s3://, http://, etc.
    }

    @Override
    public List<HechoOutputDTO> getAllHechosPorFecha(LocalDateTime fechaDeCarga) {
        List<Hecho> hechos;
        if(fechaDeCarga == null){
            hechos = hechosRepository.findAll();
        } else {
            hechos = hechosRepository.findByFechaDeCargaAfter(fechaDeCarga);
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


    private String normalizarNombreDesdeUri(String uri) {
        String base = org.apache.commons.io.FilenameUtils.getBaseName(uri); // sin extensión
        String limpio = base
                .replaceAll("[\\[\\]\\(\\)]", " ")          // saca brackets
                .replaceAll("[_\\-\\.]+", " ")               // _, -, . a espacio
                .trim()
                // tirá sufijos de versión/periodo al final
                .replaceAll("(?i)(?:\\b(20\\d{2}|19\\d{2}|q[1-4]|v\\d+|rev\\d+|final|def|ok|corrigido|backup|copia)\\b)+\\s*$", "")
                // fechas tipo 2025-09-14, 14-09-2025, 20250914 al final
                .replaceAll("(?i)(\\b\\d{4}[\\-\\/_]?\\d{2}[\\-\\/_]?\\d{2}\\b|\\b\\d{2}[\\-\\/_]?\\d{2}[\\-\\/_]?\\d{4}\\b|\\b\\d{8}\\b)\\s*$", "")
                .replaceAll("\\s{2,}", " ")
                .trim();
        return toTitleCaseEs(limpio.isEmpty() ? base : limpio);
    }

    private String toTitleCaseEs(String s) {
        Locale es = new Locale("es", "AR");
        String[] stop = {"de","del","la","las","los","y","o","u","en","para","por","a","al"};
        Set<String> minor = new HashSet<>(Arrays.asList(stop));
        String[] tokens = s.toLowerCase(es).split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            String t = tokens[i];
            if (i != 0 && minor.contains(t)) continue;
            if (!t.isEmpty()) tokens[i] = t.substring(0,1).toUpperCase(es) + t.substring(1);
        }
        return String.join(" ", tokens).trim();
    }
}
