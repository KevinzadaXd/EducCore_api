package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.Empresa.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    // Métodos customizados de busca podem ser adicionados aqui futuramente se necessário
}