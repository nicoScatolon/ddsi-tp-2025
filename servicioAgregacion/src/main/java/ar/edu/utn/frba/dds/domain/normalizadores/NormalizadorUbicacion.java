package ar.edu.utn.frba.dds.domain.normalizadores;



import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ar.edu.utn.frba.dds.domain.normalizadores.NormalizadorTexto.normalizarTexto;


public final class NormalizadorUbicacion {

    private static final Map<String, List<String>> PROVINCIAS_EQUIVALENTES = Map.ofEntries(
            Map.entry("Ciudad Autónoma de Buenos Aires", List.of("caba", "capital federal", "ciudad autonoma de buenos aires")),
            Map.entry("Buenos Aires", List.of("bs as", "buenos aires", "provincia de buenos aires", "pba")),
            Map.entry("Catamarca", List.of("catamarca", "cta")),
            Map.entry("Córdoba", List.of("cordoba", "cba", "cordoba prov", "provincia de cordoba")),
            Map.entry("Corrientes", List.of("corrientes", "ctes")),
            Map.entry("Chaco", List.of("chaco", "cc", "chs")),
            Map.entry("Chubut", List.of("chubut", "cht")),
            Map.entry("Entre Ríos", List.of("entre rios", "er")),
            Map.entry("Formosa", List.of("formosa", "fsa")),
            Map.entry("Jujuy", List.of("jujuy", "jjy")),
            Map.entry("La Pampa", List.of("la pampa", "lp", "pampa")),
            Map.entry("La Rioja", List.of("la rioja", "lr", "rioja")),
            Map.entry("Mendoza", List.of("mendoza", "mza")),
            Map.entry("Misiones", List.of("misiones", "mis")),
            Map.entry("Neuquén", List.of("neuquen", "nqn")),
            Map.entry("Río Negro", List.of("rio negro", "r negro", "rno")),
            Map.entry("Salta", List.of("salta", "sla")),
            Map.entry("San Juan", List.of("san juan", "sj")),
            Map.entry("San Luis", List.of("san luis", "sl")),
            Map.entry("Santa Cruz", List.of("santa cruz", "scz", "sta cruz")),
            Map.entry("Santa Fe", List.of("santa fe", "sta fe", "sf")),
            Map.entry("Santiago del Estero", List.of("santiago del estero", "sgo del estero", "sde")),
            Map.entry("Tierra del Fuego, Antártida e Islas del Atlántico Sur", List.of("tierra del fuego", "tdf", "t del fuego")),
            Map.entry("Tucumán", List.of("tucuman", "tuc", "tucuman prov", "tucu"))
    );


    private static final Map<String, String> ALIAS_A_OFICIAL;
    static {
        Map<String, String> tmp = new HashMap<>();
        for (var e : PROVINCIAS_EQUIVALENTES.entrySet()) {
            for (String alias : e.getValue()) {
                tmp.put(alias, e.getKey());
            }
        }
        ALIAS_A_OFICIAL = Collections.unmodifiableMap(tmp);
    }


    public static void normalizarUbicacion(UbicacionInputDTO dto) {
        if (dto == null || dto.getProvincia() == null || dto.getProvincia().isBlank()) {
            throw new IllegalArgumentException("La provincia no puede ser nula o vacía.");
        }

        String originalTrim = dto.getProvincia().trim();

        // 1) Busco si matchea primero por el nombre oficial
        if (PROVINCIAS_EQUIVALENTES.containsKey(originalTrim)) {
            dto.setProvincia(originalTrim);
            return;
        }


        // 2) Normalizo la provincia y veo si matchea con algun alias
        String normalizada = normalizarTexto(originalTrim);
        String oficial = ALIAS_A_OFICIAL.get(normalizada);
        if (oficial != null) {
            dto.setProvincia(oficial);
            return;
        }

        throw new IllegalArgumentException("Provincia no reconocida: " + dto.getProvincia());
    }

    }


