package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.User.BannerRegister;
import com.EducCore.EduCore.repositories.BannerRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banner-register")
public class BannerRegisterController {
    @Autowired
    private BannerRegisterRepository repository;

    @GetMapping
    public List<BannerRegister> getActiveBanners() {
        return repository.findByStatusTrue(); // Retorna apenas o que estiver com status true
    }
}