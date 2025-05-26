package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.ContribuyenteInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repository.IHechosRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IHechosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {
    private IHechosRepository hechosRepository;

    @Value("${hecho.diasModificacion}")
    private Long diasValidosModificacion;

    public HechosService(IHechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    @Override
    public Hecho cargarHecho(HechoInputDTO hechoDTO, ContribuyenteInputDTO contribuyenteDTO) {
        Hecho hecho = hechoInputDTO(hechoDTO, contribuyenteDTO);
        hecho.setFechaDeCarga(LocalDateTime.now());
        return this.hechosRepository.save(hecho);
    }

    @Override
    public Hecho modificarHecho(HechoInputDTO hechoDTO, ContribuyenteInputDTO contribuyenteDTO) {
        Hecho hechoGuardado = this.hechosRepository.findById(hechoDTO.getId());
        Hecho hechoNuevo = hechoInputDTO(hechoDTO, contribuyenteDTO);
        var fechaModificacion = LocalDateTime.now();

        if (hechoGuardado.getContribuyente().getId() == null) {throw new IllegalArgumentException("El hecho no es modificable"); }
        if (hechoNuevo.getId() == null) { throw new IllegalArgumentException("El hecho no existe, falta id"); }
        if (hechoNuevo.getContribuyente().getId() == null) { throw new IllegalArgumentException("El contribuyente modificador no esta registrado"); }
        //if (hechoNuevo.getContribuyente().getEsAnonimo()) { throw new IllegalArgumentException("Un usuario anonimo no puede modificar hechos");}
        if (!hechoNuevo.getContribuyente().getId().equals(hechoGuardado.getContribuyente().getId()))
            { throw new IllegalArgumentException("El contribuyente modificador no es el creador del hecho");}

        if (hechoGuardado.getFechaDeCarga().plusDays(diasValidosModificacion).isBefore(fechaModificacion))
            {throw new IllegalArgumentException("Pasaron los 7 dias, no se puede modificar");}

        //TODO mejorar los Exception tirados haciendo una clase propia
        //TODO modificarHehco
        hechoNuevo.setEstado(EstadoHecho.PENDIENTE);
        hechoNuevo.setFechaDeModificacion(fechaModificacion);
        this.hechosRepository.save(hechoNuevo);
        return hechoNuevo;
    }

    @Override
    public Hecho revisarHecho(Long idHecho, Long idAdmin, EstadoHecho nuevoEstado) {
        Hecho hecho = this.hechosRepository.findById(idHecho);
        hecho.setIdAdmin(idAdmin);
        hecho.setEstado(nuevoEstado);
        hecho.setActualizar(true);
        //posible sugerencia agregada y/o razon de rechazo
        this.hechosRepository.save(hecho);
        return hecho;
    }

    public List<HechoOutputDTO> getHechosActualizar() {
        return this.hechosRepository.findAll().stream()
                .filter(h -> h.getActualizar() && h.getEstado().equals(EstadoHecho.ACEPTADO))
                .map(this::hechoOutputDTO)
                .collect(Collectors.toList());
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
                .nombreCategoria(hecho.getCategoria())
                .ubicacion(hecho.getUbicacion())
                .fechaOcurrencia(hecho.getFechaDeOcurrencia())
                .fechaCarga(hecho.getFechaDeCarga())
                .contenidoMultimedia(hecho.getContenidoMultimedia())
                .contribuyente(hecho.getContribuyente())
                .build();
    }
}
