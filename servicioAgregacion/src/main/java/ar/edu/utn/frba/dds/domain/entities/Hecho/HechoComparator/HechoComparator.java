package ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.time.LocalDate;
import java.util.Objects;

public class HechoComparator {
    private static final HechoComparator INSTANCE = new HechoComparator();

    private HechoComparator() { }

    public static HechoComparator getInstance() {
        return INSTANCE;
    }

    public boolean compararTitulo(Hecho h1, Hecho h2) {
        return Objects.equals(h1.getTitulo(), h2.getTitulo());
    }

    public boolean compararDescripcion(Hecho h1, Hecho h2) {
        return Objects.equals(h1.getDescripcion(), h2.getDescripcion());
    }

    public boolean compararCategoria(Hecho h1, Hecho h2) {
        return Objects.equals(h1.getCategoria(), h2.getCategoria());
    }

    public boolean compararUbicacion(Hecho h1, Hecho h2) {
        return Objects.equals(h1.getUbicacion(), h2.getUbicacion());
    }

    private boolean compararFechaDeOcurrencia(Hecho h1, Hecho h2) {
        if (h1 == null || h2 == null || h1.getFechaDeOcurrencia() == null || h2.getFechaDeOcurrencia() == null) {
            return false;
        }

        LocalDate f1 = h1.getFechaDeOcurrencia();
        LocalDate f2 = h2.getFechaDeOcurrencia();

        long diasDeDiferencia = Math.abs(f1.toEpochDay() - f2.toEpochDay());
        return diasDeDiferencia <= 1;
    }


    // Comparación completa (opcional, usa todos los anteriores)
    public boolean compararHechos(Hecho h1, Hecho h2) {
        if (h1 == h2) return true;
        if (h1 == null || h2 == null) return false;

        return compararTitulo(h1, h2) &&
                compararDescripcion(h1, h2) &&
                compararCategoria(h1, h2) &&
                compararUbicacion(h1, h2) &&
                compararFechaDeOcurrencia(h1, h2);
    }
}
