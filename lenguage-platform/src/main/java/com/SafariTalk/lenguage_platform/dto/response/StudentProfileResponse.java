package com.SafariTalk.lenguage_platform.dto.response;

public record StudentProfileResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String targetLanguage,
        String nativeLanguage,
        Integer totalMinutesBalance) {}
