package com.SafariTalk.lenguage_platform.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TutorProfileResponse {
    private Long id;
    private String name;
    private String language;
    private String country;
    private Double hourlyRate;
    private Double rating;
    private String bio;
    private String avatarUrl;
}
