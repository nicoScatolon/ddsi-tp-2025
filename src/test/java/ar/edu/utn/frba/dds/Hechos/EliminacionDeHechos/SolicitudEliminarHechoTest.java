package ar.edu.utn.frba.dds.Hechos.EliminacionDeHechos;

import ar.edu.utn.frba.dds.Hechos.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


class SolicitudEliminarHechoTest {
    Usuario admin = new Usuario();
    Usuario contribuyente = new Usuario();

    Ubicacion ubicacionHecho = new Ubicacion();


    public Hecho crearHechos(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fechaHecho) {
        ubicacionHecho.setLatitud(latitud);
        ubicacionHecho.setLongitud(longitud);

        Hecho hecho = new Hecho();
        hecho.setTitulo(titulo);
        hecho.setDescripcion(descripcion);
        hecho.setCategoria(categoria);
        hecho.setUbicacion(ubicacionHecho);
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
        admin.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
        contribuyente.setTipoUsuario(TipoUsuario.CONTRIBUYENTE);
        admin.setNombre("Pedro");
        admin.setApellido("Suarez");
        contribuyente.setNombre("Juan");
        contribuyente.setApellido("Perez");


        // - Crear una solicitud de eliminación asociada a este hecho.
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion.constructorSolicitud(elHecho,this.motivoValido(), contribuyente.getNombre(),contribuyente.getApellido());

        Assertions.assertNotNull(solicitud);
        Assertions.assertEquals(elHecho,solicitud.getHecho());



        // - Quedará en estado pendiente.
        Assertions.assertEquals(EstadoDeSolicitud.PENDIENTE, solicitud.getEstado());

        // - El motivo de esta solicitud deberá tener al menos 500 caracteres.
        String motivoInvalido = "motivo corto";

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> ConstructorSolicitudesEliminacion.constructorSolicitud(elHecho, motivoInvalido, contribuyente.getNombre(),contribuyente.getApellido()));

        // - Rechazar esta solicitud un día después de su creación.
        solicitud.serRechazada(admin.getNombre(),admin.getApellido());


        Fuente fuente = new Fuente();
        fuente.getHechos().add(elHecho);

        Coleccion coleccion = new Coleccion("Colección de Prueba");
        coleccion.setFuente(fuente);

        // - Dado que fue rechazada, el hecho puede ser agregado a cualquier colección.
        Assertions.assertTrue(coleccion.getHechos().contains(elHecho));

        // - Verificar que la solicitud haya quedado en estado rechazada.
        Assertions.assertEquals(EstadoDeSolicitud.RECHAZADA, solicitud.getEstado());
    }


    @Test
    @DisplayName("Revisar una solicitud y aceptarla, queda el hecho fuera de cualquier colección.")
    public void AceptarSolicitud() {
        admin.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
        contribuyente.setTipoUsuario(TipoUsuario.CONTRIBUYENTE);
        admin.setNombre("Pedro");
        admin.setApellido("Suarez");
        contribuyente.setNombre("Juan");
        contribuyente.setApellido("Perez");

        // - Generar otra solicitud para el mismo hecho.
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion.constructorSolicitud(elHecho,this.motivoValido(), contribuyente.getNombre(),contribuyente.getApellido());

        Assertions.assertNotNull(solicitud);
        Assertions.assertEquals(EstadoDeSolicitud.PENDIENTE, solicitud.getEstado());

        // - Ahora esta solicitud es aceptada a las 2 horas.
        solicitud.serAceptada(admin.getNombre(),admin.getApellido());


        Assertions.assertEquals(EstadoDeSolicitud.ACEPTADA, solicitud.getEstado());
        // - Verificar que la solicitud haya quedado en estado aceptada.
        Assertions.assertTrue(elHecho.getFueEliminado());

        Fuente fuente = new Fuente();
        fuente.getHechos().add(elHecho);

        Coleccion coleccion = new Coleccion("Colección de Prueba");
        coleccion.setFuente(fuente);

        // - Esta vez el hecho no debería poder agregarse a una colección, puesto que este fue eliminado.
        Assertions.assertFalse(coleccion.getHechos().contains(elHecho));
    }
}

