package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.User.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IconRepository extends JpaRepository<Icon, Long> {
    // Busca apenas ícones ativos
    List<Icon> findByStatusTrue();
}