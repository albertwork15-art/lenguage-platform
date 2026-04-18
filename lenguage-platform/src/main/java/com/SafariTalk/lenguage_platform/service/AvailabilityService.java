package com.SafariTalk.lenguage_platform.service;

import com.SafariTalk.lenguage_platform.dto.request.AvailabilityRequest;
import com.SafariTalk.lenguage_platform.dto.response.AvailabilityResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AvailabilityService {

    AvailabilityResponse create(AvailabilityRequest request);

    List<AvailabilityResponse> findAvailableSlots(Long tutorId, LocalDateTime start, LocalDateTime end);

    Page<AvailabilityResponse> findByTutor(Long tutorId, Pageable pageable);
}
