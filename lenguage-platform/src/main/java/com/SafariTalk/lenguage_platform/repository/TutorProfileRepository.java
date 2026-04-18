package com.SafariTalk.lenguage_platform.repository;

import com.SafariTalk.lenguage_platform.model.TutorProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorProfileRepository extends JpaRepository<TutorProfileEntity, Long> {
}
