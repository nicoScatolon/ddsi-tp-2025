package ar.edu.utn.frba.dds.Hechos.CreacionDeColeccion;

import ar.edu.utn.frba.dds.Criterio.Criterio;
import ar.edu.utn.frba.dds.Criterio.CriterioTitulo;
import ar.edu.utn.frba.dds.Criterio.CriterioCategoria;
import ar.edu.utn.frba.dds.Criterio.CriteriosFechas.*;
import ar.edu.utn.frba.dds.Hechos.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ColeccionTest {

    private Categoria categoria1, categoria2, categoria3, categoria4;
    private Hecho hecho1, hecho2, hecho3, hecho4, hecho5;
    private Set<Hecho> listaHechosFuente;
    private Fuente fuente;
    private Coleccion coleccion;


    @BeforeEach
    public void inicializar() {
        categoria1 = new Categoria("Caída de aeronave");
        categoria2 = new Categoria("Accidente con maquinaria Industrial");
        categoria3 = new Categoria("Accidente en paso a nivel");
        categoria4 = new Categoria("Derrumbe en obra en construcción");

        hecho1 = crearHecho(
                "Caída de aeronave impacta en Olavarría",
                "Grave caída de aeronave ocurrió ...",
                categoria1,
                -36.868375, -60.343297,
                LocalDate.of(2001, 11, 29)
        );
        hecho2 = crearHecho(
                "Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
                "Un grave accidente con maquinaria ...",
                categoria2,
                -37.345571, -70.241485,
                LocalDate.of(2001, 8, 16)
        );
        hecho3 = crearHecho(
                "Caída de aeronave impacta en Venado Tuerto, Santa Fe",
                "Grave caída de aeronave ocurrió ...",
                categoria1,
                -33.768051, -61.921032,
                LocalDate.of(2008, 8, 8)
        );
        hecho4 = crearHecho(
                "Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires",
                "Grave accidente en paso a nivel ...",
                categoria3,
                -35.855811, -61.940589,
                LocalDate.of(2020, 1, 27)
        );
        hecho5 = crearHecho(
                "Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña",
                "Un grave derrumbe en obra en construcción ...",
                categoria4,
                -26.780008, -60.458782,
                LocalDate.of(2016, 6, 4)
        );

        listaHechosFuente = new HashSet<>();
        listaHechosFuente.add(hecho1);
        listaHechosFuente.add(hecho2);
        listaHechosFuente.add(hecho3);
        listaHechosFuente.add(hecho4);
        listaHechosFuente.add(hecho5);
        fuente = new Fuente();
        fuente.setHechos(listaHechosFuente);

        coleccion = new Coleccion("Coleccion prueba");
        coleccion.setDescripcion("Esto es una prueba");
        coleccion.setFuente(fuente);
    }

    private Hecho crearHecho(String titulo,
                             String descripcion,
                             Categoria categoria,
                             Double latitud,
                             Double longitud,
                             LocalDate fechaHecho) {
        Hecho hecho = new Hecho();
        hecho.setTitulo(titulo);
        hecho.setDescripcion(descripcion);
        hecho.setCategoria(categoria);
        hecho.setLatitud(latitud);
        hecho.setLongitud(longitud);
        hecho.setFechaDeOcurrencia(fechaHecho);
        return hecho;
    }

    @Test
    public void testeoCrearColecciones() {
        assertEquals(listaHechosFuente, coleccion.navegar());
    }

    @Test
    public void testCriterioDePertenencia() {
        // Aplico filtro de fechas: de 2000 a 2010 → hechos 1,2,3
        CriterioCargaEntreFechas criterioFechas = new CriterioCargaEntreFechas(LocalDate.of(2000,1,1), LocalDate.of(2010,1,1));
        coleccion.agregarCriterio(criterioFechas);

        Set<Hecho> esperadosFechas = new HashSet<>();
        esperadosFechas.add(hecho1);
        esperadosFechas.add(hecho2);
        esperadosFechas.add(hecho3);
        assertEquals(esperadosFechas, coleccion.navegar());

        // Ahora agrego, además, filtro por categoría = "Caída de aeronave"
        CriterioCategoria criterioCat = new CriterioCategoria(categoria1);
        coleccion.agregarCriterio(criterioCat);

        Set<Hecho> esperadosCat = new HashSet<>();
        esperadosCat.add(hecho1);
        esperadosCat.add(hecho3);
        assertEquals(esperadosCat, coleccion.navegar());
    }

    @Test
    void testNavegarConFiltroDirecto() {
        Criterio criterioCategoria = new CriterioCategoria(categoria1);
        Criterio criterioTitulo = new CriterioTitulo("un título");
        Set<Criterio> listaCriterios = new HashSet<>();
        listaCriterios.add(criterioCategoria);
        listaCriterios.add(criterioTitulo);
      Assertions.assertEquals(0, coleccion.navegarConFiltro(listaCriterios).size());

}
    @Test
    public void testEtiquetado() {
        Etiqueta etiqueta1 = new Etiqueta("Olavarria");
        Etiqueta etiqueta2 = new Etiqueta("Grave");
        hecho1.agregarEtiqueta(etiqueta1);
        hecho1.agregarEtiqueta(etiqueta2);
        Set<Etiqueta> etiquetas = hecho1.getEtiquetas();


        Assertions.assertEquals(2, etiquetas.size());
        Assertions.assertTrue(etiquetas.contains(etiqueta1));
        Assertions.assertTrue(etiquetas.contains(etiqueta2));


    }
}