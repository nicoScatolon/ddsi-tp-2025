package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.RevisionHechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.ContenidoMultimediaOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.*;
import ar.edu.utn.frba.dds.fuenteDinamica.models.exceptions.ModificacionNoPermitidaException;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repository.IHechosRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IHechosService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private final ICategoriaService categoriaService;

    public HechosService(IHechosRepository hechosRepository,
                         ICategoriaService categoriaService) {
        this.hechosRepository = hechosRepository;
        this.categoriaService = categoriaService;
    }
    @Value("${hecho.diasModificacion}")
    private Long diasValidosModificacion;

    @Value("${app.pagination.hechos.size}")
    private Integer pageSize;

    @Override
    public List<HechoOutputDTO> getHechos(LocalDateTime fechaDeCarga, EstadoHecho estado, Integer page) {
        Specification<Hecho> spec = Specification.where(null);
        if (fechaDeCarga != null) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                        cb.greaterThan(root.get("fechaDeCarga"), fechaDeCarga),
                        cb.greaterThan(root.get("fechaDeModificacion"), fechaDeCarga)
            ));
        }
        if (estado != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("estado"), estado));
        }

        if (page == null) {
            return hechosRepository.findAll(spec).stream()
                    .map(this::hechoOutputDTO)
                    .toList();
        }

        Pageable pageable = PageRequest.of(page, pageSize);

        return hechosRepository.findAll(spec, pageable)
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
    }

    @Override
    public ResponseEntity<HechoOutputDTO> getHechoById(Long idHecho) {
        Hecho hechoBuscado = this.hechosRepository.findById(idHecho).orElse(null);
        if (hechoBuscado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hechoOutputDTO(hechoBuscado));
    }

    @Transactional
    public void cargarHecho(HechoInputDTO dto) {
        Hecho h = hechoInputDTO(dto);
        h.setFechaDeCarga(LocalDateTime.now());
        hechosRepository.save(h);
    }

    @Override
    @Transactional
    public Hecho modificarHecho(HechoInputDTO dto) {
        Hecho hechoGuardado = this.hechosRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Hecho no encontrado"));

        if (dto.getContribuyenteId() == null) {
            throw new IllegalArgumentException("El contribuyente modificador no está registrado");
        }

        if (!dto.getContribuyenteId().equals(hechoGuardado.getContribuyenteId())) {
            throw new ModificacionNoPermitidaException("El contribuyente modificador no es el creador del hecho");
        }

        hechoGuardado.serModificado(hechoInputDTO(dto), diasValidosModificacion);
        return hechosRepository.save(hechoGuardado);
    }


    @Override
    public List<HechoOutputDTO> getHechosUsuario (Long userId, EstadoHecho estado, Integer page) {
        Specification<Hecho> spec = Specification.where(null);
        if (userId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("contribuyenteId"), userId));
        }
        if (estado != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("estado"), estado));
        }

        if (page == null) {
            return hechosRepository.findAll(spec).stream().map(this::hechoOutputDTO).toList();
        }
        Pageable pageable = PageRequest.of(page, pageSize);

        return this.hechosRepository.findAll(spec, pageable).stream()
                .map(this::hechoOutputDTO)
                .toList();
    }


    // API privada //

    @Override
    public List<HechoOutputDTO> getHechosForAgregador(LocalDateTime fechaDeGestion) {
        // Devuelvo los hechos segun los necesita el agregador
        // Solo devuelvo Aceptados que tengan aceptacion posterior a la fecha de carga pasada por parametro

        Specification<Hecho> spec = Specification.where(null);
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("estado"), EstadoHecho.ACEPTADO));

        if (fechaDeGestion != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThan(root.get("fechaDeGestion"), fechaDeGestion));
        }

        return hechosRepository.findAll(spec)
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
    }

    @Override
    @Transactional
    public ResponseEntity<HechoOutputDTO> revisarHecho(Long idAdmin, RevisionHechoInputDTO revisionHechoDTO) {
        Long idHecho = revisionHechoDTO.getIdHecho();
        EstadoHecho nuevoEstado = revisionHechoDTO.getNuevoEstado();
        String sugerencia = revisionHechoDTO.getSugerencia();

        Optional<Hecho> optionalHecho = this.hechosRepository.findById(idHecho);
        if (optionalHecho.isPresent()) {
            Hecho hecho = optionalHecho.get();
            hecho.serRevisado(idAdmin, nuevoEstado, sugerencia);
            this.hechosRepository.save(hecho);
            return ResponseEntity.ok(hechoOutputDTO(hecho));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }


    //Metodos privados
    private Hecho hechoInputDTO(HechoInputDTO hechoDTO) {
        return Hecho.builder()
                .id(hechoDTO.getId())
                .titulo(hechoDTO.getTitulo())
                .descripcion(hechoDTO.getDescripcion())
                .categoria(this.categoriaInputDTO(hechoDTO.getCategoria()))
                .ubicacion(this.ubicacionInputDTO(hechoDTO.getUbicacion()))
                .fechaDeOcurrencia(hechoDTO.getFechaDeOcurrencia())
                .contenidoMultimedia(hechoDTO.getContenidoMultimedia())
                .contribuyenteId(hechoDTO.getContribuyenteId())
                .cargadoAnonimamente(hechoDTO.getCargadoAnonimamente() != null ? hechoDTO.getCargadoAnonimamente() : true )
                .build();
    }

    private Categoria categoriaInputDTO(CategoriaInputDTO categoriaDTO){
        Categoria categoria = new Categoria();
        if(categoriaDTO.getNombre() == null){ throw new IllegalArgumentException("Falta el nombre de la categoria"); }
        categoria.setNombre(categoriaDTO.getNombre());
        if (categoriaDTO.getId() != null) {categoria.setId(categoriaDTO.getId());}
        else {categoria.setId(categoriaService.obtenerIdCategoria(categoria.getNombre()));}
        return categoria;
    }

    private HechoOutputDTO hechoOutputDTO(Hecho hecho) {
        return HechoOutputDTO.builder()
                .id(hecho.getId())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(this.categoriaOutputDTO(hecho.getCategoria()))
                .ubicacion(this.ubicacionOutputDTO(hecho.getUbicacion()))
                .fechaDeOcurrencia(hecho.getFechaDeOcurrencia())
                .fechaDeCarga(hecho.getFechaDeCarga())
                .contenidoMultimedia(this.contenidoMultimediaOutputDTO(hecho.getContenidoMultimedia()))
                .cargadoAnonimamente(hecho.getCargadoAnonimamente())
                .fechaDeModificacion(hecho.getFechaDeModificacion())
                .contribuyenteId(hecho.getContribuyenteId())
                .estado(hecho.getEstado())
                .sugerencia(hecho.getSugerencia())
                .build();
    }

    private Ubicacion ubicacionInputDTO(UbicacionInputDTO ubicacionDTO){
        if(ubicacionDTO == null) return null;
        return Ubicacion.builder()
                .provincia(ubicacionDTO.getProvincia())
                .departamento(ubicacionDTO.getDepartamento())
                .calle(ubicacionDTO.getCalle())
                .numero(ubicacionDTO.getNumero())
                .latitud(ubicacionDTO.getLatitud())
                .longitud(ubicacionDTO.getLongitud())
                .build();
    }

    private UbicacionOutputDTO ubicacionOutputDTO(Ubicacion ubicacion) {
        if (ubicacion == null) return null;
        return UbicacionOutputDTO.builder()
                .provincia(ubicacion.getProvincia())
                .departamento(ubicacion.getDepartamento())
                .calle(ubicacion.getCalle())
                .numero(ubicacion.getNumero())
                .latitud(ubicacion.getLatitud())
                .longitud(ubicacion.getLongitud())
                .build();
    }

    private CategoriaOutputDTO categoriaOutputDTO(Categoria categoria) {
        CategoriaOutputDTO categoriaOutputDTO = new CategoriaOutputDTO();
        categoriaOutputDTO.setId(categoria.getId());
        categoriaOutputDTO.setNombre(categoria.getNombre());
        return categoriaOutputDTO;
    }

    private List<ContenidoMultimediaOutputDTO> contenidoMultimediaOutputDTO(List<ContenidoMultimedia> contenidosMultimedia) {
        List<ContenidoMultimediaOutputDTO> contenidoMultimediaOutputDTO = new ArrayList<>();

        contenidosMultimedia.forEach(contenidoMultimedia -> {
            ContenidoMultimediaOutputDTO contenidoMultimediaOutputDTO1 = ContenidoMultimediaOutputDTO.builder()
                    .id(contenidoMultimedia.getId())
                    .url(contenidoMultimedia.getUrl())
                    .tipoContenido(contenidoMultimedia.getTipoContenido())
                    .descripcion(contenidoMultimedia.getDescripcion())
                    .build();

            contenidoMultimediaOutputDTO.add(contenidoMultimediaOutputDTO1);
        });

        return contenidoMultimediaOutputDTO;
    }

    // Test

    public void crearHechoTest (HechoInputDTO dto) {
        Hecho h = hechoInputDTO(dto);
        h.setFechaDeCarga(LocalDateTime.now());
        h.setEstado(EstadoHecho.ACEPTADO);
        hechosRepository.save(h);
    }

}
