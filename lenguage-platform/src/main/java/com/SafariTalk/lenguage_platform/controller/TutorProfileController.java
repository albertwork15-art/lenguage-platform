package com.SafariTalk.lenguage_platform.controller;

import com.SafariTalk.lenguage_platform.dto.response.TutorProfileResponse;
import com.SafariTalk.lenguage_platform.service.TutorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/tutors")
@RequiredArgsConstructor
public class TutorProfileController {

    private final TutorProfileService tutorProfileService;

    @GetMapping
    public ResponseEntity<List<TutorProfileResponse>> getAllTutors() {
        return ResponseEntity.ok(tutorProfileService.getAllTutors());
    }
}
