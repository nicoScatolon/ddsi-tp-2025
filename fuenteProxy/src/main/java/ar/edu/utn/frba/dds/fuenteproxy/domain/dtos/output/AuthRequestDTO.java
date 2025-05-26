package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}
