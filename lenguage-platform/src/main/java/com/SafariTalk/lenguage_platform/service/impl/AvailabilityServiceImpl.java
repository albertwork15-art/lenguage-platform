package com.SafariTalk.lenguage_platform.service.impl;

import com.SafariTalk.lenguage_platform.dto.request.AvailabilityRequest;
import com.SafariTalk.lenguage_platform.dto.response.AvailabilityResponse;
import com.SafariTalk.lenguage_platform.exception.BusinessRuleException;
import com.SafariTalk.lenguage_platform.exception.ResourceNotFoundException;
import com.SafariTalk.lenguage_platform.mapper.AvailabilityMapper;
import com.SafariTalk.lenguage_platform.model.AvailabilityEntity;
import com.SafariTalk.lenguage_platform.repository.AvailabilityRepository;
import com.SafariTalk.lenguage_platform.repository.TutorProfileRepository;
import com.SafariTalk.lenguage_platform.service.AvailabilityService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final TutorProfileRepository tutorProfileRepository;
    private final AvailabilityMapper availabilityMapper;

    @Override
    @Transactional
    public AvailabilityResponse create(AvailabilityRequest request) {
        validateSlotTimes(request.getStartTime(), request.getEndTime());
        if (!tutorProfileRepository.existsById(request.getTutorId())) {
            throw new ResourceNotFoundException("Tutor not found: " + request.getTutorId());
        }
        if (availabilityRepository.existsOverlappingSlot(
                request.getTutorId(), request.getStartTime(), request.getEndTime())) {
            throw new BusinessRuleException("Overlapping availability slot");
        }
        AvailabilityEntity entity = availabilityMapper.toEntity(request);
        entity.setBooked(false);
        AvailabilityEntity saved = availabilityRepository.save(entity);
        return availabilityMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponse> findAvailableSlots(
            Long tutorId, LocalDateTime start, LocalDateTime end) {
        validateSlotTimes(start, end);
        return availabilityRepository.findAvailableSlotsByTutorAndRange(tutorId, start, end).stream()
                .map(availabilityMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AvailabilityResponse> findByTutor(Long tutorId, Pageable pageable) {
        return availabilityRepository.findByTutorId(tutorId, pageable).map(availabilityMapper::toResponse);
    }

    private static void validateSlotTimes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new BusinessRuleException("Start and end time are required");
        }
        if (!end.isAfter(start)) {
            throw new BusinessRuleException("End time must be after start time");
        }
    }
}
