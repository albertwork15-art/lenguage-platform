package com.SafariTalk.lenguage_platform.mapper;

import com.SafariTalk.lenguage_platform.model.StudentProfileEntity;
import com.SafariTalk.lenguage_platform.model.TutorProfileEntity;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class EntityReferenceHelper {

    @Named("tutorRef")
    public TutorProfileEntity tutorRef(Long tutorId) {
        if (tutorId == null) {
            return null;
        }
        TutorProfileEntity ref = new TutorProfileEntity();
        ref.setId(tutorId);
        return ref;
    }

    @Named("studentRef")
    public StudentProfileEntity studentRef(Long studentId) {
        if (studentId == null) {
            return null;
        }
        StudentProfileEntity ref = new StudentProfileEntity();
        ref.setId(studentId);
        return ref;
    }

    @Named("tutorDisplayEmail")
    public String tutorDisplayEmail(TutorProfileEntity tutor) {
        if (tutor == null || tutor.getUserAccount() == null) {
            return null;
        }
        return tutor.getUserAccount().getEmail();
    }
}
