package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.Professor.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    // Atualizado de OrderByOrderAsc para OrderByPositionAsc
    List<Teacher> findAllByOrderByPositionAsc();
}