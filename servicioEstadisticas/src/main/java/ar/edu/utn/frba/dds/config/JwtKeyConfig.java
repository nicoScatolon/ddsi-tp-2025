package ar.edu.utn.frba.dds.config;


import ar.edu.utn.frba.dds.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class JwtKeyConfig {

    @Value("${jwt.secret.base64}")
    private String base64Secret;

    @PostConstruct
    public void init() {
        JwtUtil.initFromBase64(base64Secret);
    }
}