package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.dtos.output.estadisticas.*;
import java.io.StringWriter;
import java.util.List;

public class ExportadorCSV {

    // E_HoraOcurrenciaPorCategoria -- //

    public static String exportHoraOcurrenciaPorCategoria(List<E_HoraOcuPorCategoriaOutputDTO> lista) {
        StringWriter writer = new StringWriter();
        writer.append("ID,Categoria,HoraDia,CantHechosHora,CantHechosTotales,FechaDeCalculo\n");

        for (E_HoraOcuPorCategoriaOutputDTO e : lista) {
            writer.append(e.getId().toString()).append(",");
            writer.append(e.getCategoria()).append(",");
            writer.append(e.getCodigoCategoria()).append(",");
            writer.append(e.getHoraDia().toString()).append(",");
            writer.append(e.getCantHechosHora().toString()).append(",");
            writer.append(e.getCantHechosTotales().toString()).append(",");
            writer.append(e.getFechaDeCalculo().toString()).append("\n");
        }

        return writer.toString();
    }

    // E_MayorCategoria -- //

    public static String exportMayorCategoria(List<E_MayorCategoriaOutputDTO> lista) {
        StringWriter writer = new StringWriter();
        writer.append("ID,Categoria,CantHechosCategoria,CantHechosTotales,FechaDeCalculo\n");

        for (E_MayorCategoriaOutputDTO e : lista) {
            writer.append(e.getId().toString()).append(",");
            writer.append(e.getCategoria()).append(",");
            writer.append(e.getCodigoCategoria()).append(",");
            writer.append(e.getCantHechosCategoria().toString()).append(",");
            writer.append(e.getCantHechosTotales().toString()).append(",");
            writer.append(e.getFechaDeCalculo().toString()).append("\n");
        }

        return writer.toString();
    }

    // -- E_MayorProvinciaPorCategoria -- //

    public static String exportMayorProvinciaPorCategoria(List<E_MayorProvPorCategoriaOutputDTO> lista) {
        StringWriter writer = new StringWriter();
        writer.append("ID,Categoria,Provincia,CantHechosProvincia,CantHechosTotales,FechaDeCalculo\n");

        for (E_MayorProvPorCategoriaOutputDTO e : lista) {
            writer.append(e.getId().toString()).append(",");
            writer.append(e.getCategoria()).append(",");
            writer.append(e.getCodigoCategoria()).append(",");
            writer.append(e.getProvincia()).append(",");
            writer.append(e.getCantHechosProvincia().toString()).append(",");
            writer.append(e.getCantHechosTotales().toString()).append(",");
            writer.append(e.getFechaDeCalculo().toString()).append("\n");
        }

        return writer.toString();
    }

    // -- E_MayorProvinciaPorColeccion -- //

    public static String exportMayorProvinciaPorColeccion(List<E_MayorProvPorColeccionOutputDTO> lista) {
        StringWriter writer = new StringWriter();
        writer.append("ID,ColeccionHandle,ColeccionTitulo,Provincia,CantHechosProvincia,CantHechosTotales,FechaDeCalculo\n");

        for (E_MayorProvPorColeccionOutputDTO e : lista) {
            writer.append(e.getId().toString()).append(",");
            writer.append(e.getColeccionDTO().getHandle()).append(",");
            writer.append(e.getColeccionDTO().getTitulo()).append(",");
            writer.append(e.getProvincia()).append(",");
            writer.append(e.getCantHechosProvincia().toString()).append(",");
            writer.append(e.getCantHechosTotales().toString()).append(",");
            writer.append(e.getFechaDeCalculo().toString()).append("\n");
        }

        return writer.toString();
    }

    // -- E_SolicitudesSpam -- //

    public static String exportSolicitudesSpam(List<E_SolicitudesSpamOutputDTO> lista) {
        StringWriter writer = new StringWriter();
        writer.append("ID,CantSolicitudesSpam,CantSolicitudesNoSpam,FechaDeCalculo\n");

        for (E_SolicitudesSpamOutputDTO e : lista) {
            writer.append(e.getId().toString()).append(",");
            writer.append(e.getSolicitudesSpam().toString()).append(",");
            writer.append(e.getSolicitudesNoSpam().toString()).append(",");
            writer.append(e.getFechaDeCalculo().toString()).append("\n");
        }

        return writer.toString();
    }
}
