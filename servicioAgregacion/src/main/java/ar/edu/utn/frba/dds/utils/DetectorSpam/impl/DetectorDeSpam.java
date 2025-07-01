package ar.edu.utn.frba.dds.utils.DetectorSpam.impl;

import ar.edu.utn.frba.dds.utils.DetectorSpam.IDetectorDeSpam;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DetectorDeSpam implements IDetectorDeSpam {

    private static final List<String> PALABRAS_SPAM = List.of(
            "gratis", "oferta", "urgente", "haz-clic", "gana-dinero", "promocion",
            "compra-ya", "descuento", "sorteo", "credito-facil"
    );

    private static final int REPETICION_PALABRA_UMBRAL = 5;
    private static final int EXCESO_SIGNOS_UMBRAL = 5;
    private static final int MAYUSCULA_UMBRAL_PORCENTAJE = 80;
    private static final int URL_UMBRAL = 2;
    private static final int CARACTERES_NO_LETRA_UMBRAL = 100;

    @Override
    public boolean esSpam(String texto) {
        String textoNormalizado = this.stringToHandle(texto);

        return contienePalabrasSpam(textoNormalizado) ||
                contienePalabrasRepetidas(textoNormalizado) ||
                contieneExcesoDeSignos(texto) ||
                esMayormenteMayusculas(texto) ||
                contieneMuchasUrls(texto) ||
                contieneMuchosCaracteresNoAlfabeticos(texto);
    }

    private String stringToHandle(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("El string no puede ser nulo ni vacío.");
        }

        return string
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")  // reemplaza caracteres no alfanuméricos por guiones
                .replaceAll("^-+|-+$", "");     // quita guiones al inicio o final
    }


    private boolean contienePalabrasSpam(String texto) {
        return PALABRAS_SPAM.stream().anyMatch(texto::contains);
    }

    private boolean contienePalabrasRepetidas(String texto) {
        String[] palabras = texto.split("\\s+");
        Map<String, Integer> frecuencia = new HashMap<>();

        for (String palabra : palabras) {
            palabra = palabra.replaceAll("[^a-zA-Záéíóúñü]", ""); // quitar signos
            if (palabra.isBlank()) continue;

            frecuencia.put(palabra, frecuencia.getOrDefault(palabra, 0) + 1);
            if (frecuencia.get(palabra) >= REPETICION_PALABRA_UMBRAL) {
                return true;
            }
        }
        return false;
    }

    private boolean contieneExcesoDeSignos(String texto) {
        long signos = texto.chars()
                .filter(c -> c == '!' || c == '?')
                .count();
        return signos >= EXCESO_SIGNOS_UMBRAL;
    }

    private boolean esMayormenteMayusculas(String texto) {
        if (texto.isBlank()) return false;

        long letras = texto.chars().filter(Character::isLetter).count();
        if (letras == 0) return false;

        long mayusculas = texto.chars().filter(Character::isUpperCase).count();
        double porcentaje = (double) mayusculas / letras * 100;

        return porcentaje >= MAYUSCULA_UMBRAL_PORCENTAJE;
    }

    private boolean contieneMuchasUrls(String texto) {
        Pattern urlPattern = Pattern.compile("http[s]?://\\S+");
        Matcher matcher = urlPattern.matcher(texto);

        int count = 0;
        while (matcher.find()) {
            count++;
            if (count >= URL_UMBRAL) return true;
        }
        return false;
    }

    private boolean contieneMuchosCaracteresNoAlfabeticos(String texto) {
        long count = texto.chars()
                .filter(c -> !Character.isLetterOrDigit(c) && !Character.isWhitespace(c))
                .count();
        return count >= CARACTERES_NO_LETRA_UMBRAL;
    }
}

