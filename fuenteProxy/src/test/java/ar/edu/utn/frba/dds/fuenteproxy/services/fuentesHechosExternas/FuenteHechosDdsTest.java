package ar.edu.utn.frba.dds.fuenteproxy.services.fuentesHechosExternas;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.PaginaHechosResponseDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.AuthRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FuenteHechosDdsTest {

    private WebClient webClient;
    private FuenteHechosDds fuente;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        // Mock de WebClient y WebClient.Builder
        webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);

        // Configurar builder para devolver el webClient mockeado
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        // Crear instancia del servicio con el builder mockeado
        fuente = new FuenteHechosDds(webClientBuilder);

        // Setear manualmente los valores @Value (simulación)
        fuente.email = "test@email.com";
        fuente.password = "password123";
        fuente.baseUrl = "https://api-ddsi.disilab.ar";
    }

    @Test
    public void testAutenticar_LoginExitoso() {
        JsonNode responseJson = objectMapper.createObjectNode()
                .put("error", false)
                .put("token", "mocked_token");

        when(webClient.post().uri("/api/login").bodyValue(any(AuthRequestDTO.class))
                .retrieve().bodyToMono(JsonNode.class))
                .thenReturn(Mono.just(responseJson));

        String token = fuente.autenticar().block();
        assertEquals("mocked_token", token);
    }

    @Test
    public void testAutenticar_LoginFallido() {
        JsonNode responseJson = objectMapper.createObjectNode()
                .put("error", true)
                .put("message", "Credenciales inválidas");

        when(webClient.post().uri("/api/login").bodyValue(any(AuthRequestDTO.class))
                .retrieve().bodyToMono(JsonNode.class))
                .thenReturn(Mono.just(responseJson));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            fuente.autenticar().block();
        });
        assertTrue(ex.getMessage().contains("Credenciales inválidas"));
    }

    @Test
    public void testBuscarTodos_NoDevuelveListaVacia() {
        // Simular token válido
        FuenteHechosDds spyFuente = Mockito.spy(fuente);
        doReturn(Mono.just("mocked_token")).when(spyFuente).autenticar();

        // Página 1 con datos y 1 sola página
        PaginaHechosResponseDTO mockPagina = new PaginaHechosResponseDTO();
        mockPagina.setLastPage(1);
        HechoExternoDTO hecho = new HechoExternoDTO();
        hecho.setId(1L);
        mockPagina.setData(List.of(hecho));

        when(webClient.get().uri("/api/desastres?page=1")
                .headers(any())
                .retrieve().bodyToMono(PaginaHechosResponseDTO.class))
                .thenReturn(Mono.just(mockPagina));

        List<HechoExternoDTO> hechos = spyFuente.buscarTodos().block();
        assertNotNull(hechos);
        assertFalse(hechos.isEmpty());
    }

    @Test
    public void testBuscarPorId_DevuelveHechoId1() {
        FuenteHechosDds spyFuente = Mockito.spy(fuente);
        doReturn(Mono.just("mocked_token")).when(spyFuente).autenticar();

        HechoExternoDTO hecho = new HechoExternoDTO();
        hecho.setId(1L);
        hecho.setTitulo("Situación crítica por Precipitación de granizo en Chilecito, La Rioja");

        when(webClient.get().uri("/api/desastres/{id}", 1L)
                .headers(any())
                .retrieve().bodyToMono(HechoExternoDTO.class))
                .thenReturn(Mono.just(hecho));

        HechoExternoDTO resultado = spyFuente.buscarPorId(1L).block();
        assertEquals(1L, resultado.getId());
        assertTrue(resultado.getTitulo().contains("Chilecito"));
    }

    @Test
    public void testBuscarPorId_NoExiste() {
        FuenteHechosDds spyFuente = Mockito.spy(fuente);
        doReturn(Mono.just("mocked_token")).when(spyFuente).autenticar();

        when(webClient.get().uri("/api/desastres/{id}", 999L)
                .headers(any())
                .retrieve()
                .onStatus(eq(status -> true), any())  // simulate 404
                .bodyToMono(HechoExternoDTO.class))
                .thenReturn(Mono.error(new RuntimeException("Desastre no encontrado con ID 999")));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            spyFuente.buscarPorId(999L).block();
        });
        assertTrue(ex.getMessage().contains("Desastre no encontrado"));
    }
}
