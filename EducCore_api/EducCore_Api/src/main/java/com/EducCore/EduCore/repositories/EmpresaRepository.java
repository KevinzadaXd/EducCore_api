package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.Empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
