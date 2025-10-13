package ar.edu.utn.frba.dds.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class JwtUtil {
    private static Key key; // la setea JwtKeyConfig al arrancar

    // Llamalo una vez al inicio (desde una @Configuration)
    public static void initFromBase64(String base64Secret) {
        if (base64Secret == null || base64Secret.isBlank())
            throw new IllegalStateException("Falta jwt.secret.base64 en properties");
        byte[] bytes = Base64.getDecoder().decode(base64Secret);
        // cualquiera de las dos sirve; dejo la “oficial” de JJWT:
        // key = Keys.hmacShaKeyFor(bytes);
        key = new SecretKeySpec(bytes, "HmacSHA256");
    }

    private static void ensureKey() {
        if (key == null) throw new IllegalStateException("JWT key no inicializada");
    }

    public static String extraerRol(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);
    }

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
