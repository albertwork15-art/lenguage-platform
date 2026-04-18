package com.SafariTalk.lenguage_platform.repository;

import com.SafariTalk.lenguage_platform.model.LessonEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {

    @Query("""
            SELECT DISTINCT l FROM LessonEntity l
            JOIN FETCH l.student
            JOIN FETCH l.tutor t
            JOIN FETCH t.userAccount
            WHERE l.id = :id AND l.isDeleted = false
            """)
    Optional<LessonEntity> findActiveWithAssociationsById(@Param("id") Long id);
}
