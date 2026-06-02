package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.User.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {

    // Método útil para quando precisar de listar todos os cursos de um utilizador específico
    List<CourseUser> findByUserId(Long userId);

    // Método essencial para apagar os vínculos antigos quando atualizar ou eliminar o utilizador
    void deleteByUserId(Long userId);
}