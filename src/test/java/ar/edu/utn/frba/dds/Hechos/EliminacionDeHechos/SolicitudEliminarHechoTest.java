package ar.edu.utn.frba.dds.Hechos.EliminacionDeHechos;

import ar.edu.utn.frba.dds.Hechos.Categoria;
import ar.edu.utn.frba.dds.Hechos.Hecho;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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

/*
    @Test
    @DisplayName("Crear una solicitud de eliminación asociada a este hecho. Quedará en estado pendiente.")
    public void test01_crearSolicitudPendiente(){
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion.constructorSolicitud(elHecho,this.motivoValido());

        assertNotNull(solicitud);
        assertEquals(elHecho,solicitud.getHecho());
        assertEquals(motivoValido(),solicitud.getRazonDeEliminacion());
        assertEquals(EstadoDeSolicitud.PENDIENTE, solicitud.getEstado());
    }


    @Test
    @DisplayName("El motivo de esta solicitud deberá tener al menos 500 caracteres.")

    public void test02_motivoMuyCorto_lanzaExcepcion(){
        String motivoInvalido = "motivo corto";
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion.constructorSolicitud(elHecho,motivoInvalido);


        assertThrows(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("Rechazar esta solicitud un día después de su creación. Dado que fue rechazada, " +
            "el hecho puede ser agregado a cualquier colección. ")
    public void test03_rechazoDeSolicitud_unDiaDespues(){
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion.constructorSolicitud(elHecho,this.motivoValido());

        solicitud.serRechazada();

        assertEquals(EstadoDeSolicitud.RECHAZADA, solicitud.getEstado());
    }

    @Test
    @DisplayName("Verificar que la solicitud haya quedado en estado rechazada.")
    public void test04_segundaSolicitudAceptada_yEliminaHecho() {
        // Primera solicitud rechazada
        SolicitudEliminarHecho solicitud1 = new ConstructorSolicitudesEliminacion()
                .sumarHecho(elHecho)
                .sumarRazonDeEliminacion(motivoValido())
                .constructor();
        solicitud1.serRechazada();

        // Segunda solicitud aceptada
        SolicitudEliminarHecho solicitud2 = new ConstructorSolicitudesEliminacion()
                .sumarHecho(elHecho)
                .sumarRazonDeEliminacion(motivoValido())
                .constructor();
        solicitud2.setFechaCreacion(LocalDateTime.now().minusHours(2));
        solicitud2.aceptar();

        assertEquals(EstadoDeSolicitud.ACEPTADA, solicitud2.getEstado());
        assertFalse(elHecho.puedeSerAgregadoAColeccion());
    }


*/
    @Test
    @DisplayName("Generar otra solicitud para el mismo hecho.")
    public void GenerarSolicitudMismoHecho(){
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion.constructorSolicitud(elHecho,this.motivoValido());

        solicitud.serAceptada();

    }

    @Test
    @DisplayName("Ahora esta solicitud es aceptada a las 2 horas. " +
            "Esta vez el hecho no debería poder agregarse a una colección, puesto que este fue eliminado.")
    public void AceptarSolicitud(){}

    @Test
    @DisplayName("Verificar que la solicitud haya quedado en estado aceptada.")
    public void VerificarSolicitudAceptada(){}


}


