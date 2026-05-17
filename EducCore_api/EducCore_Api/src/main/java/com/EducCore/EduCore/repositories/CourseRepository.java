package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.Course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // Retorna os cursos mais recentes primeiro
    List<Course> findAllByOrderByCreatedAtDesc();
}