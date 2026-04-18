package com.SafariTalk.lenguage_platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentProfileRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Target language is required")
    private String targetLanguage;

    @NotBlank(message = "Native language is required")
    private String nativeLanguage;
}
