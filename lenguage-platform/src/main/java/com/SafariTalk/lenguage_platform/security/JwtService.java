package com.SafariTalk.lenguage_platform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Mi Servicio encargado de sellar y abrir las credenciales virtuales (Tokens).
 * Imagíname como el notario de la app; firmo el pase V.I.P para que navegues con confianza
 * sin tener que estar iniciando sesión a cada rato.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private SecretKey signingKey; // Mi firma secreta (la huella dactilar top-secret)

    /**
     * @PostConstruct se encarga de decirle a Spring: "Apenas prendas la app y cargues todo,
     * ejecuta este bloque". Aquí agarro tu secreto crudo que metiste en mis variables de entorno, 
     * lo vuelvo en "bytes" (10101) y armo matemáticamente una llave H-MAC criptográfica súper fuerte.
     */
    @PostConstruct
    void initKey() {
        byte[] bytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        signingKey = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Cuando alguien pone bien su password en el login, llamo a esta función para generarle
     * su token. Básicamente escribo su ID y Rol en un papelito en blanco (JWT),
     * poniéndole límite de tiempo (expiración) y luego firmándolo para que sea auténtico (signWith).
     */
    public String generateToken(AuthenticatedUser user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtProperties.getExpirationMs()); // Sumo el tiempo de vida (Ej 24hrs)
        
        return Jwts.builder()
                .subject(String.valueOf(user.getId())) // Este es mi sujeto primordial (ID)
                .claim("email", user.getEmail()) // Empaco data extra en mis "claims" para leerlo en el front fácil
                .claim("role", user.getRole().name())
                .issuedAt(now) // La fecha/hora en que lo sellé
                .expiration(exp) // La fecha en la que me autodestruyo y te pido reloguear
                .signWith(signingKey) // Firma de oro a prueba de balas
                .compact(); // Concateno todo y lo empaqueto en mi clásico chorizote de texto de 3 partes separadas por puntos (xx.yy.zz)
    }

    /**
     * "Claims" es mi palabrería técnica del JWT para "los datos empaquetados ahí adentro".
     * En esta función rompo el sello (verifico la firma), compruebo si el tiempo todavía marca correcto, 
     * y extraigo los datos puros.
     */
    public Claims parseClaims(String token) throws JwtException {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Mi atajo utilitario rápido para sacar solo el "subject" que, como lo codifiqué arriba,
     * siempre será equivalente al número de ID del usuario en mi base de datos PostgreSQL.
     */
    public Long extractUserId(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }
}
