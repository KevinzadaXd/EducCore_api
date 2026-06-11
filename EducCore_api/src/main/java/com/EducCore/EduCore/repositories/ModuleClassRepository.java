package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.ModuleClass.ModuleClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleClassRepository extends JpaRepository<ModuleClass, Long> {
    List<ModuleClass> findByIdModule(Long idModule);
    List<ModuleClass> findByIdClass(Long idClass);
    boolean existsByIdModuleAndIdClass(Long idModule, Long idClass);
    void deleteByIdModuleAndIdClass(Long idModule, Long idClass);
}
