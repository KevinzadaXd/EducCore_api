package com.EducCore.EduCore.domain.ModuleCourse;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "module_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ModuleCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "id_module")
    private Long idModule;

    @Column(name = "id_course")
    private Long idCourse;

    @Column(name = "name_module")
    private String nameModule;

    @Column(name = "name_course")
    private String nameCourse;
}
