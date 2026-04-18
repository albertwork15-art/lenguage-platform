package com.SafariTalk.lenguage_platform.controller;

import com.SafariTalk.lenguage_platform.dto.request.AvailabilityRequest;
import com.SafariTalk.lenguage_platform.dto.response.AvailabilityResponse;
import com.SafariTalk.lenguage_platform.service.AvailabilityService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mi Controlador de Disponibilidad (El calendario de mis profes).
 * Administro las franjas de tiempo libre de mis tutores. Me permite registrar
 * cuándo están libres y consultarlo para agendarles una clase.
 */
@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvailabilityResponse create(@Valid @RequestBody AvailabilityRequest request) {
        return availabilityService.create(request);
    }

    @GetMapping("/open-slots")
    public List<AvailabilityResponse> openSlots(
            @RequestParam Long tutorId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return availabilityService.findAvailableSlots(tutorId, start, end);
    }

    @GetMapping
    public Page<AvailabilityResponse> byTutor(@RequestParam Long tutorId, Pageable pageable) {
        return availabilityService.findByTutor(tutorId, pageable);
    }
}
