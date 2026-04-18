package com.SafariTalk.lenguage_platform.controller;

import com.SafariTalk.lenguage_platform.dto.request.LoginRequest;
import com.SafariTalk.lenguage_platform.dto.response.AuthResponse;
import com.SafariTalk.lenguage_platform.exception.ApiError;
import com.SafariTalk.lenguage_platform.security.AuthenticatedUser;
import com.SafariTalk.lenguage_platform.security.JwtProperties;
import com.SafariTalk.lenguage_platform.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mi Controlador de Autenticación.
 * Pienso en este archivo como la caseta principal de mi club. Aquí es donde mis usuarios
 * vienen a validar quiénes son para poder recibir su "brazalete VIP" (Token JWT).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    /**
     * El punto exacto donde ocurre mi magia del "Log-in". Recibo un JSON ("LoginRequest") 
     * con tu correo y contraseña. Si todo empata, genero tu token. Si me fallas, 
     * te reboto un 401 Unauthorized sin decirte exactamente en qué te equivocaste (por seguridad).
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().trim().toLowerCase(), request.getPassword()));
            AuthenticatedUser user = (AuthenticatedUser) auth.getPrincipal();
            String token = jwtService.generateToken(user);
            long expiresSec = jwtProperties.getExpirationMs() / 1000;
            return ResponseEntity.ok(AuthResponse.of(
                    token, expiresSec, user.getId(), user.getEmail(), user.getRole().name()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiError.of(401, "Unauthorized", "Invalid email or password"));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiError.of(403, "Forbidden", e.getMessage()));
        }
    }
}
