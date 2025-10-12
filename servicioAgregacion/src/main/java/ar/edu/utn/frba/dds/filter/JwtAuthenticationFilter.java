package ar.edu.utn.frba.dds.filter;

import ar.edu.utn.frba.dds.utils.JwtUtil; // el MISMO util que ya usás (o uno igual en este módulo)
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Solo procesa si viene Bearer; si no hay token, deja pasar (para endpoints públicos)
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                // email como principal
                String email = JwtUtil.validarToken(token); // sub = email


                String rol = JwtUtil.extraerRol(token);
                List<String> permisos = JwtUtil.extraerPermisos(token);

                List<GrantedAuthority> authorities = new ArrayList<>();
                if (rol != null && !rol.isBlank()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + rol));
                }
                if (permisos != null) {
                    for (String p : permisos) {
                        if (p != null && !p.isBlank()) {
                            authorities.add(new SimpleGrantedAuthority(p));
                        }
                    }
                }

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // Evitá parsear JWT en las rutas que ya son públicas
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        return p.startsWith("/v3/api-docs")
                || p.startsWith("/swagger-ui")
                || p.equals("/error")
                || p.equals("/api/colecciones/publica")
                || p.startsWith("/api/colecciones/publica/")
                || p.equals("/api/hechos/publica")
                || p.startsWith("/api/hechos/publica/")
                || p.equals("/api/privada/categorias")
                || p.equals("/api/privada/categorias/short")
                || p.equals("/api/solicitudes-eliminacion/publica")
                || p.startsWith("/api/fuente/test/");
    }
}
