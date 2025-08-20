package ar.edu.utn.frba.dds.domain.dtos.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaInputDTO {

    private String id;
    private String nombre;


    public CategoriaInputDTO(String nombre) {
        this.nombre = nombre;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CategoriaInputDTO fromString(String value) {
        return new CategoriaInputDTO(value);
    }
}