package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.filter.JwtAuthenticationFilter;
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

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain api(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // doc y errores
                        .requestMatchers("/error", "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // PÚBLICOS según tus controllers
                        .requestMatchers(
                                "/api/colecciones/publica",
                                "/api/colecciones/publica/**",
                                "/api/hechos/publica",
                                "/api/hechos/publica/**",
                                "/api/privada/categorias",
                                "/api/privada/categorias/short",
                                "/api/solicitudes-eliminacion/publica",   // POST crear solicitud
                                "/api/fuente/test/**"
                        ).permitAll()

                        // resto protegido
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
