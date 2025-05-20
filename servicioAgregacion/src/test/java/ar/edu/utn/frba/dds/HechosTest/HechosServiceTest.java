package ar.edu.utn.frba.dds.HechosTest;

import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputResponse;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.domain.repository.ICategoriaRepository;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.FuentesRepository;
import ar.edu.utn.frba.dds.services.impl.HechosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class HechosServiceTest {
    private IFuentesRepository fuentesRepository;
    private IHechosRepository hechosRepository;
    private ICategoriaRepository categoriaRepository;
    private MockWebServer server;
    private HechosService hechosService;

    @BeforeEach
    void setUp() throws IOException {
        hechosRepository = Mockito.mock(IHechosRepository.class);
        categoriaRepository = Mockito.mock(ICategoriaRepository.class);
        fuentesRepository = Mockito.mock(IFuentesRepository.class);

        server = new MockWebServer();
        server.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(server.url("/").toString())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        WebClient.Builder dummyBuilder = WebClient.builder().baseUrl("http://localhost:8081");
        //hechosService = new HechosService(hechosRepository, categoriaRepository, server.url("/").toString());
        //ToDo: Da error
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    void testCrearHecho_guardaYDevuelveDTO() {
        HechoInputDTO input = HechoInputDTO.builder()
                .titulo("Título de prueba")
                .descripcion("Descripción de prueba")
                .categoria(new Categoria("Ambiente"))
                .ubicacion(new Ubicacion(11.0, 11.0))
                .fechaDeOcurrencia(LocalDate.now())
                .build();

        Mockito.doAnswer(invocation -> {
            Hecho h = invocation.getArgument(0);
            h.setId(1L);
            return null;
        }).when(hechosRepository).save(Mockito.any());

        HechoOutputDTO output = hechosService.crearHecho(input);

        Assertions.assertEquals("Título de prueba", output.getTitulo());
        Assertions.assertEquals(1L, output.getId());
    }

    @Test
    void testBuscarHechoPorId_devuelveDTO() {
        Hecho hecho = Hecho.builder()
                .id(1L)
                .titulo("Título")
                .descripcion("Descripción")
                .categoria(new Categoria("Ambiente"))
                .ubicacion(new Ubicacion(11.0, 11.0))
                .fechaDeOcurrencia(LocalDate.of(2024, 5, 1))
                .build();

        Mockito.when(hechosRepository.findById(1L)).thenReturn(hecho);

        HechoOutputDTO dto = hechosService.buscarHechoPorId(1L);

        Assertions.assertEquals("Título", dto.getTitulo());
        Assertions.assertEquals("Ambiente", dto.getCategoria().getNombre());
    }
    @Test
    void testObtenerTodosLosHechos() throws Exception {
        HechoInputDTO dto = HechoInputDTO.builder()
                .titulo("Título")
                .descripcion("Descripción")
                .categoria(new Categoria("Ambiente"))
                .ubicacion(new Ubicacion(11.0, 11.0))
                .fechaDeOcurrencia(LocalDate.now())
                .build();

        HechoInputResponse response = new HechoInputResponse(List.of(dto));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = mapper.writeValueAsString(response);

        server.enqueue(new MockResponse()
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        List<Hecho> hechos = hechosService.obtenerTodosLasHechos(fuentesRepository.obtenerFuentes());

        Assertions.assertEquals(1, hechos.size());
        Assertions.assertEquals("Título", hechos.get(0).getTitulo());
    }
}
