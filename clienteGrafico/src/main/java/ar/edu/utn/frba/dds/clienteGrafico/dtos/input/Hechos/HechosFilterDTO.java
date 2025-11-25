package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechosFilterDTO {
    private String categoria;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fReporteDesde;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fReporteHasta;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fAconDesde;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fAconHasta;

    private Double latitud;
    private Double longitud;


    public Map<String, String> asMap() {
        Map<String, String> map = new HashMap<>();

        if (categoria != null && !categoria.isEmpty()) {
            map.put("categoria", categoria);
        }
        if (fReporteDesde != null) {
            map.put("fReporteDesde", fReporteDesde.toString());
        }
        if (fReporteHasta != null) {
            map.put("fReporteHasta", fReporteHasta.toString());
        }
        if (fAconDesde != null) {
            map.put("fAconDesde", fAconDesde.toString());
        }
        if (fAconHasta != null) {
            map.put("fAconHasta", fAconHasta.toString());
        }
        if (latitud != null) {
            map.put("latitud", latitud.toString());
        }
        if (longitud != null) {
            map.put("longitud", longitud.toString());
        }

        return map;
    }


}
