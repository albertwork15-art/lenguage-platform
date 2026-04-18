package com.SafariTalk.lenguage_platform.controller;

import com.SafariTalk.lenguage_platform.dto.request.UserRegistrationRequest;
import com.SafariTalk.lenguage_platform.dto.response.UserResponse;
import com.SafariTalk.lenguage_platform.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mi Controlador de las Cuentas de Usuario.
 * Me encargo estrictamente del registro de mis nuevos usuarios que llegan a mi app.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    /**
     * Mi punto final para registrarse (Crear una cuenta nueva).
     * Noto que devuelvo un código 201 (CREATED) si todo me salió bien, conforme a mis buenas
     * prácticas de APIs REST (te aviso "hey, sí lo creé con éxito").
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody UserRegistrationRequest request) {
        return userAccountService.registerStudent(request);
    }
}
