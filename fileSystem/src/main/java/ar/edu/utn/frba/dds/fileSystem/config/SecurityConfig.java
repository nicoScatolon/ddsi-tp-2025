package ar.edu.utn.frba.dds.fileSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Permitir POST para subir archivos
                        .requestMatchers(HttpMethod.POST, "/api/file-system/csv", "/api/file-system/multimedia").permitAll()
                        // Permitir GET para descargar/ver archivos - ESTO ES LO QUE FALTABA
                        .requestMatchers(HttpMethod.GET, "/api/file-system/multimedia/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}