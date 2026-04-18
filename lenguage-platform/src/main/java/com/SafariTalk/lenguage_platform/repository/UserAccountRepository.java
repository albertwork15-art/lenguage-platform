package com.SafariTalk.lenguage_platform.repository;

import com.SafariTalk.lenguage_platform.model.UserAccountEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<UserAccountEntity> findByEmailIgnoreCase(String email);
}
