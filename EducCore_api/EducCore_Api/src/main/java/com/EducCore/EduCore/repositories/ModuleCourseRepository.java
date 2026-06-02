package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.ModuleCourse.ModuleCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleCourseRepository extends JpaRepository<ModuleCourse, Long> {

    List<ModuleCourse> findByIdCourse(Long idCourse);

    boolean existsByIdModuleAndIdCourse(Long idModule, Long idCourse);

    void deleteByIdModuleAndIdCourse(Long idModule, Long idCourse);
}
