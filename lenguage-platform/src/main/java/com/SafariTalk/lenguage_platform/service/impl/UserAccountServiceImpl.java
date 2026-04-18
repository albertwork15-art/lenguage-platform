package com.SafariTalk.lenguage_platform.service.impl;

import com.SafariTalk.lenguage_platform.dto.request.UserRegistrationRequest;
import com.SafariTalk.lenguage_platform.dto.response.UserResponse;
import com.SafariTalk.lenguage_platform.exception.BusinessRuleException;
import com.SafariTalk.lenguage_platform.model.StudentProfileEntity;
import com.SafariTalk.lenguage_platform.model.UserAccountEntity;
import com.SafariTalk.lenguage_platform.model.UserRole;
import com.SafariTalk.lenguage_platform.repository.StudentProfileRepository;
import com.SafariTalk.lenguage_platform.repository.UserAccountRepository;
import com.SafariTalk.lenguage_platform.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse registerStudent(UserRegistrationRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userAccountRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessRuleException("Email already registered");
        }
        UserAccountEntity user = UserAccountEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(true)
                .role(UserRole.STUDENT)
                .build();
        user = userAccountRepository.save(user);

        StudentProfileEntity profile = new StudentProfileEntity();
        profile.setUserAccount(user);
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setTargetLanguage(request.getTargetLanguage());
        profile.setNativeLanguage(request.getNativeLanguage());
        profile.setTotalMinutesBalance(0);
        studentProfileRepository.save(profile);

        return new UserResponse(user.getId(), user.getEmail());
    }
}
