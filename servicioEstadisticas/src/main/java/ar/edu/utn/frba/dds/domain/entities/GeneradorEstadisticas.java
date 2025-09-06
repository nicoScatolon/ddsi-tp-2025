package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.Estadisticas.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class GeneradorEstadisticas {
    private static final GeneradorEstadisticas INSTANCE = new GeneradorEstadisticas();

    public E_MayorProvinciaPorCategoria mayorProvinciaPorCategoria (Categoria categoria, List<Hecho> hechos) {
        var maxProvincia = this.conteoMaxProvincia(hechos);

        if (maxProvincia == null) { return null;}

        return E_MayorProvinciaPorCategoria.builder()
                .categoria(categoria)
                .provincia(maxProvincia.getKey())
                .cantHechosProvincia(maxProvincia.getValue().intValue())
                .cantHechosTotales(hechos.size())
                .fechaDeCalculo(LocalDateTime.now())
                .build();
    }

    public E_MayorProvinciaPorColeccion mayorProvinciaPorColeccion (Coleccion coleccion, List<Hecho> hechos) {
        var maxProvincia = this.conteoMaxProvincia(hechos);

        if (maxProvincia == null) { return null;}

        return E_MayorProvinciaPorColeccion.builder()
                .coleccion(coleccion)
                .provincia(maxProvincia.getKey())
                .cantHechosProvincia(maxProvincia.getValue().intValue())
                .cantHechosTotales(hechos.size())
                .fechaDeCalculo(LocalDateTime.now())
                .build();
    }

    public E_HoraOcurrenciaPorCategoria horaDiaPorCategoria (Categoria categoria, List<Hecho> hechos) {
        Map<Integer, Long> conteoHoras = hechos.stream()
                .collect(Collectors.groupingBy(h -> h.getFechaDeOcurrencia().getHour(), Collectors.counting()));

        if (conteoHoras.isEmpty()) return null;

        var maxHora = conteoHoras.entrySet().stream().max(Map.Entry.comparingByValue()).get();

        return E_HoraOcurrenciaPorCategoria.builder()
                .categoria(categoria)
                .horaDia(maxHora.getKey())
                .cantHechosHora(maxHora.getValue().intValue())
                .cantHechosTotales(hechos.size())
                .fechaDeCalculo(LocalDateTime.now())
                .build();
    }

    public E_SolicitudesSpam solicitudesSpam (List<SolicitudEliminacion> solicitudes) {
        Integer cantSolicitudesSpam = solicitudes.stream()
                .filter(s -> s.getEstado() == EstadoDeSolicitud.SPAM)
                .toList().size();

        Integer cantSolicitudesNoSpam = solicitudes.size() - cantSolicitudesSpam;

        return E_SolicitudesSpam.builder()
                .solicitudesSpam(cantSolicitudesSpam)
                .solicitudesNoSpam(cantSolicitudesNoSpam)
                .fechaDeCalculo(LocalDateTime.now())
                .build();
    }

    public E_MayorCategoria mayorCategoria (List<Hecho> hechos) {
        Map<Categoria, Long> hechosPorCategoria = hechos.stream()
                .collect(Collectors.groupingBy(Hecho::getCategoria, Collectors.counting()));

        if (hechosPorCategoria.isEmpty()) return null;

        var maxCategoria = hechosPorCategoria.entrySet().stream().max(Map.Entry.comparingByValue()).get();

        return E_MayorCategoria.builder()
                .categoria(maxCategoria.getKey())
                .cantHechosCategoria(maxCategoria.getValue().intValue())
                .cantHechosTotales(hechos.size())
                .fechaDeCalculo(LocalDateTime.now())
                .build();
    }

    public E_MayorCategoria mayorCategoria (Map<Categoria, List<Hecho>> hechosPorCategoria) {
        var maxCategoria = hechosPorCategoria.entrySet().stream().max(Comparator.comparingInt(e -> e.getValue().size())).get();

        Integer cantidadTotalHechos = hechosPorCategoria.values().stream()
                .mapToInt(List::size)
                .sum();

        return E_MayorCategoria.builder()
                .categoria(maxCategoria.getKey())
                .cantHechosCategoria(maxCategoria.getValue().size())
                .cantHechosTotales(cantidadTotalHechos)
                .fechaDeCalculo(LocalDateTime.now())
                .build();
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
