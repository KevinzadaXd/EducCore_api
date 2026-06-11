package com.EducCore.EduCore.domain.Lesson;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "\"class\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "videourl", columnDefinition = "TEXT")
    private String videoUrl;

    @Column(name = "\"order\"")
    private Integer order;

    private Boolean status;
}
