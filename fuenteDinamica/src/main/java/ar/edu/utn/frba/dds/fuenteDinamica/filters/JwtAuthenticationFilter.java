package ar.edu.utn.frba.dds.fuenteDinamica.filters;


import ar.edu.utn.frba.dds.fuenteDinamica.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            jakarta.servlet.FilterChain chain
    ) throws jakarta.servlet.ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String email = JwtUtil.validarToken(token);
                Long id = JwtUtil.extraerId(token);

                List<org.springframework.security.core.GrantedAuthority> auths = new ArrayList<>();
                String rol = JwtUtil.extraerRol(token);
                if (rol != null && !rol.isBlank()) {
                    auths.add(new SimpleGrantedAuthority("ROLE_" + rol));
                }
                for (String p : JwtUtil.extraerPermisos(token)) {
                    auths.add(new SimpleGrantedAuthority(p));
                }

                var auth = new UsernamePasswordAuthenticationToken(email, null, auths);
                auth.setDetails(id); //le guardo el id

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(jakarta.servlet.http.HttpServletRequest req) {
        String p = req.getRequestURI();
        String m = req.getMethod();

        if (p.startsWith("/v3/api-docs") || p.equals("/swagger-ui.html") || p.startsWith("/swagger-ui/")) return true;

        if (p.equals("/api/fuenteDinamica/hechos") && "GET".equalsIgnoreCase(m)) return true;
        if (p.matches("^/api/fuenteDinamica/hechos/\\d+$") && "GET".equalsIgnoreCase(m)) return true;
        return p.equals("/api/fuenteDinamica/hechos") && "POST".equalsIgnoreCase(m);
    }
}