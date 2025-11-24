package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.DdsHechoInputDTO;
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

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


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
                .localidad(dto.getLocalidad())
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

    public static FuenteOutputDTO mapToFuenteOutputDTO(Fuente fuente) {
        return FuenteOutputDTO.builder()
                .id(fuente.getId())
                .nombre(fuente.getNombre())
                .tipo(fuente.getTipo().name())
                .build();
    }



    public static Hecho mapToHecho(HechoInputDTO dto, Fuente fuente) {
        Hecho h = new Hecho();
        h.setIdOriginal(dto != null && dto.getId() != null ? String.valueOf(dto.getId()) : null);
        h.setNombre(dto != null ? dto.getTitulo() : null);
        h.setDescripcion(dto != null ? dto.getDescripcion() : null);
        h.setCategoria(dto != null ? dto.getCategoria() : null);

        if (dto != null && dto.getUbicacion() != null) {
            UbicacionInputDTO ubicacionDto = dto.getUbicacion();
            Ubicacion u = new Ubicacion();
            u.setProvincia(ubicacionDto.getProvincia());
            u.setLocalidad(ubicacionDto.getLocalidad());
            u.setCalle(ubicacionDto.getCalle());
            u.setNumero(ubicacionDto.getNumero());
            u.setLatitud(ubicacionDto.getLatitud());
            u.setLongitud(ubicacionDto.getLongitud());
            h.setUbicacion(u);
        } else {
            h.setUbicacion(null);
        }

        h.setFechaDeCarga(dto != null && dto.getFechaDeCarga() != null
                ? dto.getFechaDeCarga()
                : java.time.LocalDateTime.now());

        h.setFechaDeOcurrencia(dto != null && dto.getFechaDeOcurrencia() != null
                ? dto.getFechaDeOcurrencia()
                : null);


        h.setEliminado(false);
        h.setFuente(fuente);

        return h;
    }


    public static UbicacionOutputDTO mapUbicacionToUbicacionOutput(Ubicacion u) {
        if (u == null) return null;
        return UbicacionOutputDTO.builder()
                .provincia(u.getProvincia())
                .localidad(u.getLocalidad())
                .calle(u.getCalle())
                .numero(u.getNumero())
                .latitud(u.getLatitud())
                .longitud(u.getLongitud())
                .build();
    }



    public static HechoInputDTO mapDdsToHechoInput(DdsHechoInputDTO dds) {
        if (dds == null) return null;

        UbicacionInputDTO ubi = null;
        if (dds.getLatitud() != null || dds.getLongitud() != null) {
            ubi = UbicacionInputDTO.builder()
                    .latitud(dds.getLatitud())
                    .longitud(dds.getLongitud())
                    .build();
        }

        ZoneId BA = ZoneId.of("America/Argentina/Buenos_Aires");

        LocalDateTime fechaOcur = dds.getFechaHecho()
                .atZoneSameInstant(BA)
                .toLocalDateTime();

        LocalDateTime fechaCarga = (dds.getCreatedAt() != null
                ? dds.getCreatedAt()
                : OffsetDateTime.now(ZoneOffset.UTC))
                .atZoneSameInstant(BA)
                .toLocalDateTime();

        return HechoInputDTO.builder()
                .id(dds.getId())
                .titulo(dds.getTitulo())
                .descripcion(dds.getDescripcion())
                .categoria(dds.getCategoria())
                .ubicacion(ubi)
                .fechaDeOcurrencia(fechaOcur)
                .fechaDeCarga(fechaCarga)
                .build();
    }


}
