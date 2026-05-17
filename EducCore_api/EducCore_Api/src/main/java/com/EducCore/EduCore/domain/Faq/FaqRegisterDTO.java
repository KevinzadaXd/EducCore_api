package com.EducCore.EduCore.domain.Faq;

public record FaqRegisterDTO(
        String question,
        String response,
        Long order,
        Boolean status
) {}
