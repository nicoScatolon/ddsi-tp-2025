package ar.edu.utn.frba.dds.clienteGrafico.dtos;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.TipoAlgoritmoConsenso;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.ContenidoMultimediaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.AlgoritmoConcensoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.CriterioOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares.ColeccionFormDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares.CriterioFormDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.ContenidoMultimediaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.ProcesarSolicitudOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.SolicitudEliminarHechoOutputDTO;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DTOConverter {
    public static HechoOutputDTO convertirHechoInputDTO(HechoInputDTO hechoDTO) {
        return HechoOutputDTO.builder()
                .id(hechoDTO.getId())
                .titulo(hechoDTO.getTitulo())
                .descripcion(hechoDTO.getDescripcion())
                .categoria(convertirCategoriaInputDTO(hechoDTO.getCategoria()))
                .ubicacion(convertirUbicacionInputDTO(hechoDTO.getUbicacion()))
                .etiquetas(
                        hechoDTO.getEtiquetas() != null
                                ? hechoDTO.getEtiquetas().stream()
                                .map(DTOConverter::convertirEtiquetaInputDTO)
                                .toList()
                                : List.of()
                )
                .contenidoMultimedia(
                        hechoDTO.getContenidoMultimedia() != null
                                ? hechoDTO.getContenidoMultimedia().stream()
                                .map(DTOConverter::convertirContenidoMultimediaInputDTO)
                                .toList()
                                : List.of()
                )
                .fechaDeOcurrencia(hechoDTO.getFechaDeOcurrencia())
                .fechaDeCarga(hechoDTO.getFechaDeCarga())
                .contribuyente(
                        hechoDTO.getContribuyente() != null
                                ? convertirContribuyenteInputDTO(hechoDTO.getContribuyente())
                                : null
                )
                .cargadoAnonimamente(hechoDTO.getCargadoAnonimamente())
                .build();
    }

    public static CategoriaOutputDTO convertirCategoriaInputDTO(CategoriaInputDTO categoriaInputDTO) {
        return CategoriaOutputDTO.builder()
                .id(categoriaInputDTO.getId())
                .nombre(categoriaInputDTO.getNombre())
                .build();
    }

    public static UbicacionOutputDTO convertirUbicacionInputDTO(UbicacionInputDTO ubicacionInputDTO) {
        return UbicacionOutputDTO.builder()
                .provincia(ubicacionInputDTO.getProvincia())
                .localidad(ubicacionInputDTO.getLocalidad())
                .calle(ubicacionInputDTO.getCalle())
                .numero(ubicacionInputDTO.getNumero())
                .longitud(ubicacionInputDTO.getLongitud())
                .latitud(ubicacionInputDTO.getLatitud())
                .id(ubicacionInputDTO.getId())
                .build();
    }

    public static EtiquetaOutputDTO convertirEtiquetaInputDTO(EtiquetaInputDTO etiquetaInputDTO) {
        return EtiquetaOutputDTO.builder()
                .id(etiquetaInputDTO.getId())
                .build();
    }

    public static ContenidoMultimediaOutputDTO convertirContenidoMultimediaInputDTO(ContenidoMultimediaInputDTO contenidoMultimediaInputDTO) {
        return ContenidoMultimediaOutputDTO.builder()
                .Id(contenidoMultimediaInputDTO.getId())
                .url(contenidoMultimediaInputDTO.getUrl())
                .descripcion(contenidoMultimediaInputDTO.getDescripcion())
                .build();
    }

    public static ContribuyenteOutputDTO convertirContribuyenteInputDTO(ContribuyenteInputDTO contribuyenteInputDTO) {
        return ContribuyenteOutputDTO.builder()
                .fechaNacimiento(contribuyenteInputDTO.getFechaNacimiento())
                .apellido(contribuyenteInputDTO.getApellido())
                .nombre(contribuyenteInputDTO.getNombre())
                .id(contribuyenteInputDTO.getId())
                .build();
    }

    public static HechosFilterOutputDTO convertirHechosFilterInputDTO(HechosFilterInputDTO filterInputDTO) {
        return HechosFilterOutputDTO.builder()
                .categoria(filterInputDTO.getCategoria())
                .provincia(filterInputDTO.getProvincia())
                .etiqueta(filterInputDTO.getEtiqueta())
                .fuenteId(filterInputDTO.getFuenteId())
                .fAconDesde( filterInputDTO.getFAconDesde() != null ? filterInputDTO.getFAconDesde().atStartOfDay() : null )
                .fAconHasta( filterInputDTO.getFAconHasta() != null ? filterInputDTO.getFAconHasta().atStartOfDay() : null )
                .fReporteDesde( filterInputDTO.getFReporteDesde() != null ? filterInputDTO.getFReporteDesde().atStartOfDay() : null )
                .fReporteHasta( filterInputDTO.getFReporteHasta() != null ? filterInputDTO.getFReporteHasta().atStartOfDay() : null )
                .build();
    }

    public static SolicitudEliminarHechoOutputDTO convertirSolicitudEliminacion(Long hechoId, Long usuarioId, String razonEliminacion){
        return SolicitudEliminarHechoOutputDTO.builder()
                .fechaCreacion(LocalDateTime.now())
                .razonDeEliminacion(razonEliminacion)
                .idCreador(usuarioId)
                .hechoId(hechoId)
                .build();
    }

    public static SolicitudEliminarHechoOutputDTO convertirSolicitudEliminacion(SolicitudEliminarHechoInputDTO solInputDTO){
        return SolicitudEliminarHechoOutputDTO.builder()
                .fechaCreacion(solInputDTO.getFechaCreacion())
                .razonDeEliminacion(solInputDTO.getRazonDeEliminacion())
                .idCreador(solInputDTO.getIdCreador())
                .hechoId(solInputDTO.getHecho().getId())
                .build();
    }

    public static ProcesarSolicitudOutputDTO convertirProcesarSolicitudOutputDTO(SolicitudEliminarHechoOutputDTO solInputDTO, Long idAdmin) {
        return ProcesarSolicitudOutputDTO.builder()
                .administradorId(idAdmin)
                .solicitud(solInputDTO)
                .build();
        }

    public static ColeccionOutputDTO convertirFormToOutput(ColeccionFormDTO formDTO) {
        Set<CriterioOutputDTO> criterios = new HashSet<>();

        if (formDTO.getListaCriterios() != null) {
            for (CriterioFormDTO criterioForm : formDTO.getListaCriterios()) {
                CriterioOutputDTO criterio = CriterioOutputDTO.builder()
                        .tipo(criterioForm.getTipo())
                        .parametros(criterioForm.getParametros())
                        .build();
                criterios.add(criterio);
            }
        }

        AlgoritmoConcensoOutputDTO algoritmo = null;
        if (formDTO.getAlgoritmoConsensoTipo() != null && !formDTO.getAlgoritmoConsensoTipo().isEmpty()) {
            algoritmo = new AlgoritmoConcensoOutputDTO();
            try {
                TipoAlgoritmoConsenso tipoEnum = TipoAlgoritmoConsenso.valueOf(formDTO.getAlgoritmoConsensoTipo());
                algoritmo.setTipo(tipoEnum);
            } catch (IllegalArgumentException e) {
                // Manejo si el string no coincide con ningún enum
                algoritmo.setTipo(null); // o un valor default, ej: TipoAlgoritmoConsenso.MAYORIASIMPLE
            }
        }

        return ColeccionOutputDTO.builder()
                .listaCriterios(criterios)
                .listaIdsFuentes(formDTO.getListaIdsFuentes() != null ?
                        new HashSet<>(formDTO.getListaIdsFuentes()) : new HashSet<>())
                .handle(formDTO.getHandle())
                .titulo(formDTO.getTitulo())
                .descripcion(formDTO.getDescripcion())
                .algoritmoConsenso(algoritmo)
                .build();
    }
}
