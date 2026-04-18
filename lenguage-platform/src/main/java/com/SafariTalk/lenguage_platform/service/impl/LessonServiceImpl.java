package com.SafariTalk.lenguage_platform.service.impl;

import com.SafariTalk.lenguage_platform.dto.request.LessonRequest;
import com.SafariTalk.lenguage_platform.dto.response.LessonResponse;
import com.SafariTalk.lenguage_platform.exception.BusinessRuleException;
import com.SafariTalk.lenguage_platform.exception.ResourceNotFoundException;
import com.SafariTalk.lenguage_platform.mapper.LessonMapper;
import com.SafariTalk.lenguage_platform.model.LessonEntity;
import com.SafariTalk.lenguage_platform.model.LessonStatus;
import com.SafariTalk.lenguage_platform.repository.LessonRepository;
import com.SafariTalk.lenguage_platform.repository.StudentProfileRepository;
import com.SafariTalk.lenguage_platform.repository.TutorProfileRepository;
import com.SafariTalk.lenguage_platform.service.LessonService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final TutorProfileRepository tutorProfileRepository;
    private final LessonMapper lessonMapper;

    @Override
    @Transactional
    public LessonResponse create(LessonRequest request) {
        validateLessonTimes(request.getStartTime(), request.getEndTime());
        if (!studentProfileRepository.existsById(request.getStudentId())) {
            throw new ResourceNotFoundException("Student not found: " + request.getStudentId());
        }
        if (!tutorProfileRepository.existsById(request.getTutorId())) {
            throw new ResourceNotFoundException("Tutor not found: " + request.getTutorId());
        }
        LessonEntity entity = lessonMapper.toEntity(request);
        entity.setStatus(LessonStatus.SCHEDULED);
        LessonEntity saved = lessonRepository.save(entity);
        return lessonMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse getById(Long id) {
        LessonEntity entity = lessonRepository
                .findActiveWithAssociationsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + id));
        return lessonMapper.toResponse(entity);
    }

    private static void validateLessonTimes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new BusinessRuleException("Start and end time are required");
        }
        if (!end.isAfter(start)) {
            throw new BusinessRuleException("End time must be after start time");
        }
    }
}
