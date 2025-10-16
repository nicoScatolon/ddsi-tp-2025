package ar.edu.utn.frba.dds.fuenteEstatica.utils;


import io.jsonwebtoken.Jwts;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;


public class JwtUtil {

    private static Key key;

    public static void initFromBase64(String base64Secret) {
        if (base64Secret == null || base64Secret.isBlank())
            throw new IllegalStateException("Falta jwt.secret.base64 en properties");
        byte[] bytes = Base64.getDecoder().decode(base64Secret);
        key = new SecretKeySpec(bytes, "HmacSHA256");
    }

    private static void ensureKey() {
        if (key == null) throw new IllegalStateException("JWT key no inicializada");
    }

    public static String extraerRol(String token) {
        ensureKey();
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);
    }

    public static List<String> extraerPermisos(String token) {
        ensureKey();
        Object value = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("perms");
        if (value instanceof List<?> raw) {
            List<String> out = new ArrayList<>();
            for (Object o : raw) {
                if (o != null) out.add(String.valueOf(o));
            }
            return out;
        }
        return Collections.emptyList();
    }

    public static String validarToken(String token) {
        ensureKey();
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
