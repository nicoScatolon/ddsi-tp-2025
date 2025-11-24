package ar.edu.utn.frba.dds.fuenteproxy.filters;
import ar.edu.utn.frba.dds.fuenteproxy.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            jakarta.servlet.FilterChain chain
    ) throws jakarta.servlet.ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        logger.debug("[JWT FILTER] Entró request a {} {}", method, uri);

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            logger.debug("[JWT FILTER] Token presente en header Authorization");

            try {
                String email = JwtUtil.validarToken(token);
                logger.debug("[JWT FILTER] Token válido. Subject/email extraído: {}", email);

                List<org.springframework.security.core.GrantedAuthority> auths = new ArrayList<>();

                String rol = JwtUtil.extraerRol(token);
                logger.debug("[JWT FILTER] Rol extraído del token: {}", rol);
                if (rol != null && !rol.isBlank()) {
                    auths.add(new SimpleGrantedAuthority("ROLE_" + rol));
                }

                for (String p : JwtUtil.extraerPermisos(token)) {
                    logger.debug("[JWT FILTER] Permiso extraído: {}", p);
                    auths.add(new SimpleGrantedAuthority(p));
                }

                var auth = new UsernamePasswordAuthenticationToken(email, null, auths);
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.info("[JWT FILTER] Authentication seteada en SecurityContext. Authorities: {}", auths);

            } catch (Exception e) {
                logger.error("[JWT FILTER] Error al validar token o armar Authentication", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        } else {
            logger.debug("[JWT FILTER] No se encontró header Authorization Bearer, se continúa sin Authentication");
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(jakarta.servlet.http.HttpServletRequest req) {
        String p = req.getRequestURI();
        String m = req.getMethod();

        if (p.startsWith("/v3/api-docs") || p.equals("/swagger-ui.html") || p.startsWith("/swagger-ui/")) return true;

        if (p.equals("/api/fuenteProxy/colecciones")) return true;
        if (p.startsWith("/api/fuenteProxy/colecciones/")) return true;
        if (p.equals("/api/fuenteProxy/hechos")) return true;
        if (p.startsWith("/api/fuenteProxy/hechos/")) return true;
        return p.equals("/api/fuenteProxy/solicitudes/solicitud") && "POST".equalsIgnoreCase(m);
    }
}
