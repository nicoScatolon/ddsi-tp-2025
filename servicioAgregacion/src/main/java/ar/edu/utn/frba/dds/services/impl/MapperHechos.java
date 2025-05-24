package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputDinamicaDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputEstaticaDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputProxyDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import ar.edu.utn.frba.dds.domain.entities.Hecho.impl.HechoFuenteDinamica;
import ar.edu.utn.frba.dds.domain.entities.Hecho.impl.HechoFuenteEstatica;
import ar.edu.utn.frba.dds.domain.entities.Hecho.impl.HechoFuenteProxy;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;

//---CONVERTIDORES DE HECHOS Y DTOS---
public class MapperHechos {
    public static HechoOutputDTO convertirHechoOutputDTO(IHecho hecho) {
        return HechoOutputDTO.builder()
                .id(hecho.getId())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(convertirCategoriaOutputDTO(hecho.getCategoria()))
                .ubicacion(convertirUbicacionOutputDTO(hecho.getUbicacion()))
                .fechaDeOcurrencia(hecho.getFechaDeOcurrencia())
                .build();
    }

    public static IHecho convertirHechoInputDTO(HechoInputProxyDTO dto) {
        return HechoFuenteProxy.builder()
                .fuenteId(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(convertirCategoria(dto.getCategoria()))
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .build();
    }

    public static IHecho convertirHechoInputDTO(HechoInputEstaticaDTO dto) {
        return HechoFuenteEstatica.builder()
                .fuenteId(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(convertirCategoria(dto.getCategoria()))
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .build();
    }

    public static IHecho convertirHechoInputDTO(HechoInputDinamicaDTO dto) {
        return HechoFuenteDinamica.builder()
                .fuenteId(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(convertirCategoria(dto.getCategoria()))
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .build();
    }


    public static Categoria convertirCategoria(CategoriaInputDTO dto) {
        return Categoria.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
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

    public static IHecho convertirHechoInputDTO(IHechoInputDTO dto) {
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
}