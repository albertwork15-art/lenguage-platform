package com.SafariTalk.lenguage_platform.repository;

import com.SafariTalk.lenguage_platform.model.StudentProfileEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfileEntity, Long> {

    @EntityGraph(attributePaths = "userAccount")
    @Query("SELECT s FROM StudentProfileEntity s WHERE s.id = :id")
    Optional<StudentProfileEntity> findWithUserAccountById(@Param("id") Long id);
}
