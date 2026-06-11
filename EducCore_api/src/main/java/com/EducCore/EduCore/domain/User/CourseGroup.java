package com.EducCore.EduCore.domain.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "course_group", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "id_group", insertable = false, updatable = false)
    private Long idGroup;

    @Column(name = "id_course", nullable = false)
    private Long idCourse;

    @Column(name = "name_group")
    private String nameGroup;

    @Column(name = "name_course", nullable = false)
    private String nameCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_group", nullable = false)
    @JsonIgnore
    private Group group;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
}