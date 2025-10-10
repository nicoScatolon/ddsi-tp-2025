package ar.edu.utn.frrba.dds.servicioUsuarios.utils;

import ar.edu.utn.frrba.dds.servicioUsuarios.models.entities.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import java.security.Key;
import java.util.Date;
import java.util.List;

public class JwtUtil {
    @Getter
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 días

    public static String generarAccessToken(Usuario u) {
        List<String> permisos = u.getPermisos() == null
                ? java.util.List.of()
                : u.getPermisos().stream().map(Enum::name).toList();

        return Jwts.builder()
                .setSubject(String.valueOf(u))
                .setIssuer("gestion-usuarios")
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .claim("type", "access")
                .claim("uid", u.getId())
                .claim("rol", u.getRol() == null ? null : u.getRol().name())
                .claim("perms", permisos)
                .signWith(key)
                .compact();
    }

    public static String generarRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("gestion-usuarios")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .claim("type", "refresh")
                .signWith(key)
                .compact();
    }

    public static String validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
