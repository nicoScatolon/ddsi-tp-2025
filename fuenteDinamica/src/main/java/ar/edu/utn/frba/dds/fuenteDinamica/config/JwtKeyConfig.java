package ar.edu.utn.frba.dds.fuenteDinamica.config;

import ar.edu.utn.frba.dds.fuenteDinamica.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;


@Configuration
public class JwtKeyConfig {
    @Value("${jwt.secret.base64}")
    private String secret;

    @PostConstruct
    public void init() {
        System.out.println("🔑 Iniciando JWT Key en fuenteDinamica...");
        JwtUtil.initFromBase64(secret);
    }
}
