package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.Lesson.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByOrderByOrderAsc();
}
