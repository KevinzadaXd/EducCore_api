package com.EducCore.EduCore.domain.User;

public record LoginResponseDTO(String token, String name, Long id, String role) {
}