package com.SafariTalk.lenguage_platform.dto.response;

import java.time.LocalDateTime;

public record LessonResponse(
        Long id,
        Long tutorId,
        String tutorName,
        Long studentId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LessonStatusView status,
        String meetingLink) {}
