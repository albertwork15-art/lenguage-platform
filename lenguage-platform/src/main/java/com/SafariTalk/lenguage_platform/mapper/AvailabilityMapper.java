package com.SafariTalk.lenguage_platform.mapper;

import com.SafariTalk.lenguage_platform.dto.request.AvailabilityRequest;
import com.SafariTalk.lenguage_platform.dto.response.AvailabilityResponse;
import com.SafariTalk.lenguage_platform.model.AvailabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = EntityReferenceHelper.class)
public interface AvailabilityMapper {

    @Mapping(target = "tutorId", source = "tutor.id")
    AvailabilityResponse toResponse(AvailabilityEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isBooked", ignore = true)
    @Mapping(target = "tutor", source = "tutorId", qualifiedByName = "tutorRef")
    AvailabilityEntity toEntity(AvailabilityRequest request);
}
