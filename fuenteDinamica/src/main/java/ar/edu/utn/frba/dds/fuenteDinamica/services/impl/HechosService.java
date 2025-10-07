package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.ContribuyenteInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.*;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repository.IHechosRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IHechosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<HechoOutputDTO> getHechos(LocalDateTime fechaDeCarga) {
        List<Hecho> hechosAEnviar = this.hechosRepository.findByEstadoOrderByFechaDeCargaDesc(EstadoHecho.ACEPTADO);

        if (fechaDeCarga !=null){
            hechosAEnviar = hechosAEnviar.stream().filter(h -> h.getFechaDeCarga().isAfter(fechaDeCarga)).toList();
        }

        return hechosAEnviar.stream().map(this::hechoOutputDTO).toList();
    }

    @Transactional
    public void cargarHecho(HechoInputDTO dto) {
        Hecho h = hechoInputDTO(dto);
        h.setFechaDeCarga(LocalDateTime.now());
        hechosRepository.save(h);
    }

    @Override
    public Hecho modificarHecho(HechoInputDTO hechoInputDTO) {
        Optional<Hecho> optionalHechoGuardado = this.hechosRepository.findById(hechoInputDTO.getId());
        Hecho hechoNuevo = hechoInputDTO(hechoInputDTO);
        if (optionalHechoGuardado.isPresent()) {
            Hecho hechoGuardado = optionalHechoGuardado.get();

            if (hechoNuevo.getId() == null) { throw new IllegalArgumentException("El hecho no existe, falta id"); }
            if (hechoNuevo.getContribuyente().getId() == null) { throw new IllegalArgumentException("El contribuyente modificador no esta registrado"); }
            if (!hechoNuevo.getContribuyente().getId().equals(hechoGuardado.getContribuyente().getId()))
            { throw new IllegalArgumentException("El contribuyente modificador no es el creador del hecho");}

            hechoGuardado.serModificado(hechoNuevo, diasValidosModificacion);
            this.hechosRepository.save(hechoGuardado);
            return hechoGuardado;

        } else {
           return null;//TODO responder 404
        }
    }

    @Override
    public Hecho revisarHecho(Long idHecho, Long idAdmin, EstadoHecho nuevoEstado, String sugerencia) {
        Optional<Hecho> optionalHecho = this.hechosRepository.findById(idHecho);
        if (optionalHecho.isPresent()) {
            Hecho hecho = optionalHecho.get();
            hecho.serRevisado(idAdmin, nuevoEstado, sugerencia);
            this.hechosRepository.save(hecho);
            return hecho;
        }
        else {
            return null; //TODO responder 404
        }
    }



    //Metodos privados

    private Hecho hechoInputDTO(HechoInputDTO hechoDTO) {
        return Hecho.builder()
                .titulo(hechoDTO.getTitulo())
                .descripcion(hechoDTO.getDescripcion())
                .categoria(this.categoriaInputDTO(hechoDTO.getCategoria()))
                .ubicacion(this.ubicacionInputDTO(hechoDTO.getUbicacion()))
                .fechaDeOcurrencia(hechoDTO.getFechaDeOcurrencia())
                .contenidoMultimedia(hechoDTO.getContenidoMultimedia())
                .contribuyente(this.contribuyenteDTO(hechoDTO.getContribuyente()))
                .cargadoAnonimamente(hechoDTO.getCargadoAnonimamente())
                .build();
    }

    private Contribuyente contribuyenteDTO(ContribuyenteInputDTO contribuyenteDTO) {
        return Contribuyente.builder()
                .id(contribuyenteDTO.getId())
                .nombre(contribuyenteDTO.getNombre())
                .apellido(contribuyenteDTO.getApellido())
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
                .idLocal(hecho.getId())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoriaOutputDTO(this.categoriaOutputDTO(hecho.getCategoria()))
                .ubicacionOutputDTO(this.ubicacionOutputDTO(hecho.getUbicacion()))
                .fechaOcurrencia(hecho.getFechaDeOcurrencia())
                .fechaCarga(hecho.getFechaDeCarga())
                .contenidoMultimedia(hecho.getContenidoMultimedia())
                .cargadoAnonimamente(hecho.getCargadoAnonimamente())
                .contribuyente(hecho.getContribuyente())
                .estado(hecho.getEstado())
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
}
