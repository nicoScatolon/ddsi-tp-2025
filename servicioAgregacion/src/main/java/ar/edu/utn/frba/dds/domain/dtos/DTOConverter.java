package ar.edu.utn.frba.dds.domain.dtos;

import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UsuarioInputDTO;
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
import ar.edu.utn.frba.dds.domain.entities.Usuario;

//---CONVERTIDORES DE HECHOS Y DTOS---
public class DTOConverter {
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
                .categoria(convertirCategoria(dto.getNombreCategoria()))
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .build();
    }

    public static IHecho convertirHechoInputDTO(HechoInputEstaticaDTO dto) {
        return HechoFuenteEstatica.builder()
                .fuenteId(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(convertirCategoria(dto.getNombreCategoria()))
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .build();
    }

    public static IHecho convertirHechoInputDTO(HechoInputDinamicaDTO dto) {
        return HechoFuenteDinamica.builder()
                .fuenteId(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(convertirCategoria(dto.getNombreCategoria()))
                .ubicacion(convertirUbicacion(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .contenidoMultimedia(dto.getContenidoMultimedia())
                .contribuyente(convertirUsuario(dto.getContribuyente()))
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
                .build();
    }
}