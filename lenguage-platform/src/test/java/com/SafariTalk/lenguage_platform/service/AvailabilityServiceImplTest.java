package com.SafariTalk.lenguage_platform.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.SafariTalk.lenguage_platform.dto.request.AvailabilityRequest;
import com.SafariTalk.lenguage_platform.dto.response.AvailabilityResponse;
import com.SafariTalk.lenguage_platform.exception.BusinessRuleException;
import com.SafariTalk.lenguage_platform.exception.ResourceNotFoundException;
import com.SafariTalk.lenguage_platform.mapper.AvailabilityMapper;
import com.SafariTalk.lenguage_platform.model.AvailabilityEntity;
import com.SafariTalk.lenguage_platform.repository.AvailabilityRepository;
import com.SafariTalk.lenguage_platform.repository.TutorProfileRepository;
import com.SafariTalk.lenguage_platform.service.impl.AvailabilityServiceImpl;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceImplTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private TutorProfileRepository tutorProfileRepository;

    @Mock
    private AvailabilityMapper availabilityMapper;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    @Test
    void create_rejectsWhenEndNotAfterStart() {
        AvailabilityRequest req = new AvailabilityRequest();
        req.setTutorId(1L);
        req.setStartTime(LocalDateTime.now().plusDays(1));
        req.setEndTime(req.getStartTime());

        assertThatThrownBy(() -> availabilityService.create(req)).isInstanceOf(BusinessRuleException.class);

        verify(availabilityRepository, never()).save(any());
    }

    @Test
    void create_rejectsWhenTutorMissing() {
        AvailabilityRequest req = new AvailabilityRequest();
        req.setTutorId(99L);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        req.setStartTime(start);
        req.setEndTime(start.plusHours(1));
        when(tutorProfileRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> availabilityService.create(req)).isInstanceOf(ResourceNotFoundException.class);

        verify(availabilityRepository, never()).save(any());
    }

    @Test
    void create_persistsWhenValid() {
        AvailabilityRequest req = new AvailabilityRequest();
        req.setTutorId(1L);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        req.setStartTime(start);
        req.setEndTime(start.plusHours(1));
        when(tutorProfileRepository.existsById(1L)).thenReturn(true);
        when(availabilityRepository.existsOverlappingSlot(1L, start, start.plusHours(1))).thenReturn(false);

        AvailabilityEntity mapped = mock(AvailabilityEntity.class);
        when(availabilityMapper.toEntity(req)).thenReturn(mapped);
        when(availabilityRepository.save(mapped)).thenReturn(mapped);

        AvailabilityResponse dto = new AvailabilityResponse(5L, 1L, start, start.plusHours(1), false);
        when(availabilityMapper.toResponse(mapped)).thenReturn(dto);

        AvailabilityResponse result = availabilityService.create(req);

        assertThat(result.id()).isEqualTo(5L);
        verify(mapped).setBooked(false);
        verify(availabilityRepository).save(mapped);
    }
}
