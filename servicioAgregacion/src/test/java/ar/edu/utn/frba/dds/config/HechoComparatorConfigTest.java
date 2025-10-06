package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.HechoComparator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class HechoComparatorConfigTest {
    @Test
    void testInicializacionDesdeProperties() {
        HechoComparator comparator = HechoComparator.getInstance();

        List<String> nombres = comparator.getListaComandos()
                .stream()
                .map(c -> c.getClass().getSimpleName())
                .toList();

        assertTrue(nombres.contains("CompararCategoria"));
        assertTrue(nombres.contains("CompararTitulo"));
        assertFalse(nombres.contains("CompararFechaDeOcurrencia"));
    }

    @Test
    void test2(){
        Hecho h1 = new Hecho();
        var categoria = new Categoria("cat1" ,"Viento fuerte");
        h1.setCategoria(categoria);
        h1.setTitulo("Ráfagas intensas");
        h1.setDescripcion("asfasfjhnajskfnajkh1");

        Hecho h2 = new Hecho();
        h2.setCategoria(categoria);
        h2.setTitulo("Ráfagas intensas");
        h2.setDescripcion("asfaaaaaaaaaaaaaaaaaaaaaaaa2");

        boolean iguales = HechoComparator.getInstance().compararHechos(h1, h2);
        System.out.println("¿Son iguales según comparadores activos? " + iguales);
        assertTrue(iguales);
    }
}