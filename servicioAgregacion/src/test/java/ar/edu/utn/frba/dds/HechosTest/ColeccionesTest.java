package ar.edu.utn.frba.dds.HechosTest;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriteriosFechas.CriterioOcurrenciaEntreFechas;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.domain.entities.Contribuyente;
import ar.edu.utn.frba.dds.domain.repository.impl.CategoriasRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.ColeccionesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.FuentesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.HechosRepository;
import ar.edu.utn.frba.dds.services.impl.CategoriaService;
import ar.edu.utn.frba.dds.services.impl.ColeccionesService;
import ar.edu.utn.frba.dds.services.impl.FuentesService;
import ar.edu.utn.frba.dds.services.impl.HechosService;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ColeccionesTest {

    private final ColeccionesRepository coleccionesRepository = new ColeccionesRepository();
    private final HechosRepository hechosRepository = new HechosRepository();
    private final CategoriasRepository categoriasRepository = new CategoriasRepository();
    private final FuentesRepository fuentesRepository = new FuentesRepository();
    private final CategoriaService categoriaService = new CategoriaService(categoriasRepository);
    private final FuentesService fuentesService = new FuentesService(fuentesRepository);
    private final HechosService hechosService = new HechosService(hechosRepository,fuentesService,categoriaService,mock(WebClient.Builder.class));
    private final ColeccionesService coleccionesService = new ColeccionesService(coleccionesRepository,hechosService);


    @Test
    void testCrearColeccion() {
        ColeccionInputDTO input = ColeccionInputDTO.builder()
                .handle("nueva")
                .titulo("Nueva Colección")
                .descripcion("Descripción")
                .listaCriterios(Set.of())
                .build();

        ColeccionOutputDTO result = coleccionesService.crearColeccion(input);

        assertEquals("nueva", result.getHandle());
        assertEquals("Nueva Colección", result.getTitulo());
        assertEquals("Descripción", result.getDescripcion());
    }
    @Test
    void testActualizarColeccion() {
        CriterioOcurrenciaEntreFechas criterio = new CriterioOcurrenciaEntreFechas(LocalDate.of(2020, 1, 1),LocalDate.of(2025, 1, 1));

        Coleccion coleccion = Coleccion.builder()
                .handle("nueva")
                .titulo("Nueva Colección")
                .descripcion("Descripción")
                .build();

        coleccion.agregarCriterio(criterio);

        // Ejemplo de categorías y usuario para los hechos
        Categoria catSeguridad = new Categoria("Seguridad");
        Categoria catMedioAmbiente = new Categoria("Medio Ambiente");
        Categoria catTransito = new Categoria("Tránsito");

        Contribuyente contribuyenteEjemplo = new Contribuyente();
        contribuyenteEjemplo.setNombre("Jose");
        contribuyenteEjemplo.setApellido("Perez");
        contribuyenteEjemplo.setFechaNacimiento(LocalDate.of(2020, 1, 1));
        contribuyenteEjemplo.setEsAnonimo(false);

// Hechos variados:
        HechoFuenteEstatica hecho1 = crearHechoEstatica(
                1L,
                "Robo en el barrio",
                "Se reportó un robo en la calle 123",
                catSeguridad,
                -34.6037,
                -58.3816,
                LocalDate.of(2024, 5, 25)
        );

        HechoFuenteProxy hecho2 = crearHechoProxy(
                2L,
                "Contaminación del río",
                "Elevados niveles de contaminación en el río local",
                catMedioAmbiente,
                -34.6090,
                -58.3940,
                LocalDate.of(2025, 5, 20)
        );

        HechoFuenteDinamica hecho3 = crearHechoDinamica(
                3L,
                "Accidente de tránsito",
                "Choque múltiple en la avenida principal",
                catTransito,
                -34.6000,
                -58.3800,
                LocalDate.of(2024, 5, 26),
                contribuyenteEjemplo
        );

        HechoFuenteEstatica hecho4 = crearHechoEstatica(
                4L,
                "Corte de luz",
                "Corte de luz programado para mantenimiento",
                catSeguridad,
                -34.6015,
                -58.3750,
                LocalDate.of(2025, 5, 24)
        );

        HechoFuenteProxy hecho5 = crearHechoProxy(
                5L,
                "Incendio forestal",
                "Incendio en zona protegida",
                catMedioAmbiente,
                -34.6050,
                -58.3850,
                LocalDate.of(2024, 5, 23)
        );

        List<HechoBase> listaHechos = new ArrayList<>();
        listaHechos.add(hecho1);
        listaHechos.add(hecho2);
        listaHechos.add(hecho3);

        //hechosService.actualizarRepositoryHecho(listaHechos);
        //ToDo

        assertEquals(3,hechosRepository.findAll().size());
        assertEquals(2, coleccion.filtrarHechos(hechosRepository.findAll()).size());

        List<HechoBase> listaHechos2 = new ArrayList<>();
        listaHechos2.add(hecho4);
        listaHechos2.add(hecho5);

        //hechosService.actualizarRepositoryHecho(listaHechos2);
        //Todo
        assertEquals(5,hechosRepository.findAll().size());
        assertEquals(3, coleccion.filtrarHechos(hechosRepository.findAll()).size());
    }

    public HechoFuenteEstatica crearHechoEstatica(Long id, String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fechaDeOcurrencia) {
        var hechoE = new HechoFuenteEstatica();
        hechoE.setOrigenId(id);
        hechoE.setTitulo(titulo);
        hechoE.setDescripcion(descripcion);
        hechoE.setCategoria(categoria);
        var ubicacion = new Ubicacion(latitud,longitud);
        hechoE.setUbicacion(ubicacion);
        hechoE.setFechaDeOcurrencia(fechaDeOcurrencia);
        hechoE.setFechaDeCarga(LocalDateTime.now());

        return hechoE;
    }

    public HechoFuenteProxy crearHechoProxy(Long id, String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fechaDeOcurrencia) {
        var hechoE = new HechoFuenteProxy();
        hechoE.setOrigenId(id);
        hechoE.setTitulo(titulo);
        hechoE.setDescripcion(descripcion);
        hechoE.setCategoria(categoria);
        var ubicacion = new Ubicacion(latitud,longitud);
        hechoE.setUbicacion(ubicacion);
        hechoE.setFechaDeOcurrencia(fechaDeOcurrencia);
        hechoE.setFechaDeCarga(LocalDateTime.now());

        return hechoE;
    }

    public HechoFuenteDinamica crearHechoDinamica(Long id, String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fechaDeOcurrencia, Contribuyente contribuyente) {
        var hechoE = new HechoFuenteDinamica();
        hechoE.setOrigenId(id);
        hechoE.setTitulo(titulo);
        hechoE.setDescripcion(descripcion);
        hechoE.setCategoria(categoria);
        var ubicacion = new Ubicacion(latitud,longitud);
        hechoE.setUbicacion(ubicacion);
        hechoE.setFechaDeOcurrencia(fechaDeOcurrencia);
        hechoE.setContribuyente(contribuyente);
        hechoE.setFechaDeCarga(LocalDateTime.now());

        return hechoE;
    }
}
