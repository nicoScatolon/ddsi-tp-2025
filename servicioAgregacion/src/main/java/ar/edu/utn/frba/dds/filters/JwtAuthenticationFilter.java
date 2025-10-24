package ar.edu.utn.frba.dds.filters;

import ar.edu.utn.frba.dds.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends org.springframework.web.filter.OncePerRequestFilter {
    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                    jakarta.servlet.http.HttpServletResponse response,
                                    jakarta.servlet.FilterChain chain)
            throws jakarta.servlet.ServletException, java.io.IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String email = JwtUtil.validarToken(token); // sub=email
                java.util.List<org.springframework.security.core.GrantedAuthority> auths = new java.util.ArrayList<>();

                String rol = JwtUtil.extraerRol(token);
                if (rol != null && !rol.isBlank()) {
                    auths.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + rol));
                }
                for (String p : JwtUtil.extraerPermisos(token)) {
                    auths.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(p));
                }

                org.springframework.security.authentication.UsernamePasswordAuthenticationToken auth =
                        new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(email, null, auths);

                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);

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
        return p.startsWith("/v3/api-docs")
                || p.equals("/api/colecciones/publica")
                || p.startsWith("/api/colecciones/publica/")
                || p.equals("/api/hechos/publica")
                || p.startsWith("/api/hechos/publica/")
                || p.startsWith("/api/privada/categorias")
                || p.equals("/api/privada/categorias/")
                || p.equals("/api/privada/categorias/short")
                || p.equals("/api/solicitudes-eliminacion/publica")
                || p.startsWith("/api/fuente/test")
                || p.startsWith("/api/colecciones/test");
    }
}
