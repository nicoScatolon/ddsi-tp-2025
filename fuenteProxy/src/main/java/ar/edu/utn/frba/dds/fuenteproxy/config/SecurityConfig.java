package ar.edu.utn.frba.dds.fuenteproxy.config;

import ar.edu.utn.frba.dds.fuenteproxy.filters.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        logger.debug("[SECURITY CONFIG] Creando bean JwtAuthenticationFilter");
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain api(HttpSecurity http) throws Exception {
        logger.debug("[SECURITY CONFIG] Inicializando SecurityFilterChain para fuenteProxy");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> {
                    logger.debug("[SECURITY CONFIG] Configurando SessionCreationPolicy.STATELESS");
                    s.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(auth -> {
                    logger.debug("[SECURITY CONFIG] Configurando reglas de autorización");

                    auth.requestMatchers(
                            "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**",
                            "/api/fuenteProxy/colecciones",
                            "/api/fuenteProxy/colecciones/**",
                            "/api/fuenteProxy/hechos",
                            "/api/fuenteProxy/hechos/**",
                            "/api/fuenteProxy/solicitudes/solicitud"
                    ).permitAll();

                    logger.debug("[SECURITY CONFIG] Todo el resto de requests requiere autenticación");
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        logger.debug("[SECURITY CONFIG] SecurityFilterChain armado correctamente");
        return http.build();
    }
}