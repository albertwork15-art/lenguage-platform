package com.SafariTalk.lenguage_platform.service.impl;

import com.SafariTalk.lenguage_platform.dto.response.TutorProfileResponse;
import com.SafariTalk.lenguage_platform.model.TutorProfileEntity;
import com.SafariTalk.lenguage_platform.repository.TutorProfileRepository;
import com.SafariTalk.lenguage_platform.service.TutorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutorProfileServiceImpl implements TutorProfileService {

    private final TutorProfileRepository tutorProfileRepository;

    @Override
    public List<TutorProfileResponse> getAllTutors() {
        List<TutorProfileEntity> tutors = tutorProfileRepository.findAll();
        
        return tutors.stream().map(t -> TutorProfileResponse.builder()
                .id(t.getId())
                // Extrayendo el pseudónimo temporalmente de mi correo electrónico
                .name("Tutor " + (t.getUserAccount() != null ? t.getUserAccount().getEmail().split("@")[0] : "Anónimo"))
                // Variables que temporalmente hago random para enriquecer el UI (después pasarán a mi modelo de BD real)
                .language(t.getId() % 3 == 0 ? "Inglés" : (t.getId() % 3 == 1 ? "Francés" : "Portugués")) 
                .country(t.getId() % 3 == 0 ? "Estados Unidos" : (t.getId() % 3 == 1 ? "Francia" : "Brasil"))
                .hourlyRate(t.getHourlyRate() != null ? t.getHourlyRate() : 15.0)
                .rating(t.getRating() != null ? t.getRating() : 5.0)
                .bio(t.getBio() != null ? t.getBio() : "Hola, soy tutor apasionado por enseñar idiomas.")
                .avatarUrl("https://i.pravatar.cc/150?img=" + (10 + t.getId()))
                .build()
        ).collect(Collectors.toList());
    }
}
