package ar.edu.utn.frba.dds.filters;

import ar.edu.utn.frba.dds.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            jakarta.servlet.FilterChain chain
    ) throws jakarta.servlet.ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();
        log.info("[ESTADISTICAS][FILTER] Entrando a {} {}", method, uri);

        String header = request.getHeader("Authorization");
        log.info("[ESTADISTICAS][FILTER] Authorization header = {}", header);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String email = JwtUtil.validarToken(token);
                List<org.springframework.security.core.GrantedAuthority> auths = new ArrayList<>();

                String rol = JwtUtil.extraerRol(token);
                if (rol != null && !rol.isBlank()) {
                    if (!rol.startsWith("ROLE_")) {
                        rol = "ROLE_" + rol;
                    }
                    auths.add(new SimpleGrantedAuthority(rol));
                }

                for (String p : JwtUtil.extraerPermisos(token)) {
                    auths.add(new SimpleGrantedAuthority(p));
                }

                var auth = new UsernamePasswordAuthenticationToken(email, null, auths);
                SecurityContextHolder.getContext().setAuthentication(auth);

                log.info("[ESTADISTICAS][FILTER] email={}, roles={}", email, auths);

            } catch (Exception e) {
                log.warn("[ESTADISTICAS][FILTER] Token inválido: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        } else {
            log.info("[ESTADISTICAS][FILTER] Sin Authorization, sigue sin auth");
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(jakarta.servlet.http.HttpServletRequest req) {
        String p = req.getRequestURI();
        String m = req.getMethod();

        boolean skip =
                p.startsWith("/v3/api-docs")
                        || p.equals("/swagger-ui.html")
                        || p.startsWith("/swagger-ui/")
                        || p.equals("/api/estadisticas/test");

        log.info("[ESTADISTICAS][FILTER] shouldNotFilter? {} {} -> {}", m, p, skip);
        return skip;
    }
}
