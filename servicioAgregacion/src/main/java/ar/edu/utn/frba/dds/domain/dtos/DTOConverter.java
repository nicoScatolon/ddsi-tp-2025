package ar.edu.utn.frba.dds.domain.dtos;

import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.*;
import ar.edu.utn.frba.dds.domain.dtos.output.*;
import ar.edu.utn.frba.dds.domain.entities.*;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.IAlgoritmoConsenso;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.ConstructorSolicitudesEliminacion;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ar.edu.utn.frba.dds.domain.entities.normalizadores.NormalizadorUbicacion.normalizarUbicacion;

//---CONVERTIDORES DE HECHOS Y DTOS---
public class DTOConverter {
    public static HechoOutputDTO convertirHechoOutputDTO(Hecho hecho) {
        return HechoOutputDTO.builder()
                .id(hecho.getId())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(convertirCategoriaOutputDTO(hecho.getCategoria()))
                .ubicacion(convertirUbicacionOutputDTO(hecho.getUbicacion()))
                .fechaDeOcurrencia(hecho.getFechaDeOcurrencia())
                .build();
    }

    public static Hecho convertirHechoInputDTO(HechoInputProxyDTO dto) {
        return Hecho.builder()
                .origenId(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .categoria(categoriaInputDTO(dto.getCategoria()))
                .fueEliminado(false)
                .build();
    }

    public static Hecho convertirHechoInputDTO(HechoInputEstaticaDTO dto) {
        return Hecho.builder()
                .origenId(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .categoria(categoriaInputDTO(dto.getCategoria()))
                .fueEliminado(false)
                .build();
    }

    public static Hecho convertirHechoInputDTO(HechoInputDinamicaDTO dto) {
        return Hecho.builder()
                .origenId(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .contenidoMultimedia(dto.getContenidoMultimedia())
                .contribuyente(convertirUsuario(dto.getContribuyente()))
                .categoria(categoriaInputDTO(dto.getCategoria()))
                .fueEliminado(false)
                .build();
    }

    public static Hecho convertirHechoInputDTO(IHechoInputDTO dto) {
        if (dto instanceof HechoInputProxyDTO proxy) {
            return convertirHechoInputDTO(proxy);
        } else if (dto instanceof HechoInputEstaticaDTO estatica) {
            return convertirHechoInputDTO(estatica);
        } else if (dto instanceof HechoInputDinamicaDTO dinamica) {
            return convertirHechoInputDTO(dinamica);
        } else {
            throw new IllegalArgumentException("Tipo de DTO no soportado: " + dto.getClass());
        }
    }


    public static Ubicacion convertirUbicacion(UbicacionInputDTO dto) {
        normalizarUbicacion(dto);
        return Ubicacion.builder()
                .provincia(dto.getProvincia())
                .departamento(dto.getDepartamento())
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .latitud(dto.getLatitud())
                .longitud(dto.getLongitud())
                .build();
    }

    public static CategoriaOutputDTO convertirCategoriaOutputDTO(Categoria categoria) {
        return CategoriaOutputDTO.builder()
                .id(categoria.getCodigoCategoria())
                .nombre(categoria.getNombre())
                .build();
    }

    public static UbicacionOutputDTO convertirUbicacionOutputDTO(Ubicacion ubicacion) {
        return UbicacionOutputDTO.builder()
                .id(ubicacion.getId())
                .provincia(ubicacion.getProvincia())
                .calle(ubicacion.getCalle())
                .numero(ubicacion.getNumero())
                .latitud(ubicacion.getLatitud())
                .longitud(ubicacion.getLongitud())
                .build();
    }

    public static Contribuyente convertirUsuario(UsuarioInputDTO dto) {
        return Contribuyente.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .fechaNacimiento(dto.getFechaNacimiento())
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

    public static SolicitudEliminarHecho solicitudEliminarHecho(SolicitudEliminarHechoInputDTO dto, Hecho hecho) {
        return ConstructorSolicitudesEliminacion
                .constructorSolicitud(
                        hecho,
                        dto.getRazonDeEliminacion(),
                        dto.getNombreCreador(),
                        dto.getApellidoCreador());
    }

    public static List<HechoOutputDTO> hechoOutputDTO(Set<Hecho> hechos) {
        return hechos.stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .collect(Collectors.toList());
    }

    public static List<HechoOutputDTO> hechoOutputDTO(List<Hecho> hechos) {
        return hechos.stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .collect(Collectors.toList());
    }

    public static Categoria categoriaInputDTO(CategoriaInputDTO categoriaInputDTO) {
        return Categoria.builder()
                .nombre(categoriaInputDTO.getNombre())
                .codigoCategoria(categoriaInputDTO.getCodigoCat())
                .build();
    }

    public static AlgoritmoConsenso algoritmoConsensoFromDTO(AlgoritmoConsensoDTO dto) {
        if (dto == null) { return null;}
        return dto.getTipo().obtenerConsenso();
    }

    public static AlgoritmoConsensoDTO algoritmoConsensoFromDTO(IAlgoritmoConsenso algoritmoConsenso) {
        if (algoritmoConsenso == null) { return null;}
        return new AlgoritmoConsensoDTO(algoritmoConsenso.getTipo());
    }


    public static ColeccionOutputDTO coleccionOutputDTO(Coleccion coleccion) {
        return ColeccionOutputDTO.builder()
                .titulo(coleccion.getTitulo())
                .descripcion(coleccion.getDescripcion())
                .handle(coleccion.getHandle())
                .algoritmoConsenso(DTOConverter.algoritmoConsensoFromDTO(coleccion.getAlgoritmoConsenso()))
                .build();
    }

    public static Coleccion coleccionFromInputDTO(ColeccionInputDTO input) {
        return new Coleccion(
                input.getHandle(),
                input.getTitulo(),
                input.getDescripcion(),
                DTOConverter.algoritmoConsensoFromDTO(input.getAlgoritmoConsenso()));
    }

    public static ColeccionInputDTO toInputDTO(Coleccion coleccion) {
        return ColeccionInputDTO.builder()
                .titulo(coleccion.getTitulo())
                .descripcion(coleccion.getDescripcion())
                .handle(coleccion.getHandle())
                .build();
    }

    public static Fuente fuenteDTOToFuente(FuenteInputDTO fuenteDTO) {
        Fuente fuente = fuenteDTO.getTipoFuente().crearFuente(fuenteDTO.getUrl());
        fuente.setNombre(fuenteDTO.getNombre());
        return fuente;
    }

    public static HechoFilter convertirHechoFilterInputDTO(HechosFilterDTO filterDTO) {
        return HechoFilter.builder()
                .categoria(filterDTO.getCategoria())
                .fReporteDesde(filterDTO.getFReporteDesde())
                .fReporteHasta(filterDTO.getFReporteHasta())
                .fAconDesde(filterDTO.getFAconDesde())
                .fAconHasta(filterDTO.getFAconHasta())
                .build();
    }


}