package com.EducCore.EduCore.domain.Course;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity(name = "courses")
@Table(name = "courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    private String category;
    private String title;

    @Column(name = "cover")
    private String cover; // URL da imagem no bucket

    private String price;
    private Boolean status;
    private String workload; // Carga horária (ex: 10h)

    @Column(columnDefinition = "TEXT")
    private String description;
}