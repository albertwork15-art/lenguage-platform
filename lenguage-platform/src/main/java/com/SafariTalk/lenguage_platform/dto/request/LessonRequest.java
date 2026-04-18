package com.SafariTalk.lenguage_platform.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonRequest {

    @NotNull(message = "Tutor ID is required")
    private Long tutorId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Start time is required")
    @Future(message = "Lesson must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @AssertTrue(message = "End time must be after start time")
    public boolean isValidTimeRange() {
        if (startTime == null || endTime == null) {
            return true;
        }
        return endTime.isAfter(startTime);
    }
}
