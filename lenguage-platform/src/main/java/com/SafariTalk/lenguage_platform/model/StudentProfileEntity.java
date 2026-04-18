package com.SafariTalk.lenguage_platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Mi entidad de Perfil del Estudiante.
 * Como no quiero mezclar credenciales de login con cosas personales, creo esta
 * tabla separada atada "1 a 1" con el usuario. Guardo mis metas, nombres y minutos que he comprado.
 */
@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
public class StudentProfileEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private UserAccountEntity userAccount;

    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "target_language", nullable = false)
    private String targetLanguage;

    @Column(name = "native_language", nullable = false)
    private String nativeLanguage;

    private Integer totalMinutesBalance;
}
