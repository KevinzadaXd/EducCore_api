package com.EducCore.EduCore.domain.User;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    @JsonValue
    public String getRole() {
        return role;
    }

    public static UserRole fromCode(String code) {
        if (code == null) return null;
        for (UserRole val : UserRole.values()) {
            if (val.getRole().equalsIgnoreCase(code.trim())) {
                return val;
            }
        }
        throw new IllegalArgumentException("Role desconhecida: " + code);
    }
}