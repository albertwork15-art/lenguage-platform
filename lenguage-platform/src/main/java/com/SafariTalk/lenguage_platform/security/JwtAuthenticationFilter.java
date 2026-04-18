package com.SafariTalk.lenguage_platform.security;

import com.SafariTalk.lenguage_platform.model.UserAccountEntity;
import com.SafariTalk.lenguage_platform.repository.UserAccountRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Este soy yo literalmente, el "guardia de seguridad" que intercepta cada una de las peticiones
 * (clicks o llamados a mi backend) para ver si traes un Token JWT válido en el encabezado.
 * Como heredo de "OncePerRequestFilter", te garantizo que solo revisaré tu mochila una vez por visita.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 1. A ver, reviso la mochila (buscando el header "Authorization")
        String header = request.getHeader("Authorization");
        
        // Si no traes mochila, o lo que traes dentro no empieza con "Bearer " (así inician los JWTs),
        // te dejo pasar así nomás sin identificarte. Ya mi capa de seguridad luego te rebotará si esta
        // página requería cuenta obligatoria.
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraigo el texto "crudo" del token quitándole el texto "Bearer " y espacios extra.
        String raw = header.substring(7).trim();
        if (raw.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 3. Verifico la validez del token con mi servicio, para saber quién eres (saco el ID).
            Long userId = jwtService.extractUserId(raw);
            
            // 4. Voy a la base de datos a buscar tu perfil, porque, aunque el token sea válido,
            // ¡tu cuenta pudo haber sido eliminada o desactivada hace 5 segundos!
            UserAccountEntity user =
                    userAccountRepository.findById(userId).orElse(null);
            if (user == null || !user.isEnabled()) {
                unauthorized(response);
                return;
            }
            
            // 5. ¡Estás limpio! Construyo tu "Gafete Oficial VIP" (UsernamePasswordAuthenticationToken)
            // Y se lo presto al jefe de seguridad de Spring (SecurityContextHolder).
            AuthenticatedUser principal = AuthenticatedUser.from(user);
            var auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            
        } catch (JwtException | IllegalArgumentException ex) {
            // Si el token estaba hackeado, falso o venció en el tiempo, 
            // cacho el error para que mi aplicación no explote, sino que te corra amablemente.
            unauthorized(response);
            return;
        }

        // 6. Adelante pasa a mi bar (sigue con la petición al controlador que querías invocar).
        filterChain.doFilter(request, response);
    }

    /**
     * Esta función pequeña me sirve para echar a alguien si su token apesta. 
     * Armo un error limpio en formato JSON amigable (401 Unauthorized) en vez 
     * de mostrar un error raro de Java nativo.
     */
    private static void unauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Invalid or expired token\"}");
    }
}
