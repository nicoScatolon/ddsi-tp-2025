package ar.edu.utn.frba.dds.domain.dtos;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputDinamicaDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputEstaticaDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputProxyDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.*;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;
import ar.edu.utn.frba.dds.domain.entities.Hecho.impl.HechoFuenteDinamica;
import ar.edu.utn.frba.dds.domain.entities.Hecho.impl.HechoFuenteEstatica;
import ar.edu.utn.frba.dds.domain.entities.Hecho.impl.HechoFuenteProxy;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.domain.entities.Usuario;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.ConstructorSolicitudesEliminacion;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//---CONVERTIDORES DE HECHOS Y DTOS---
public class DTOConverter {
    public static HechoOutputDTO convertirHechoOutputDTO(HechoBase hecho) {
        return HechoOutputDTO.builder()
                .id(hecho.getId())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(convertirCategoriaOutputDTO(hecho.getCategoria()))
                .ubicacion(convertirUbicacionOutputDTO(hecho.getUbicacion()))
                .fechaDeOcurrencia(hecho.getFechaDeOcurrencia())
                .build();
    }

    public static HechoBase convertirHechoInputDTO(HechoInputProxyDTO dto) {
        return HechoFuenteProxy.builder()
                .origenId(dto.getId())
                .idFuente(idFuente)
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .categoria(categoriaInputDTO(dto.getCategoria()))
                .build();
    }

    public static HechoBase convertirHechoInputDTO(HechoInputEstaticaDTO dto, Long idFuente) {
        return HechoFuenteEstatica.builder()
                .origenId(dto.getId())
                .idFuente(idFuente)
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .categoria(categoriaInputDTO(dto.getCategoria()))
                .build();
    }

    public static HechoBase convertirHechoInputDTO(HechoInputDinamicaDTO dto, Long idFuente) {
        return HechoFuenteDinamica.builder()
                .origenId(dto.getId())
                .idFuente(idFuente)
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(convertirCategoria(dto.getNombreCategoria()))
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .contenidoMultimedia(dto.getContenidoMultimedia())
                .contribuyente(convertirUsuario(dto.getContribuyente()))
                .categoria(categoriaInputDTO(dto.getCategoria()))
                .build();
    }

    public static HechoBase convertirHechoInputDTO(IHechoInputDTO dto, Long idFuente) {
        if (dto instanceof HechoInputProxyDTO proxy) {
            return convertirHechoInputDTO(proxy, idFuente);
        } else if (dto instanceof HechoInputEstaticaDTO estatica) {
            return convertirHechoInputDTO(estatica, idFuente);
        } else if (dto instanceof HechoInputDinamicaDTO dinamica) {
            return convertirHechoInputDTO(dinamica, idFuente);
        } else {
            throw new IllegalArgumentException("Tipo de DTO no soportado: " + dto.getClass());
        }
    }

    public static Categoria convertirCategoria(String nombreCategoria) {
        return Categoria.builder()
                .nombre(nombreCategoria)
                .build();
    }

    public static Ubicacion convertirUbicacion(UbicacionInputDTO dto) {
        return Ubicacion.builder()
                .latitud(dto.getLatitud())
                .longitud(dto.getLongitud())
                .build();
    }

    public static CategoriaOutputDTO convertirCategoriaOutputDTO(Categoria categoria) {
        return CategoriaOutputDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .build();
    }

    public static UbicacionOutputDTO convertirUbicacionOutputDTO(Ubicacion ubicacion) {
        return UbicacionOutputDTO.builder()
                .latitud(ubicacion.getLatitud())
                .longitud(ubicacion.getLongitud())
                .build();
    }

    public static Usuario convertirUsuario(UsuarioInputDTO dto) {
        return Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .fechaNacimiento(dto.getFechaNacimiento())
                .esAnonimo(dto.getEsAnonimo())
                .build();
    }

    public static SolicitudEliminarHechoOutputDTO solicitudEliminarHechoOutputDTO(SolicitudEliminarHecho solicitud) {
        return SolicitudEliminarHechoOutputDTO
                .builder()
                .hecho(DTOConverter.convertirHechoOutputDTO(solicitud.getHecho()))
                .razonDeEliminacion(solicitud.getRazonDeEliminacion())
                .nombreCreador(solicitud.getNombreCreador())
                .apellidoCreador(solicitud.getApellidoCreador())
                .fechaCreacion(solicitud.getFechaCreacion())
                .build();
    }

    public static SolicitudEliminarHecho solicitudEliminarHecho(SolicitudEliminarHechoInputDTO dto) {
        return ConstructorSolicitudesEliminacion
                .constructorSolicitud(
                        dto.getHecho(),
                        dto.getRazonDeEliminacion(),
                        dto.getNombreCreador(),
                        dto.getApellidoCreador());
    }

    public static List<HechoOutputDTO> hechoOutputDTO(Set<HechoBase> hechos) {
        return hechos.stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .collect(Collectors.toList());
    }

    public static List<HechoOutputDTO> hechoOutputDTO(List<HechoBase> hechos) {
        return hechos.stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .collect(Collectors.toList());
    }

    public static Categoria categoriaInputDTO(CategoriaInputDTO categoriaInputDTO) {
        return Categoria.builder()
                .nombre(categoriaInputDTO.getNombre())
                .id(categoriaInputDTO.getId())
                .build();
    }
}