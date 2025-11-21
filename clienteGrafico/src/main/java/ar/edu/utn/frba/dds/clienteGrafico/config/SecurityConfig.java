package ar.edu.utn.frba.dds.clienteGrafico.config;

import ar.edu.utn.frba.dds.clienteGrafico.providers.CustomAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

                        // Rutas Publicas
//                        .requestMatchers("/", "/index","/legales","/about").permitAll()
//                        .requestMatchers("/hechos", "/hechos/create", "/hechos/{id}", "/hechos/map", "/hechos/fuenteDinamica/**") .permitAll()
                        .requestMatchers(HttpMethod.GET, "/colecciones", "/colecciones/{handle}").permitAll()
//                        .requestMatchers("/solicitudesEliminacion").permitAll()

                        // Rutas Admin
                        .requestMatchers("/hechos/destacar/**").hasAnyRole("ADMIN", "ADMINSUPERIOR")
                        .requestMatchers("/colecciones/**").hasAnyRole("ADMIN", "ADMINSUPERIOR")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "ADMINSUPERIOR")
                        // Lo demás requiere autenticación

                        .requestMatchers(HttpMethod.GET, "/**").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/index", true)
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        // Usuario no autenticado → redirigir a login
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/login?unauthorized")
                        )
                        // Usuario autenticado pero sin permisos → redirigir a página de error
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/error/403")
                        )
                );

        return http.build();
    }
}