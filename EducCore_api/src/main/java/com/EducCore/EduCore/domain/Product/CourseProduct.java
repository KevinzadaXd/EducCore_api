package com.EducCore.EduCore.domain.Product;

import com.EducCore.EduCore.domain.Course.Course;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Table(name = "course_product")
@Entity(name = "CourseProduct")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CourseProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plan", nullable = false)
    private Product product;

    // Caso você já tenha a classe Course mapeada no seu projeto:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course", nullable = false)
    private Course course;
}