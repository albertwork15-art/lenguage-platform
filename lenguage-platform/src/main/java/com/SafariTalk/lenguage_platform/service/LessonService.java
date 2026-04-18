package com.SafariTalk.lenguage_platform.service;

import com.SafariTalk.lenguage_platform.dto.request.LessonRequest;
import com.SafariTalk.lenguage_platform.dto.response.LessonResponse;

public interface LessonService {

    LessonResponse create(LessonRequest request);

    LessonResponse getById(Long id);
}
