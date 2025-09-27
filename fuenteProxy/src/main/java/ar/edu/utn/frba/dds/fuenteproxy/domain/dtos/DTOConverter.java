package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.FuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteproxy.services.impl.FuenteFactory;


public final class DTOConverter {

    private DTOConverter() {}

    // Mapper DTO externo → DTO de salida del sistema
    public static HechoOutputDTO mapHechoInputToHechoOutput(HechoInputDTO dto, ICategoriaService categoriaService) {

        return HechoOutputDTO.builder()
                .id(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(categoriaToDTO(dto.getCategoria(), categoriaService))
                .ubicacion(mapUbicacionInputToUbicacionOutput(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia())
                .fechaDeCarga(dto.getFechaDeCarga())
                .build();
    }


    public static HechoOutputDTO mapHechoToHechoOutput(Hecho h, ICategoriaService categoriaService) {
        return HechoOutputDTO.builder()
                .id(h.getId())
                .titulo(h.getNombre())
                .descripcion(h.getDescripcion())
                .categoria(categoriaToDTO(h.getCategoria(), categoriaService))
                .ubicacion(mapUbicacionToUbicacionOutput(h.getUbicacion()))
                .fechaDeOcurrencia(h.getFechaDeOcurrencia())
                .fechaDeCarga(h.getFechaDeCarga())
                .build();
    }


    public static UbicacionOutputDTO mapUbicacionInputToUbicacionOutput(UbicacionInputDTO dto){
        if(dto == null) return null;
        return UbicacionOutputDTO.builder()
                .provincia(dto.getProvincia())
                .departamento(dto.getDepartamento())
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .latitud(dto.getLatitud())
                .longitud(dto.getLongitud())
                .build();
    }



    public static CategoriaOutputDTO categoriaToDTO(String nombreCategoria, ICategoriaService categoriaService) {
        return CategoriaOutputDTO.builder()
                .codigoCategoria(categoriaService.obtenerIdCategoria(nombreCategoria))
                .nombre(nombreCategoria)
                .build();
    }

    public static FuenteMetaMapa mapToFuenteMetaMapa(FuenteInputDTO dto, FuenteFactory factory) {
        return factory.nuevaFuenteMetaMapa(dto.getNombre(), dto.getBaseUrl());
    }

    public static FuenteDDS mapToFuenteDDS(FuenteInputDTO dto, FuenteFactory factory) {
        return factory.nuevaFuenteDDS(dto.getNombre());
    }

    public static FuenteOutputDTO mapToFuenteOutputDTO(FuenteMetaMapa fuente) {
        return FuenteOutputDTO.builder()
                .id(fuente.getId())
                .nombre(fuente.getNombre())
                .tipo(fuente.getTipo().name())
                .build();
    }

    public static FuenteOutputDTO mapToFuenteOutputDTO(FuenteDDS fuente) {
        return FuenteOutputDTO.builder()
                .id(fuente.getId())
                .nombre(fuente.getNombre())
                .tipo(fuente.getTipo().name())
                .build();
    }



    public static Hecho mapToHecho(HechoInputDTO dto, Fuente fuente) {
        Hecho h = new Hecho();
        h.setId_original(String.valueOf(dto.getId()));
        h.setNombre(dto.getTitulo());
        h.setDescripcion(dto.getDescripcion());
        h.setCategoria(dto.getCategoria());

        Ubicacion u = new Ubicacion();
        u.setProvincia(dto.getUbicacion().getProvincia());
        u.setDepartamento(dto.getUbicacion().getDepartamento());
        u.setCalle(dto.getUbicacion().getCalle());
        u.setNumero(dto.getUbicacion().getNumero());
        u.setLatitud(dto.getUbicacion().getLatitud());
        u.setLongitud(dto.getUbicacion().getLongitud());
        h.setUbicacion(u);

        h.setFechaDeOcurrencia(dto.getFechaDeOcurrencia());
        h.setFechaDeCarga(dto.getFechaDeCarga());
        h.setEliminado(false);
        h.setFuente(fuente);
        return h;
    }


    public static UbicacionOutputDTO mapUbicacionToUbicacionOutput(Ubicacion u) {
        return UbicacionOutputDTO.builder()
                .provincia(u.getProvincia())
                .departamento(u.getDepartamento())
                .calle(u.getCalle())
                .numero(u.getNumero())
                .latitud(u.getLatitud())
                .longitud(u.getLongitud())
                .build();
    }


}
