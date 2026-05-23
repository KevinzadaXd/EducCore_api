package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.User.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    // Retorna os banners ordenados pela posição configurada
    List<Banner> findAllByOrderByPosicaoAsc();
}