package com.SafariTalk.lenguage_platform.mapper;

import com.SafariTalk.lenguage_platform.dto.request.StudentProfileRequest;
import com.SafariTalk.lenguage_platform.dto.response.StudentProfileResponse;
import com.SafariTalk.lenguage_platform.model.StudentProfileEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentProfileMapper {

    @Mapping(target = "email", source = "userAccount.email")
    StudentProfileResponse toResponse(StudentProfileEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "totalMinutesBalance", ignore = true)
    StudentProfileEntity toEntity(StudentProfileRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "totalMinutesBalance", ignore = true)
    void updateFromRequest(StudentProfileRequest request, @MappingTarget StudentProfileEntity entity);
}
