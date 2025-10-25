package ar.edu.utn.frba.dds.clienteGrafico.dtos;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.CriterioInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.TipoAlgoritmoConsenso;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.ContenidoMultimediaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.AlgoritmoConsensoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.CriterioOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares.ColeccionFormDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares.CriterioFormDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.ProcesarSolicitudOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.SolicitudEliminarHechoOutputDTO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class DTOConverter {
    public static HechoDinamicaOutputDTO convertirHechoInputDTO(HechoInputDTO hechoDTO) {
        return HechoDinamicaOutputDTO.builder()
                .id(hechoDTO.getId())
                .titulo(hechoDTO.getTitulo())
                .descripcion(hechoDTO.getDescripcion())
                .categoria(convertirCategoriaInputDTO(hechoDTO.getCategoria()))
                .ubicacion(convertirUbicacionInputDTO(hechoDTO.getUbicacion()))
                .contribuyenteId(hechoDTO.getContribuyenteId())
                .contenidoMultimedia(
                        hechoDTO.getContenidoMultimedia() != null
                                ? hechoDTO.getContenidoMultimedia().stream()
                                .map(DTOConverter::convertirContenidoMultimediaInputDTO)
                                .toList()
                                : List.of()
                )
                .fechaDeOcurrencia(hechoDTO.getFechaDeOcurrencia())
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
                .departamento(ubicacionInputDTO.getDepartamento())
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
                .id(contenidoMultimediaInputDTO.getId())
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

        AlgoritmoConsensoOutputDTO algoritmo = null;
        if (formDTO.getAlgoritmoConsensoTipo() != null && !formDTO.getAlgoritmoConsensoTipo().isEmpty()) {
            algoritmo = new AlgoritmoConsensoOutputDTO();
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

    public static ColeccionFormDTO convertirAFormDTO(ColeccionInputDTO coleccionDTO) {
        ColeccionFormDTO form = new ColeccionFormDTO();
        form.setHandle(coleccionDTO.getHandle());
        form.setTitulo(coleccionDTO.getTitulo());
        form.setDescripcion(coleccionDTO.getDescripcion());
        form.setAlgoritmoConsensoTipo(
                coleccionDTO.getAlgoritmoConsenso() != null ?
                        coleccionDTO.getAlgoritmoConsenso().name() : null
        );

        // Convertir fuentes
        form.setListaIdsFuentes(
                coleccionDTO.getFuentes().stream()
                        .map(FuenteInputDTO::getFuenteId)
                        .collect(Collectors.toList())
        );

        // Convertir criterios
        form.setListaCriterios(
                coleccionDTO.getListaCriterios().stream()
                        .map(DTOConverter::convertirCriterio)
                        .collect(Collectors.toList())
        );

        return form;
    }

    public static CriterioFormDTO convertirCriterio(CriterioInputDTO input) {
        CriterioFormDTO form = new CriterioFormDTO();
        form.setTipo(input.getTipo());
        form.setParametros(new HashMap<>(input.getParametros()));
        return form;
    }
}
