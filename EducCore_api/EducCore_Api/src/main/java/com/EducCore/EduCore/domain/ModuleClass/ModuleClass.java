package com.EducCore.EduCore.domain.ModuleClass;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "modules_class")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ModuleClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "id_module")
    private Long idModule;

    @Column(name = "id_class")
    private Long idClass;

    @Column(name = "name_class")
    private String nameClass;

    @Column(name = "name_module")
    private String nameModule;
}
