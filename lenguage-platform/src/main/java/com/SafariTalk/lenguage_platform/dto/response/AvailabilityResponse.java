package com.SafariTalk.lenguage_platform.dto.response;

import java.time.LocalDateTime;

public record AvailabilityResponse(
        Long id, Long tutorId, LocalDateTime startTime, LocalDateTime endTime, boolean isBooked) {}
