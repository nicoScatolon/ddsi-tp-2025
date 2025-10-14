package ar.edu.utn.frba.dds.fuenteDinamica.config;

import ar.edu.utn.frba.dds.fuenteDinamica.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,  "/api/fuenteDinamica/hechos").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/fuenteDinamica/hechos/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/fuenteDinamica/hechos").permitAll()

                        .requestMatchers(HttpMethod.PUT,   "/api/fuenteDinamica/hechos/*").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/api/fuenteDinamica/hechos/user/*").authenticated()
                        .requestMatchers(HttpMethod.POST,  "/api/fuenteDinamica/hechos/admin/*").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
