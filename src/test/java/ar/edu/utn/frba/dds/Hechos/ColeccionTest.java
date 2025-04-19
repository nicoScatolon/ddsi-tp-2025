package ar.edu.utn.frba.dds.Hechos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;

class ColeccionTest {

    public Hecho crearHechos(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fechaHecho) {
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
    public void testeoColecciones() {

        Categoria categoria1 = new Categoria("Caída de aeronave");
        Categoria categoria2 = new Categoria("Accidente con maquinaria Industrial");
        Categoria categoria3 = new Categoria("Accidente en paso a nivel");
        Categoria categoria4 = new Categoria("Derrumbe en obra en construcción");

        Hecho hecho1 = crearHechos("Caída de aeronave impacta en Olavarría", "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                categoria1, -36.868375, -60.343297, LocalDate.of(2001, 11, 29));
        Hecho hecho2 = crearHechos("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén", "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                categoria2, -37.345571, -70.241485, LocalDate.of(2001, 8, 16));
        Hecho hecho3 = crearHechos("Caída de aeronave impacta en Venado Tuerto, Santa Fe", "Grave caída de aeronave ocurrió en las inmediaciones de Venado Tuerto, Santa Fe. El incidente destruyó viviendas y dejó a familias evacuadas. Autoridades nacionales se han puesto a disposición para brindar asistencia.",
                categoria1, -33.768051, -61.921032, LocalDate.of(2008,8,8));
        Hecho hecho4 = crearHechos("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires","Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.",
                categoria3, -35.855811, -61.940589, LocalDate.of(2020, 1,27));
        Hecho hecho5 = crearHechos("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña", "Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.",
                categoria4, -26.780008, -60.458782, LocalDate.of(2016,6,4));

        Coleccion coleccion = new Coleccion("Coleccion prueba");
        coleccion.setDescripcion("Esto es una prueba");

        Set<Hecho> listaHechosFuente = new HashSet<>();
        listaHechosFuente.add(hecho1);
        listaHechosFuente.add(hecho2);
        listaHechosFuente.add(hecho3);
        listaHechosFuente.add(hecho4);
        listaHechosFuente.add(hecho5);

        Fuente fuente = new Fuente();
        fuente.setHechos(listaHechosFuente);

        coleccion.setFuente(fuente);

        Assertions.assertEquals(listaHechosFuente, coleccion.navegar());
    }
}