package com.SafariTalk.lenguage_platform.service;

import com.SafariTalk.lenguage_platform.dto.response.TutorProfileResponse;
import java.util.List;

public interface TutorProfileService {
    List<TutorProfileResponse> getAllTutors();
}
