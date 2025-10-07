package ar.edu.utn.frba.dds.domain.entities.Normalizadores;

import java.text.Normalizer;

public final class NormalizadorTexto {
    private NormalizadorTexto() {}

    public static String normalizarTexto(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("El texto no puede ser nulo ni vacío.");
        }

        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }
}
