package com.EducCore.EduCore.domain.User;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // 🟢 Aplica a conversão de String <-> Enum automaticamente
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(UserRole attribute) {
        if (attribute == null) return null;
        return attribute.getRole();
    }

    @Override
    public UserRole convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return UserRole.fromCode(dbData);
    }
}