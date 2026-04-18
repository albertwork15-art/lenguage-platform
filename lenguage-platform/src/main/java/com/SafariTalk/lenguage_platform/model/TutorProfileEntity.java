package com.SafariTalk.lenguage_platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Mi entidad de Perfil del Tutor/Profesor.
 * Tabla ligada a mi cuenta maestra que guarda lo específico de enseñar: 
 * ¿A cuánto cobro la hora?, ¿cuál es mi video de YouTube para presentarme?, y mi calificación (rating).
 */
@Entity
@Table(name = "tutor_profiles")
@Getter
@Setter
@NoArgsConstructor
public class TutorProfileEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserAccountEntity userAccount;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String videoIntroUrl;
    private Double hourlyRate;
    private Double rating;
    private boolean isCertified;
}
