package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtKeyConfig {

    @Value("${jwt.secret.base64}")
    private String secret;

    @PostConstruct
    public void init() {
        JwtUtil.initFromBase64(secret);
    }
}

