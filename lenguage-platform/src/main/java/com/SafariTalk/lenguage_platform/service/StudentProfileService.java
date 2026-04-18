package com.SafariTalk.lenguage_platform.service;

import com.SafariTalk.lenguage_platform.dto.request.StudentProfileRequest;
import com.SafariTalk.lenguage_platform.dto.response.StudentProfileResponse;

public interface StudentProfileService {

    StudentProfileResponse getByUserId(Long userId);

    StudentProfileResponse update(Long userId, StudentProfileRequest request);
}
