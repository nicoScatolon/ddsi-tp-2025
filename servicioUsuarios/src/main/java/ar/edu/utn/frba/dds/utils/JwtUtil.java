package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.models.entities.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {
    @Getter
    private static Key key = null;

    public static void initFromBase64(String base64Secret) {
        byte[] bytes = Base64.getDecoder().decode(base64Secret);
        key = new SecretKeySpec(bytes, "HmacSHA256");
    }

    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 días

    public static String generarAccessToken(Usuario u) {
        return Jwts.builder()
                .setSubject(u.getEmail())
                .setIssuer("gestion-usuarios")
                .claim("username", u.getUsername())
                .claim("id", u.getId())
                .claim("rol", u.getRol().name())
                .claim("perms", u.getPermisos().stream().map(Enum::name).toList())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    public static String generarRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
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
