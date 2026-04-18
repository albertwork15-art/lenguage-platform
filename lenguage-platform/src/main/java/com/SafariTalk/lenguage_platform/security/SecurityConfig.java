package com.SafariTalk.lenguage_platform.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Yo soy la clase principal que actúa como el "Cadenero" de la discoteca.
 * Básicamente le digo a Spring qué rutas de la web están abiertas al público
 * y cuáles necesitan que el usuario presente su "credencial" (Token JWT).
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    /**
     * El PasswordEncoder se encarga de que nunca guarde contraseñas en texto plano (como "123456").
     * Uso BCrypt, que procesa la contraseña con matemáticas complejas (hashing/salting)
     * para que si alguien me roba la base de datos, vea solo basura inentendible.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * El AuthenticationManager es mi jefe supremo que valida que el usuario y la contraseña 
     * coincidan en mi base de datos de verdad.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * El SecurityFilterChain es mi conjunto de reglas. Aquí le digo:
     * - Apaga CSRF porque uso JWT (no manejo cookies del navegador clásicas).
     * - Activa CORS (para que el frontend en otro dominio pueda hacer peticiones).
     * - Las sesiones son "STATELESS" (la app no se acuerda de ti, en cada click debes enviarme tu token).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(HttpMethod.POST, "/api/users/register", "/api/auth/login")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/tutors")
                                // Le digo: "Deja pasar a todos los que vengan a ver los tutores sin iniciar sesión"
                                .permitAll()
                                // Dejo ver la consola en mi memoria si estás en modo DEV
                                .requestMatchers("/h2-console", "/h2-console/**")
                                .permitAll()
                                // Dejo pasar solicitudes previas de tipo OPTIONS (cosas técnicas de los navegadores)
                                .requestMatchers(HttpMethod.OPTIONS, "/**")
                                .permitAll()
                                // a cualquier OTRA ruta de la app, PÍDELE QUE ESTÉ AUTENTICADO
                                .anyRequest()
                                .authenticated())
                // Configuro los headers para que mi consola h2 no falle internamente
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // Si alguien la riega o le falta acceso, los lanzo con mis propios mensajes de error JSON
                .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                // Por último, meto a mi "cadenero JWT" justo antes del filtro normal de contraseñas.
                // Él agarrará las peticiones, revisará el token, identificará al usuario y lo dejará pasar.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS (Cross-Origin Resource Sharing). Protejo que sitios malvados no usen mi backend.
     * Como estoy en inicio, aquí estoy diciendo que me permita peticiones desde cualquier lado ("*")
     * y con casi cualquier método (GET, POST, etc). En producción puedo restringir el "*" a "mi-frontend.com".
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
