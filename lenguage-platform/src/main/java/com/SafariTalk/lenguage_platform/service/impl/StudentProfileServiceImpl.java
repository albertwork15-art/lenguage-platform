package com.SafariTalk.lenguage_platform.service.impl;

import com.SafariTalk.lenguage_platform.dto.request.StudentProfileRequest;
import com.SafariTalk.lenguage_platform.dto.response.StudentProfileResponse;
import com.SafariTalk.lenguage_platform.exception.ResourceNotFoundException;
import com.SafariTalk.lenguage_platform.mapper.StudentProfileMapper;
import com.SafariTalk.lenguage_platform.model.StudentProfileEntity;
import com.SafariTalk.lenguage_platform.repository.StudentProfileRepository;
import com.SafariTalk.lenguage_platform.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentProfileMapper studentProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public StudentProfileResponse getByUserId(Long userId) {
        StudentProfileEntity entity = studentProfileRepository
                .findWithUserAccountById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user id " + userId));
        return studentProfileMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public StudentProfileResponse update(Long userId, StudentProfileRequest request) {
        StudentProfileEntity entity = studentProfileRepository
                .findWithUserAccountById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user id " + userId));
        studentProfileMapper.updateFromRequest(request, entity);
        StudentProfileEntity saved = studentProfileRepository.save(entity);
        return studentProfileMapper.toResponse(saved);
    }
}
