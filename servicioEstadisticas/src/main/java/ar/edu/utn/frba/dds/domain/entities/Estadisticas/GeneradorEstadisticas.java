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

    public E_MayorProvinciaPorCategoria mayorProvinciaPorCategoria (Categoria categoria, List<Hecho> hechos) {
        var maxProvincia = this.conteoMaxProvincia(hechos);

        if (maxProvincia == null) { return null;}

        var estadistica = E_MayorProvinciaPorCategoria.builder()
                .categoria(categoria)
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

    public E_HoraOcurrenciaPorCategoria horaDiaPorCategoria (Categoria categoria, List<Hecho> hechos) {
        Map<Integer, Long> conteoHoras = hechos.stream()
                .collect(Collectors.groupingBy(h -> h.getFechaDeOcurrencia().getHour(), Collectors.counting()));

        if (conteoHoras.isEmpty()) return null;

        var maxHora = conteoHoras.entrySet().stream().max(Map.Entry.comparingByValue()).get();

        var estadistica = E_HoraOcurrenciaPorCategoria.builder()
                .categoria(categoria)
                .horaDia(maxHora.getKey())
                .cantHechosHora(maxHora.getValue().intValue())
                .cantHechosTotales(hechos.size())
                .build();

        estadistica.setFechaDeCalculo(LocalDateTime.now());
        return estadistica;
    }

    public E_SolicitudesSpam solicitudesSpam (List<SolicitudEliminacion> solicitudes) {
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
        Map<Categoria, Long> hechosPorCategoria = hechos.stream()
                .collect(Collectors.groupingBy(Hecho::getCategoria, Collectors.counting()));

        if (hechosPorCategoria.isEmpty()) return null;

        var maxCategoria = hechosPorCategoria.entrySet().stream().max(Map.Entry.comparingByValue()).get();

        var estadistica = E_MayorCategoria.builder()
                .categoria(maxCategoria.getKey())
                .cantHechosCategoria(maxCategoria.getValue().intValue())
                .cantHechosTotales(hechos.size())
                .build();

        estadistica.setFechaDeCalculo(LocalDateTime.now());
        return estadistica;
    }

    public E_MayorCategoria mayorCategoria (Map<Categoria, List<Hecho>> hechosPorCategoria) {
        var maxCategoria = hechosPorCategoria.entrySet().stream().max(Comparator.comparingInt(e -> e.getValue().size())).get();

        Integer cantidadTotalHechos = hechosPorCategoria.values().stream()
                .mapToInt(List::size)
                .sum();

        var estadistica = E_MayorCategoria.builder()
                .categoria(maxCategoria.getKey())
                .cantHechosCategoria(maxCategoria.getValue().size())
                .cantHechosTotales(cantidadTotalHechos)
                .build();

        estadistica.setFechaDeCalculo(LocalDateTime.now());
        return estadistica;
    }

    //Metodos privados

    private Map.Entry<String, Long> conteoMaxProvincia (List<Hecho> hechos) {
        Map<String, Long> conteoProvincias = hechos.stream()
                .collect(Collectors.groupingBy(h -> h.getUbicacion().getProvincia(), Collectors.counting()));
        //esto me da un map asociando cada provincia a la cantidad de hechos que la tienen

        if (conteoProvincias.isEmpty()) return null;

        return conteoProvincias.entrySet().stream().max(Map.Entry.comparingByValue()).get();
    }
}
