package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.User.Icon;
import com.EducCore.EduCore.repositories.IconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/icons")
public class IconController {

    @Autowired
    private IconRepository repository;

    @GetMapping
    public List<Icon> getAllIcons() {
        return repository.findByStatusTrue();
    }
}