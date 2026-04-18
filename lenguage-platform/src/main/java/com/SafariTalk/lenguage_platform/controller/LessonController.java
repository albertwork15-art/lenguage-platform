package com.SafariTalk.lenguage_platform.controller;

import com.SafariTalk.lenguage_platform.dto.request.LessonRequest;
import com.SafariTalk.lenguage_platform.dto.response.LessonResponse;
import com.SafariTalk.lenguage_platform.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mi Controlador de Clases (Lecciones).
 * Aquí manejo mi magia de agendar: cuando mi estudiante quiere apartar un espacio 
 * para platicar con mi tutor, esta es mi ventanilla que los atiende.
 */
@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse create(@Valid @RequestBody LessonRequest request) {
        return lessonService.create(request);
    }

    @GetMapping("/{id}")
    public LessonResponse getById(@PathVariable Long id) {
        return lessonService.getById(id);
    }
}
