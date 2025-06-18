package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.ContribuyenteInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.ContenidoMultimedia;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repository.impl.HechosRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IHechosService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

class HechosServiceTest {

    private HechoInputDTO crearHechoInputDTO(String titulo, String descripcion, String categoria, Double latitud, Double longitud, LocalDate fechaDeOcurrencia, String url) {
        var hechoInputDTO = new HechoInputDTO();
        hechoInputDTO.setTitulo(titulo);
        hechoInputDTO.setDescripcion(descripcion);
        hechoInputDTO.setCategoria(categoria);
        var ubicacion = new Ubicacion();
        ubicacion.setLatitud(latitud);
        ubicacion.setLongitud(longitud);
        hechoInputDTO.setUbicacion(ubicacion);
        hechoInputDTO.setFechaDeOcurrencia(fechaDeOcurrencia);
        var contenidoMultimedia = new ContenidoMultimedia();
        contenidoMultimedia.setDescripcion("");
        contenidoMultimedia.setUrl(url);
        hechoInputDTO.setContenidoMultimedia(contenidoMultimedia);
        return hechoInputDTO;
    }
    private ContribuyenteInputDTO crearContribuyenteDTO (Long id, String nombre, String apellido, Boolean anonimo, LocalDate fechaNacimiento) {
        var contribuyenteInputDTO = new ContribuyenteInputDTO();
        contribuyenteInputDTO.setId(id);
        contribuyenteInputDTO.setNombre(nombre);
        contribuyenteInputDTO.setApellido(apellido);
        contribuyenteInputDTO.setEsAnonimo(anonimo);
        contribuyenteInputDTO.setFechaNacimiento(fechaNacimiento);
        return contribuyenteInputDTO;
    }

    private Boolean compararHechos(HechoInputDTO hechoDTO, Hecho hecho){
        Boolean compararHechos = true;
        compararHechos = Objects.equals(hechoDTO.getTitulo(), hecho.getTitulo())
                && Objects.equals(hechoDTO.getDescripcion(), hecho.getDescripcion())
                && Objects.equals(hechoDTO.getCategoria(), hecho.getCategoria())
                && Objects.equals(hechoDTO.getUbicacion(), hecho.getUbicacion())
                && Objects.equals(hechoDTO.getFechaDeOcurrencia(), hecho.getFechaDeOcurrencia())
                && Objects.equals(hechoDTO.getContenidoMultimedia(), hecho.getContenidoMultimedia());
        return compararHechos;
    }

    @Test
    public void testCrearHecho(){
        var hechoRepository = new HechosRepository();
        var hechoService = new HechosService(hechoRepository);

        var hechoDTO1 = this.crearHechoInputDTO("1", "desc" , "incendio", 12.33, 13.22, LocalDate.now(), "youtube/lol");
        var contribuyente1DTO = this.crearContribuyenteDTO(1L, "jorge", "lopez", false, LocalDate.now());
        hechoService.cargarHecho(hechoDTO1, contribuyente1DTO);
        Assertions.assertTrue(compararHechos(hechoDTO1, hechoRepository.findAll().get(0)));
    }
}