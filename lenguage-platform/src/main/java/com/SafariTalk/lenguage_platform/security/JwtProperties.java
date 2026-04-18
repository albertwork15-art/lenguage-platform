package com.SafariTalk.lenguage_platform.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.jwt")
@Getter
@Setter
public class JwtProperties {

    /** HS256: min 32 bytes. Requiero configuracion (env APP_SECURITY_JWT_SECRET). */
    private String secret;

    private long expirationMs = 86_400_000L;
}
