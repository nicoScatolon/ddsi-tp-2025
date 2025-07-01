package ar.edu.utn.frba.dds.HechosTest;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriterioOcurrenciaEntreFechas;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteEstatica;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.domain.repository.impl.CategoriasRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.ColeccionesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.FuentesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.HechosRepository;
import ar.edu.utn.frba.dds.services.impl.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ColeccionesTest {
    private final ColeccionesRepository coleccionesRepository = new ColeccionesRepository();
    private final HechosRepository hechosRepository = new HechosRepository();
    private final CategoriasRepository categoriaRepository = new CategoriasRepository();
    private final CategoriaService categoriaService = new CategoriaService(categoriaRepository);
    private final CriterioFactory criterioFactory = new CriterioFactory();
    private final FuentesRepository fuentesRepository = new FuentesRepository();
    private final HechosService hechosService = new HechosService(hechosRepository,categoriaService,criterioFactory);
    private final ColeccionesService coleccionesService = new ColeccionesService(coleccionesRepository,hechosService,criterioFactory, fuentesRepository);
    private final FuentesService fuentesService = new FuentesService(fuentesRepository,hechosService, coleccionesService);

    @Test
    void testCrearColeccion() {
        ColeccionInputDTO input = ColeccionInputDTO.builder()
                .titulo("Nueva Coleccion")
                .descripcion("Descripción")
                .listaCriterios(Set.of())
                .build();

        ColeccionOutputDTO result = coleccionesService.crearColeccion(input);

        assertEquals("nueva-coleccion-1", result.getHandle());
        assertEquals("Nueva Coleccion", result.getTitulo());
        assertEquals("Descripción", result.getDescripcion());
    }
    @Test
    void testActualizarColeccion() {
            ICriterio criterio = new CriterioOcurrenciaEntreFechas(LocalDate.of(2020, 1, 1),LocalDate.of(2025, 1, 1));

            Coleccion coleccion = new Coleccion(
                    "nueva",
                    "Nueva Colección",
                    "descripcion",
                    null
            );
            coleccion.agregarCriterio(criterio);

            // Ejemplo de categorías y usuario para los hechos
            Categoria catSeguridad = new Categoria("Seguridad");
            Categoria catMedioAmbiente = new Categoria("Medio Ambiente");
            Categoria catTransito = new Categoria("Tránsito");


// Hechos variados:
            Hecho hecho1 = crearHechoEstatica(
                    1L,
                    "Robo en el barrio",
                    "Se reportó un robo en la calle 123",
                    catSeguridad,
                    -34.6037,
                    -58.3816,
                    LocalDate.of(2024, 5, 25),
                    1L
            );

            Hecho hecho2 = crearHechoProxy(
                    2L,
                    "Contaminación del río",
                    "Elevados niveles de contaminación en el río local",
                    catMedioAmbiente,
                    -34.6090,
                    -58.3940,
                    LocalDate.of(2025, 5, 20),
                    2L
            );

            Hecho hecho3 = crearHechoDinamica(
                    3L,
                    "Accidente de tránsito",
                    "Choque múltiple en la avenida principal",
                    catTransito,
                    -34.6000,
                    -58.3800,
                    LocalDate.of(2024, 5, 26),
                    3L
            );

            Hecho hecho4 = crearHechoEstatica(
                    4L,
                    "Corte de luz",
                    "Corte de luz programado para mantenimiento",
                    catSeguridad,
                    -34.6015,
                    -58.3750,
                    LocalDate.of(2025, 5, 24),
                    4L
            );

            Hecho hecho5 = crearHechoProxy(
                    5L,
                    "Incendio forestal",
                    "Incendio en zona protegida",
                    catMedioAmbiente,
                    -34.6050,
                    -58.3850,
                    LocalDate.of(2024, 5, 23),
                    5L
            );

        List<Hecho> listaHechos = new ArrayList<>();
        listaHechos.add(hecho1);
        listaHechos.add(hecho2);
        listaHechos.add(hecho3);

        hechosService.actualizarHechosRepository(listaHechos);

        assertEquals(3,hechosRepository.findAll().size());
        assertEquals(2, coleccion.filtrarHechos(hechosRepository.findAll()).size());

        List<Hecho> listaHechos2 = new ArrayList<>();
        listaHechos2.add(hecho4);
        listaHechos2.add(hecho5);

        hechosService.actualizarHechosRepository(listaHechos2);
        assertEquals(5,hechosRepository.findAll().size());
        assertEquals(3, coleccion.filtrarHechos(hechosRepository.findAll()).size());
    }

    @Test
    void testAlmacenarFuente(){
        FuenteInputDTO fuenteDTO = crearFuenteEstatica();
        fuentesService.agregarFuente(fuenteDTO);

        assertEquals(1, fuentesService.buscarFuentes().size());
        assertEquals(1L, fuentesService.buscarFuentes().get(0).getId());
    }

    void testAlmacenarFuenteyHechos(){

    }

    @Test
    void testAlmacenarHechosMock(){
        FuenteInputDTO fuenteDTO = crearFuenteEstatica();
        fuentesService.agregarFuente(fuenteDTO);
        List<Hecho> listHechos = this.crearLista();
        FuenteEstatica fuenteEjemplo = new FuenteEstatica("url/lol");
        fuenteEjemplo.actualizarHechos(listHechos);
        Coleccion coleccion = new Coleccion("1", "titulo", "desc", null);
        coleccion.agregarCriterio(new CriterioOcurrenciaEntreFechas(LocalDate.MIN, LocalDate.MAX));
        coleccion.agregarFuente(fuenteEjemplo);
        coleccion.actualizarHechos();
        coleccion.setTitulo("lol");
    }

    @Test
    void testAlmacenarHechos(){
        FuenteInputDTO fuenteDTO = crearFuenteEstatica();
        fuentesService.agregarFuente(fuenteDTO);
        FuenteAdapter fuenteAdapter = TipoFuente.ESTATICA.crearAdapter(fuentesService.buscarFuentePorId(1L));

        assertEquals(0,fuenteAdapter.obtenerHechos().size());
    }

    public FuenteInputDTO crearFuenteEstatica(){
        FuenteInputDTO fuenteDTO = new FuenteInputDTO();
        fuenteDTO.setNombre("Robo en el barrio");
        fuenteDTO.setUrl("url/mock");
        fuenteDTO.setTipoFuente(TipoFuente.ESTATICA);
        fuenteDTO.setId(444L);

        return fuenteDTO;
    }

    public Hecho crearHechoEstatica(Long id, String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fechaDeOcurrencia, Long origenID) {
        var hechoE = new Hecho();
        hechoE.setTitulo(titulo);
        hechoE.setDescripcion(descripcion);
        hechoE.setCategoria(categoria);
        var ubicacion = new Ubicacion(latitud,longitud);
        hechoE.setUbicacion(ubicacion);
        hechoE.setFechaDeOcurrencia(fechaDeOcurrencia);
        hechoE.setFechaDeCarga(LocalDateTime.now());
        hechoE.setOrigenId(origenID);

        return hechoE;
    }

    public Hecho crearHechoProxy(Long id, String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fechaDeOcurrencia, Long origenID) {
        var hechoE = new Hecho();
        hechoE.setTitulo(titulo);
        hechoE.setDescripcion(descripcion);
        hechoE.setCategoria(categoria);
        var ubicacion = new Ubicacion(latitud,longitud);
        hechoE.setUbicacion(ubicacion);
        hechoE.setFechaDeOcurrencia(fechaDeOcurrencia);
        hechoE.setFechaDeCarga(LocalDateTime.now());
        hechoE.setOrigenId(origenID);

        return hechoE;
    }

    public Hecho crearHechoDinamica(Long id, String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fechaDeOcurrencia, Long origenID) {
        var hechoE = new Hecho();
        hechoE.setTitulo(titulo);
        hechoE.setDescripcion(descripcion);
        hechoE.setCategoria(categoria);
        var ubicacion = new Ubicacion(latitud,longitud);
        hechoE.setUbicacion(ubicacion);
        hechoE.setFechaDeOcurrencia(fechaDeOcurrencia);
        hechoE.setFechaDeCarga(LocalDateTime.now());
        hechoE.setOrigenId(origenID);

        return hechoE;
    }

    public List<Hecho> crearLista(){
        ICriterio criterio = new CriterioOcurrenciaEntreFechas(LocalDate.of(2020, 1, 1),LocalDate.of(2025, 1, 1));

        Coleccion coleccion = new Coleccion(
                "nueva",
                "Nueva Colección",
                "descripcion",
                null
        );
        coleccion.agregarCriterio(criterio);

        // Ejemplo de categorías y usuario para los hechos
        Categoria catSeguridad = new Categoria("Seguridad");
        Categoria catMedioAmbiente = new Categoria("Medio Ambiente");
        Categoria catTransito = new Categoria("Tránsito");


// Hechos variados:
        Hecho hecho1 = crearHechoEstatica(
                1L,
                "Robo en el barrio",
                "Se reportó un robo en la calle 123",
                catSeguridad,
                -34.6037,
                -58.3816,
                LocalDate.of(2024, 5, 25),
                1L
        );

        Hecho hecho2 = crearHechoProxy(
                2L,
                "Contaminación del río",
                "Elevados niveles de contaminación en el río local",
                catMedioAmbiente,
                -34.6090,
                -58.3940,
                LocalDate.of(2025, 5, 20),
                2L
        );

        Hecho hecho3 = crearHechoDinamica(
                3L,
                "Accidente de tránsito",
                "Choque múltiple en la avenida principal",
                catTransito,
                -34.6000,
                -58.3800,
                LocalDate.of(2024, 5, 26),
                3L
        );

        Hecho hecho4 = crearHechoEstatica(
                4L,
                "Corte de luz",
                "Corte de luz programado para mantenimiento",
                catSeguridad,
                -34.6015,
                -58.3750,
                LocalDate.of(2025, 5, 24),
                4L
        );

        Hecho hecho5 = crearHechoProxy(
                5L,
                "Incendio forestal",
                "Incendio en zona protegida",
                catMedioAmbiente,
                -34.6050,
                -58.3850,
                LocalDate.of(2024, 5, 23),
                5L
        );

        return List.of(hecho1, hecho2,hecho3, hecho4,hecho5);
    }

}