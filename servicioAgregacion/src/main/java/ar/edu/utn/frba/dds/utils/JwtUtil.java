package ar.edu.utn.frba.dds.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String extraerRol(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);
    }

    @SuppressWarnings("unchecked")
    public static List<String> extraerPermisos(String token) {
        Object value = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("perms");
        if (value instanceof List<?>) {
            List<?> raw = (List<?>) value;
            List<String> out = new ArrayList<>();
            for (Object o : raw) {
                if (o != null) out.add(String.valueOf(o));
            }
            return out;
        }
        return java.util.Collections.emptyList();
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
