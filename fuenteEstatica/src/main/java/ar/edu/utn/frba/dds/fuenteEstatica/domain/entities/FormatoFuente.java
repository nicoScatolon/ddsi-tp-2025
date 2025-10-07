package ar.edu.utn.frba.dds.fuenteEstatica.domain.entities;

public enum FormatoFuente {
    CSV, XLSX, JSON;

    public static FormatoFuente fromPath(String path) {
        String ext = org.apache.commons.io.FilenameUtils.getExtension(path).toLowerCase();
        return switch (ext) {
            case "csv" -> CSV;
            case "xlsx", "xls" -> XLSX;
            case "json" -> JSON;
            default -> throw new IllegalArgumentException("Formato no soportado: ." + ext);
        };
    }

    public String estrategiaKey() {
        return name().toLowerCase(); // "csv", "xlsx", "json"
    }
}