package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.User.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}