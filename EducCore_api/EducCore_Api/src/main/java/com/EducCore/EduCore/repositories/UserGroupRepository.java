package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.User.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    // Método útil para quando precisar de listar todos os grupos de um utilizador específico
    List<UserGroup> findByUserId(Long userId);

    // Método essencial para apagar os vínculos antigos quando atualizar ou eliminar o utilizador
    void deleteByUserId(Long userId);
}