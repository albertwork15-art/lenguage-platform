package com.SafariTalk.lenguage_platform.mapper;

import com.SafariTalk.lenguage_platform.dto.request.LessonRequest;
import com.SafariTalk.lenguage_platform.dto.response.LessonResponse;
import com.SafariTalk.lenguage_platform.model.LessonEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = EntityReferenceHelper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LessonMapper {

    @Mapping(target = "tutorId", source = "tutor.id")
    @Mapping(target = "tutorName", source = "tutor", qualifiedByName = "tutorDisplayEmail")
    @Mapping(target = "studentId", source = "student.id")
    LessonResponse toResponse(LessonEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "meetingLink", ignore = true)
    @Mapping(target = "student", source = "studentId", qualifiedByName = "studentRef")
    @Mapping(target = "tutor", source = "tutorId", qualifiedByName = "tutorRef")
    LessonEntity toEntity(LessonRequest request);

    @AfterMapping
    default void markLessonNotDeleted(@MappingTarget LessonEntity entity) {
        entity.setDeleted(false);
    }
}
