package com.SafariTalk.lenguage_platform.service;

import com.SafariTalk.lenguage_platform.dto.request.UserRegistrationRequest;
import com.SafariTalk.lenguage_platform.dto.response.UserResponse;

public interface UserAccountService {

    UserResponse registerStudent(UserRegistrationRequest request);
}
