package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.User.Banner;
import com.EducCore.EduCore.repositories.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@Tag(name = "Banners", description = "Endpoints para gerenciamento de banners da escola")
public class BannerController {

    @Autowired
    private BannerRepository repository;

    @GetMapping
    public ResponseEntity<List<Banner>> getAllBanners() {
        List<Banner> banners = repository.findAll();
        return ResponseEntity.ok(banners);
    }
}