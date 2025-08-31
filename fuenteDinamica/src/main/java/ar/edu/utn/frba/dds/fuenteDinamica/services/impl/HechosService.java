package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.ContribuyenteInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.ModificarHechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repository.IHechosRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IHechosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private ICategoriaService categoriaService;

    @Value("${hecho.diasModificacion}")
    private Long diasValidosModificacion;

    public HechosService(IHechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    @Override
    public void cargarHecho(ModificarHechoInputDTO inputDTO) {
        Hecho hecho = hechoInputDTO(inputDTO.getHechoInputDTO(), inputDTO.getContribuyenteInputDTO());
        hecho.setFechaDeCarga(LocalDateTime.now());
        this.hechosRepository.save(hecho);
    }

    @Override
    public void modificarHecho(ModificarHechoInputDTO inputDTO) {
        Hecho hechoGuardado = this.hechosRepository.findById(inputDTO.getHechoInputDTO().getId());
        Hecho hechoNuevo = hechoInputDTO(inputDTO.getHechoInputDTO(), inputDTO.getContribuyenteInputDTO());

        if (hechoNuevo.getId() == null) { throw new IllegalArgumentException("El hecho no existe, falta id"); }
        if (hechoNuevo.getContribuyente().getId() == null) { throw new IllegalArgumentException("El contribuyente modificador no esta registrado"); }
        if (!hechoNuevo.getContribuyente().getId().equals(hechoGuardado.getContribuyente().getId()))
        { throw new IllegalArgumentException("El contribuyente modificador no es el creador del hecho");}


        hechoGuardado.serModificado(hechoNuevo, diasValidosModificacion);
        this.hechosRepository.save(hechoGuardado);
    }

    @Override
    public Hecho revisarHecho(Long idHecho, Long idAdmin, EstadoHecho nuevoEstado, String sugerencia) {
        Hecho hecho = this.hechosRepository.findById(idHecho);
        hecho.serRevisado(idAdmin, nuevoEstado, sugerencia);
        this.hechosRepository.save(hecho);
        return hecho;
    }

    public List<HechoOutputDTO> getHechos(LocalDateTime fechaDeCarga) {
        List<Hecho> hechosAEnviar = this.hechosRepository.findAll().stream().filter(h -> h.getEstado().equals(EstadoHecho.ACEPTADO)).toList();

        if (fechaDeCarga !=null){
            hechosAEnviar = hechosAEnviar.stream().filter(h -> h.getFechaDeCarga().isAfter(fechaDeCarga)).toList();
        }

        return hechosAEnviar.stream().map(this::hechoOutputDTO).toList();
    }

    //Metodos privados

    private Hecho hechoInputDTO(HechoInputDTO hechoDTO, ContribuyenteInputDTO contribuyenteDTO) {
        return Hecho.builder()
                .titulo(hechoDTO.getTitulo())
                .descripcion(hechoDTO.getDescripcion())
                .categoria(hechoDTO.getCategoria())
                .ubicacion(hechoDTO.getUbicacion())
                .fechaDeOcurrencia(hechoDTO.getFechaDeOcurrencia())
                .contenidoMultimedia(hechoDTO.getContenidoMultimedia())
                .contribuyente(this.contribuyenteDTO(contribuyenteDTO))
                .build();
    }

    private Contribuyente contribuyenteDTO(ContribuyenteInputDTO contribuyenteDTO) {
        return Contribuyente.builder()
                .id(contribuyenteDTO.getId())
                .nombre(contribuyenteDTO.getNombre())
                .apellido(contribuyenteDTO.getApellido())
                .fechaNacimiento(contribuyenteDTO.getFechaNacimiento())
                .esAnonimo(contribuyenteDTO.getEsAnonimo())
                .build();
    }

    private HechoOutputDTO hechoOutputDTO(Hecho hecho) {
        return HechoOutputDTO.builder()
                .idLocal(hecho.getId())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoriaOutputDTO(this.categoriaOutputDTO(hecho.getCategoria()))
                .ubicacion(hecho.getUbicacion())
                .fechaOcurrencia(hecho.getFechaDeOcurrencia())
                .fechaCarga(hecho.getFechaDeCarga())
                .contenidoMultimedia(hecho.getContenidoMultimedia())
                .contribuyente(hecho.getContribuyente())
                .build();
    }

    private CategoriaOutputDTO categoriaOutputDTO(String nombreCategoria) {
        CategoriaOutputDTO categoriaOutputDTO = new CategoriaOutputDTO();
        categoriaOutputDTO.setId(this.categoriaService.obtenerIdCategoria(nombreCategoria));
        categoriaOutputDTO.setNombreCategoria(nombreCategoria);
        return categoriaOutputDTO;
    }
}
