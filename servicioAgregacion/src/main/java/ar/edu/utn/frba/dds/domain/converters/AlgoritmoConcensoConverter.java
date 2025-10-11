package ar.edu.utn.frba.dds.domain.converters;

import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.ConsensoAbsoluto;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.ConsensoMayoriaSimple;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.ConsensoMultipleMencion;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.IAlgoritmoConsenso;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AlgoritmoConcensoConverter implements AttributeConverter<IAlgoritmoConsenso, String> {

    @Override
    public String convertToDatabaseColumn(IAlgoritmoConsenso iAlgoritmoConsenso) {
        if (iAlgoritmoConsenso == null) {
            return null; }
        if (iAlgoritmoConsenso.getClass().equals(ConsensoAbsoluto.class)){
            return "ABSOLUTO";
        } else if (iAlgoritmoConsenso.getClass().equals(ConsensoMayoriaSimple.class)){
            return "MAYORIASIMPLE";
        } else if (iAlgoritmoConsenso.getClass().equals(ConsensoMultipleMencion.class)){
            return "MULTIPLEMENCION";
        } else return null;
    }

    @Override
    public IAlgoritmoConsenso convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return switch (s) {
            case "ABSOLUTO" -> new ConsensoAbsoluto();
            case "MAYORIASIMPLE" -> new ConsensoMayoriaSimple();
            case "MULTIPLEMENCION" -> new ConsensoMultipleMencion();
            default -> null;
        };
    }
}
