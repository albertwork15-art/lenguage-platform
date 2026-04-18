package com.SafariTalk.lenguage_platform.controller;

import com.SafariTalk.lenguage_platform.dto.request.StudentProfileRequest;
import com.SafariTalk.lenguage_platform.dto.response.StudentProfileResponse;
import com.SafariTalk.lenguage_platform.service.StudentProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mi Controlador del Perfil Estudiantil.
 * Mi único trabajo es dejar que mi estudiante vea y actualice la información
 * de su propio perfil (su idioma nativo, qué nivel tiene, biografías, etc).
 */
@RestController
@RequestMapping("/api/student-profiles")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @GetMapping("/{userId}")
    public StudentProfileResponse get(@PathVariable Long userId) {
        return studentProfileService.getByUserId(userId);
    }

    @PutMapping("/{userId}")
    public StudentProfileResponse update(
            @PathVariable Long userId, @Valid @RequestBody StudentProfileRequest request) {
        return studentProfileService.update(userId, request);
    }
}
