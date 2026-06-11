package com.EducCore.EduCore.domain.User;

import java.time.LocalDate;

public record RegisterDTO(
        String name,
        String login, // seu e-mail do front
        String telephone,
        LocalDate birthDate,
        String password,
        UserRole role
) {}