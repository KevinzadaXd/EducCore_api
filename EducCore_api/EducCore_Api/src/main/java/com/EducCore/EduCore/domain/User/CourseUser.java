package com.EducCore.EduCore.domain.User;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "course_user")
@Entity(name = "CourseUser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CourseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_user", nullable = false)  // ✅ era user_id
    private Long userId;

    @Column(name = "id_course", nullable = false) // ✅ era course_id
    private Long courseId;

    public CourseUser(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }
}