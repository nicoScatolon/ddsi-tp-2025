package ar.edu.utn.frba.dds.Hechos.EliminacionDeHechos;

import ar.edu.utn.frba.dds.Hechos.Categoria;
import ar.edu.utn.frba.dds.Hechos.Coleccion;
import ar.edu.utn.frba.dds.Hechos.Fuente;
import ar.edu.utn.frba.dds.Hechos.Hecho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


class SolicitudEliminarHechoTest {

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

    String titulo= "Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe";
    String descripcion = "Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.";
    Categoria categoria = new Categoria("Evento sanitario");
    Double latitud= -32.786098;
    Double longitud= -60.741543;
    LocalDate fechaHecho = LocalDate.of(2005,7,5);


    Hecho elHecho = crearHechos(titulo, descripcion, categoria, latitud, longitud, fechaHecho);

    public String motivoValido(){
        return "a".repeat(500);
    }

    @Test
    @DisplayName("Crear una solicitud, revisarla y rechazarla, el hecho puede sumarse a cualquier colección.")
    public void RechazarSolicitud() {
        // - Crear una solicitud de eliminación asociada a este hecho.
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion.constructorSolicitud(elHecho,this.motivoValido());

        Assertions.assertNotNull(solicitud);
        Assertions.assertEquals(elHecho,solicitud.getHecho());


        // - Quedará en estado pendiente.
        Assertions.assertEquals(EstadoDeSolicitud.PENDIENTE, solicitud.getEstado());

        // - El motivo de esta solicitud deberá tener al menos 500 caracteres.
        String motivoInvalido = "motivo corto";

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> ConstructorSolicitudesEliminacion.constructorSolicitud(elHecho, motivoInvalido));

        // - Rechazar esta solicitud un día después de su creación.
        solicitud.serRechazada();


        Fuente fuente = new Fuente();
        fuente.getHechos().add(elHecho);

        Coleccion coleccion = new Coleccion("Colección de Prueba");
        coleccion.setFuente(fuente);

        // - Dado que fue rechazada, el hecho puede ser agregado a cualquier colección.
        Assertions.assertTrue(coleccion.getListaHechos().contains(elHecho));

        // - Verificar que la solicitud haya quedado en estado rechazada.
        Assertions.assertEquals(EstadoDeSolicitud.RECHAZADA, solicitud.getEstado());
    }



    @Test
    @DisplayName("Revisar una solicitud y aceptarla, queda el hecho fuera de cualquier colección.")
    public void AceptarSolicitud() {

        // - Generar otra solicitud para el mismo hecho.
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion.constructorSolicitud(elHecho,this.motivoValido());

        Assertions.assertNotNull(solicitud);
        Assertions.assertEquals(EstadoDeSolicitud.PENDIENTE, solicitud.getEstado());

        // - Ahora esta solicitud es aceptada a las 2 horas.

        solicitud.serAceptada();

        Assertions.assertEquals(EstadoDeSolicitud.ACEPTADA, solicitud.getEstado());
        // - Verificar que la solicitud haya quedado en estado aceptada.
        Assertions.assertTrue(elHecho.getFueEliminado());

        Fuente fuente = new Fuente();
        fuente.getHechos().add(elHecho);

        Coleccion coleccion = new Coleccion("Colección de Prueba");
        coleccion.setFuente(fuente);

        // - Esta vez el hecho no debería poder agregarse a una colección, puesto que este fue eliminado.
        Assertions.assertFalse(coleccion.getListaHechos().contains(elHecho));

    }

}


