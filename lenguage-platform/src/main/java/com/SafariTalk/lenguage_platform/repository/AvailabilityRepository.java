package com.SafariTalk.lenguage_platform.repository;

import com.SafariTalk.lenguage_platform.model.AvailabilityEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity, Long> {

    Page<AvailabilityEntity> findByTutorId(Long tutorId, Pageable pageable);

    @Query(
            "SELECT a FROM AvailabilityEntity a "
                    + "WHERE a.tutor.id = :tutorId "
                    + "AND a.isBooked = false "
                    + "AND a.startTime >= :start "
                    + "AND a.endTime <= :end "
                    + "ORDER BY a.startTime ASC")
    List<AvailabilityEntity> findAvailableSlotsByTutorAndRange(
            @Param("tutorId") Long tutorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query(
            "SELECT COUNT(a) > 0 FROM AvailabilityEntity a "
                    + "WHERE a.tutor.id = :tutorId "
                    + "AND (:start < a.endTime AND :end > a.startTime)")
    boolean existsOverlappingSlot(
            @Param("tutorId") Long tutorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
