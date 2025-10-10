package ar.edu.utn.frba.dds.domain.dtos;

import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.*;
import ar.edu.utn.frba.dds.domain.dtos.output.*;
import ar.edu.utn.frba.dds.domain.entities.*;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.IAlgoritmoConsenso;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.ContenidoMultimedia.ContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Normalizadores.NormalizadorTexto;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.ConstructorSolicitudesEliminacion;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.utn.frba.dds.domain.entities.Normalizadores.NormalizadorUbicacion.normalizarUbicacion;
import static ar.edu.utn.frba.dds.domain.entities.Normalizadores.NormalizadorUbicacion.normalizarProvincia;

//---CONVERTIDORES DE HECHOS Y DTOS---
public class DTOConverter {

    // HECHOS OUTPUT

    public static HechoOutputDTO convertirHechoOutputDTO(Hecho hecho) {

        return HechoOutputDTO.builder()
                .id(hecho.getId())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(convertirCategoriaOutputDTO(hecho.getCategoria()))
                .ubicacion(convertirUbicacionOutputDTO(hecho.getUbicacion()))
                .contenidoMultimedia(
                        hecho.getContenidoMultimedia() != null ?
                                hecho.getContenidoMultimedia()
                                        .stream()
                                        .map(DTOConverter::convertirContenidoMultimediaOutputDTO)
                                        .toList()
                                : Collections.emptyList()
                )
                .etiquetas(hecho.getEtiquetas())
                .fechaDeOcurrencia(hecho.getFechaDeOcurrencia())
                .fechaDeCarga(hecho.getFechaDeCarga())
                .cargadoAninimamente(hecho.getCargadoAnonimamente())
                .fuente(convertirFuentePreviewOutputDTO(hecho.getFuente()))
                .build();
    }

    public static List<HechoOutputDTO> hechoOutputDTO(List<Hecho> hechos) {
        return hechos.stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .collect(Collectors.toList());
    }

    public static HechoMapaOutputDTO convertirHechoMapaOutputDTO(Hecho hecho) {
        return HechoMapaOutputDTO.builder()
                .id(hecho.getId())
                .titulo(hecho.getTitulo())
                .categoria(hecho.getCategoria().getNombre())
                .latitud(hecho.getUbicacion().getLatitud())
                .longitud(hecho.getUbicacion().getLongitud())
                .build();
    }

    // HECHOS INPUT

