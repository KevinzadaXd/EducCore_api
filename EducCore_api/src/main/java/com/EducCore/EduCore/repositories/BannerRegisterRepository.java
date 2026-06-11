package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.User.BannerRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BannerRegisterRepository extends JpaRepository<BannerRegister, Long> {

    List<BannerRegister> findByStatusTrue();
}