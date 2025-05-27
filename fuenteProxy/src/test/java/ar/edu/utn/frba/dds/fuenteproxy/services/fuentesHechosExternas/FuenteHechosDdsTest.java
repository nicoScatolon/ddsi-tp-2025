package ar.edu.utn.frba.dds.fuenteproxy.services.fuentesHechosExternas;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FuenteHechosDdsTest {

    @Autowired
    FuenteHechosDds fuenteHechosDds = new FuenteHechosDds(
            WebClient.builder(),
            "https://api-ddsi.disilab.ar/public",
            "ddsi@gmail.com",
            "ddsi2025*"
    );


    @Test
    @DisplayName("Autenticación Válida")
    void autenticacionValida(){
        String token = fuenteHechosDds.autenticar().block();
        assertNotNull(token);
        assertFalse(token.isEmpty());
        System.out.println("Token recibido: " + token);
    }


    @Test
    @DisplayName("Busca todos los hechos de la api")
    void buscarTodos_valido() {
        List<HechoExternoDTO> lista = fuenteHechosDds.buscarTodos().block();

        assertNotNull(lista, "La lista no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista no debe estar vacía");
    }

    @Test
    @DisplayName("Busca el hecho con Id 1 y lo encuentra de forma correcta")
    void buscarPorId_valido() {
        HechoExternoDTO hecho = fuenteHechosDds.buscarPorId(1L).block();

        assertNotNull(hecho);
        assertEquals(1L, hecho.getId());
        assertEquals("Situación crítica por Precipitación de granizo en Chilecito, La Rioja", hecho.getTitulo());

    }

    @Test
    @DisplayName("Busca un Id inexistente")
    void buscarPorId_inexistente() {
        Long idInexistente = 999999L;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fuenteHechosDds.buscarPorId(idInexistente).block();
        });

        assertTrue(exception.getMessage().contains("Desastre no encontrado"));
    }





}