    public static Hecho convertirHechoInputDTO(HechoInputProxyDTO dto) {
        return Hecho.builder()
                .origenId(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .categoria(categoriaInputDTO(dto.getCategoria()) )
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
        Hecho hecho = Hecho.builder()
                .origenId(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .contribuyenteId(dto.getContribuyenteId())
                .categoria(categoriaInputDTO(dto.getCategoria()))
                .fueEliminado(false)
                .build();

        hecho.setContenidoMultimedia(DTOConverter.convertirContenidoMultimedia(dto.getContenidoMultimedia(), hecho));
        return hecho;
    }

    // FUENTE OUTPUT

    public static FuentePreviewOutputDTO convertirFuentePreviewOutputDTO(Fuente fuente) {
        return FuentePreviewOutputDTO.builder()
                .fuenteId(fuente.getId())
                .nombre(fuente.getNombre())
                .build();
    }

    public static List<FuentePreviewOutputDTO> convertirListaFuentePreviewOutputDTO(List<Fuente> fuente) {
        return fuente.stream().map(DTOConverter::convertirFuentePreviewOutputDTO).toList();
    }

    // FUENTE INPUT

    public static Fuente fuenteOutputDTOToFuente(FuenteInputDTO fuenteDTO) {
        Fuente fuente = fuenteDTO.getTipoFuente().crearFuente(fuenteDTO.getUrl());
        fuente.setNombre(fuenteDTO.getNombre());
        return fuente;
    }

    // CONTENIDO MULTIMEDIA INPUT

    public static List<ContenidoMultimedia> convertirContenidoMultimedia(
            List<ContenidoMultimediaInputDTO> contenidoMultimediaInputDTO, Hecho hecho) {

        if (contenidoMultimediaInputDTO == null) {
            return List.of();
        }

        return contenidoMultimediaInputDTO.stream()
                .map(cmDTO -> DTOConverter.convertirContenidoMultimedia(cmDTO, hecho)).toList();
    }

    public static ContenidoMultimedia convertirContenidoMultimedia(ContenidoMultimediaInputDTO contenidoMultimediaInputDTO, Hecho hecho) {
        return ContenidoMultimedia.builder()
                .descripcion(contenidoMultimediaInputDTO.getDescripcion())
                .url(contenidoMultimediaInputDTO.getUrl())
                .hecho(hecho)
                .tipoContenido(contenidoMultimediaInputDTO.getTipo())
                .build();
    }

    // CONTENIDO MULTIMEDIA OUTPUT

    public static ContenidoMultimediaOutputDTO convertirContenidoMultimediaOutputDTO(ContenidoMultimedia contenidoMultimedia) {
        return ContenidoMultimediaOutputDTO.builder()
                .id(contenidoMultimedia.getId())
                .tipo(contenidoMultimedia.getTipoContenido())
                .descripcion(contenidoMultimedia.getDescripcion())
                .url(contenidoMultimedia.getUrl())
                .build();
    }

    // UBICACION INPUT

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

    // UBICACION OUTPUT

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

    // CATEGORIA OUTPUT

    public static CategoriaOutputDTO convertirCategoriaOutputDTO(Categoria categoria) {
        return CategoriaOutputDTO.builder()
                .id(categoria.getCodigoCategoria())
                .nombre(categoria.getNombre())
                .build();
    }

    // CATEGORIA INPUT

    public static Categoria categoriaInputDTO(CategoriaInputDTO categoriaInputDTO) {
        return Categoria.builder()
                .nombre(categoriaInputDTO.getNombre())
                .codigoCategoria(categoriaInputDTO.getCodigoCat())
                .build();
    }

    public static Categoria categoriaInputDTO(String categoriaInputDTO) {
        return Categoria.builder()
                .nombre(categoriaInputDTO)
                .codigoCategoria(NormalizadorTexto.normalizarTexto(categoriaInputDTO))
                .build();
    }

    // SOLICITUD ELIMINACION OUTPUT

    public static SolicitudEliminarHechoOutputDTO solicitudEliminarHechoOutputDTO(SolicitudEliminarHecho solicitud, Contribuyente contribuyente) {
        return SolicitudEliminarHechoOutputDTO
                .builder()
                .hecho(DTOConverter.convertirHechoOutputDTO(solicitud.getHecho()))
                .razonDeEliminacion(solicitud.getRazonDeEliminacion())
                .idCreador(solicitud.getIdCreador())
                .fechaCreacion(solicitud.getFechaCreacion())
                .build();
    }

    // SOLICITUD ELIMINACION INPUT

    public static SolicitudEliminarHecho solicitudEliminarHecho(SolicitudEliminarHechoInputDTO dto, Hecho hecho) {
        return ConstructorSolicitudesEliminacion
                .constructorSolicitud(
                        hecho,
                        dto.getRazonDeEliminacion(),
                        dto.getIdCreador());
    }

    // ALGORITMO CONCENSO DTO

    public static IAlgoritmoConsenso algoritmoConsensoFromDTO(AlgoritmoConsensoDTO dto) {
        if (dto == null) { return null;}
        return dto.getTipo().obtenerConsenso();
    }

    public static AlgoritmoConsensoDTO algoritmoConsensoFromDTO(IAlgoritmoConsenso algoritmoConsenso) {
        if (algoritmoConsenso == null) { return null;}
        return new AlgoritmoConsensoDTO(algoritmoConsenso.getTipo());
    }

    // COLECCION OUTPUT

    public static ColeccionOutputDTO coleccionOutputDTO(Coleccion coleccion) {
        return ColeccionOutputDTO.builder()
                .titulo(coleccion.getTitulo())
                .descripcion(coleccion.getDescripcion())
                .handle(coleccion.getHandle())
                .algoritmoConsenso(DTOConverter.algoritmoConsensoFromDTO(coleccion.getAlgoritmoConsenso()))
                .hechos( DTOConverter.hechoOutputDTO(coleccion.getListaHechos()) )
                .build();
    }

    public static ColeccionPreviewOutputDTO coleccionPreviewOutputDTO(Coleccion coleccion) {
        return ColeccionPreviewOutputDTO.builder()
                .titulo(coleccion.getTitulo())
                .descripcion(coleccion.getDescripcion())
                .handle(coleccion.getHandle())
                .algoritmoCurado(DTOConverter.algoritmoConsensoFromDTO(coleccion.getAlgoritmoConsenso()))
                .fuentes(DTOConverter.convertirListaFuentePreviewOutputDTO(coleccion.getListaFuentes()))
                .build();
    }

    // COLECCION INPUT

    public static Coleccion coleccionFromInputDTO(ColeccionInputDTO input) {
        return new Coleccion(
                input.getHandle(),
                input.getTitulo(),
                input.getDescripcion(),
                DTOConverter.algoritmoConsensoFromDTO(input.getAlgoritmoConsenso()));
    }

    // HECHOS FILTER DTO

    public static HechoFilter convertirHechoFilterInputDTO(HechosFilterDTO filterDTO) {
        return HechoFilter.builder()
                .categoria(filterDTO.getCategoria())
                .fReporteDesde(filterDTO.getFReporteDesde())
                .fReporteHasta(filterDTO.getFReporteHasta())
                .fAconDesde(filterDTO.getFAconDesde())
                .fAconHasta(filterDTO.getFAconHasta())
                .provincia(normalizarProvincia(filterDTO.getProvincia()))
                .fuenteId(filterDTO.getFuenteId())
                .etiqueta(filterDTO.getEtiqueta())
                .build();
    }



}