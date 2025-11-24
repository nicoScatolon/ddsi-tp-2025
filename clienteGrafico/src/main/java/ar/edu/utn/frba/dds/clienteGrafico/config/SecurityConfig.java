package ar.edu.utn.frba.dds.clienteGrafico.config;

import ar.edu.utn.frba.dds.clienteGrafico.providers.CustomAuthProvider;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomAuthProvider provider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(provider)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // PRIMERO Y MÁS IMPORTANTE: Páginas de error públicas
                        .requestMatchers("/error/**").permitAll()

                        // Recursos estáticos y login público
                        .requestMatchers("/login", "/signup", "/css/**", "/js/**", "/img/**").permitAll()

                        // Rutas de COLECCIONES que deben ser públicas (solo GET)
                        .requestMatchers(HttpMethod.GET,"/colecciones/create").hasAnyRole("ADMIN", "ADMINSUPERIOR")
                        .requestMatchers(HttpMethod.GET, "/colecciones/**").permitAll()

                        // Rutas Admin
                        .requestMatchers("/hechos/destacar/**").hasAnyRole("ADMIN", "ADMINSUPERIOR")
                        .requestMatchers("/colecciones/**").hasAnyRole("ADMIN", "ADMINSUPERIOR")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "ADMINSUPERIOR")
                        .requestMatchers("/adminsuperior/**").hasRole("ADMINSUPERIOR")
                        .requestMatchers("/hechos/fuenteDinamica/**").hasAnyRole("ADMIN", "ADMINSUPERIOR", "CONTRIBUYENTE")
                        .requestMatchers("/mis-hechos").hasAnyRole("ADMIN", "ADMINSUPERIOR", "CONTRIBUYENTE")
                        .requestMatchers("/mis-solicitudes").hasAnyRole("ADMIN", "ADMINSUPERIOR", "CONTRIBUYENTE")
                        .requestMatchers("/profile").hasAnyRole("ADMIN", "ADMINSUPERIOR", "CONTRIBUYENTE")
                        .requestMatchers("/hechos/create").hasAnyRole("ADMIN", "ADMINSUPERIOR", "CONTRIBUYENTE")

                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        // Success handler inline (sin clase separada)
                        .successHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession(false);
                            String targetUrl = "/index"; // URL por defecto

                            // Intentar obtener la URL guardada en la sesión
                            if (session != null) {
                                String redirectUrl = (String) session.getAttribute("REDIRECT_URL_AFTER_LOGIN");
                                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                                    targetUrl = redirectUrl;
                                    // Limpiar después de usar
                                    session.removeAttribute("REDIRECT_URL_AFTER_LOGIN");
                                }
                            }

                            // Evitar redirecciones a páginas de login o error
                            if (targetUrl.contains("/login") || targetUrl.contains("/error")) {
                                targetUrl = "/index";
                            }

                            response.sendRedirect(targetUrl);
                        })
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        // Usuario no autenticado → redirigir a login guardando la URL
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Guardar la URL original en la sesión
                            String targetUrl = request.getRequestURI();

                            // Si tiene query params, incluirlos también
                            String queryString = request.getQueryString();
                            if (queryString != null && !queryString.isEmpty()) {
                                targetUrl += "?" + queryString;
                            }

                            request.getSession().setAttribute("REDIRECT_URL_AFTER_LOGIN", targetUrl);
                            response.sendRedirect("/login?unauthorized");
                        })
                        // Usuario autenticado pero sin permisos → redirigir a página de error
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/error/403")
                        )
                );

        return http.build();
    }
}