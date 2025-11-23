package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import ar.edu.utn.frba.dds.domain.entities.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class GeneradorEstadisticas {
    private static final GeneradorEstadisticas INSTANCE = new GeneradorEstadisticas();

    public static GeneradorEstadisticas getInstance() {
        return INSTANCE;
    }

    public E_MayorProvinciaPorCategoria mayorProvinciaPorCategoria (String cod_Categoria, List<Hecho> hechos) {
        var maxProvincia = this.conteoMaxProvincia(hechos);

        if (maxProvincia == null) { return null;}

        var estadistica = E_MayorProvinciaPorCategoria.builder()
                .categoria(hechos.get(0).getCategoria().getNombre())
                .codigoCategoria(cod_Categoria)
                .provincia(maxProvincia.getKey())
                .cantHechosProvincia(maxProvincia.getValue().intValue())
                .cantHechosTotales(hechos.size())
                .build();
        estadistica.setFechaDeCalculo(LocalDateTime.now());
        return estadistica;
    }

    public E_MayorProvinciaPorColeccion mayorProvinciaPorColeccion (Coleccion coleccion, List<Hecho> hechos) {
        var maxProvincia = this.conteoMaxProvincia(hechos);

        if (maxProvincia == null) { return null;}

        var estadistica = E_MayorProvinciaPorColeccion.builder()
                .coleccion(coleccion)
                .provincia(maxProvincia.getKey())
                .cantHechosProvincia(maxProvincia.getValue().intValue())
                .cantHechosTotales(hechos.size())
                .build();
        estadistica.setFechaDeCalculo(LocalDateTime.now());
        return estadistica;
    }

    public E_HoraOcurrenciaPorCategoria horaDiaPorCategoria (String categoria_id, List<Hecho> hechos) {
        Map<Integer, Long> conteoHoras = hechos.stream()
                .collect(Collectors.groupingBy(h -> h.getFechaDeOcurrencia().getHour(), Collectors.counting()));

        if (conteoHoras.isEmpty()) return null;

        var maxHora = conteoHoras.entrySet().stream().max(Map.Entry.comparingByValue()).get();

        var estadistica = E_HoraOcurrenciaPorCategoria.builder()
                .categoria(hechos.get(0).getCategoria().getNombre())
                .codigoCategoria(categoria_id)
                .horaDia(maxHora.getKey())
                .cantHechosHora(maxHora.getValue().intValue())
                .cantHechosTotales(hechos.size())
                .build();

        estadistica.setFechaDeCalculo(LocalDateTime.now());
        return estadistica;
    }

    public E_SolicitudesSpam solicitudesSpam (List<SolicitudEliminacion> solicitudes) {
        if (solicitudes == null || solicitudes.isEmpty()) {
            var estadisticaCasoNULL = E_SolicitudesSpam.builder()
                    .solicitudesSpam(0)
                    .solicitudesNoSpam(0)
                    .build();
            estadisticaCasoNULL.setFechaDeCalculo(LocalDateTime.now());
            return estadisticaCasoNULL;
        };

        Integer cantSolicitudesSpam = solicitudes.stream()
                .filter(s -> s.getEstado() == EstadoDeSolicitud.SPAM)
                .toList().size();

        Integer cantSolicitudesNoSpam = solicitudes.size() - cantSolicitudesSpam;

        var estadistica = E_SolicitudesSpam.builder()
                .solicitudesSpam(cantSolicitudesSpam)
                .solicitudesNoSpam(cantSolicitudesNoSpam)
                .build();

        estadistica.setFechaDeCalculo(LocalDateTime.now());
        return estadistica;
    }

    public E_MayorCategoria mayorCategoria (List<Hecho> hechos) {
        Map<String, Long> hechosPorCodigoCategoria = hechos.stream()
                .collect(Collectors.groupingBy(h -> h.getCategoria().getCodigoCategoria(), Collectors.counting()));

        if (hechosPorCodigoCategoria.isEmpty()) return null;

        var maxCategoria = hechosPorCodigoCategoria.entrySet().stream().max(Map.Entry.comparingByValue()).get();

        String codigoGanador = maxCategoria.getKey();
        Integer cantHechos = maxCategoria.getValue().intValue();

        Categoria categoriaGanadora = hechos.stream()
                .map(Hecho::getCategoria)
                .filter(c -> c.getCodigoCategoria().equals(codigoGanador))
                .findFirst()
                .orElse(null);

        var estadistica = E_MayorCategoria.builder()
                .categoria(categoriaGanadora.getNombre())
                .codigoCategoria(codigoGanador)
                .cantHechosCategoria(cantHechos)
                .cantHechosTotales(hechos.size())
                .build();

        estadistica.setFechaDeCalculo(LocalDateTime.now());
        return estadistica;
    }

    //Metodos privados

    private Map.Entry<String, Long> conteoMaxProvincia (List<Hecho> hechos) {
        List<Hecho> hechosUtilizables = hechos.stream()
                .filter(h -> h.getUbicacion() != null)
                .filter(h -> h.getUbicacion().getProvincia() != null && !h.getUbicacion().getProvincia().isBlank() && !h.getUbicacion().getProvincia().equals("DESCONOCIDA"))
                .toList();

        Map<String, Long> conteoProvincias = hechosUtilizables.stream()
                .collect(Collectors.groupingBy(h -> h.getUbicacion().getProvincia(), Collectors.counting()));
        //esto me da un map asociando cada provincia a la cantidad de hechos que la tienen

        if (conteoProvincias.isEmpty()) return null;

        return conteoProvincias.entrySet().stream().max(Map.Entry.comparingByValue()).get();
    }
}
