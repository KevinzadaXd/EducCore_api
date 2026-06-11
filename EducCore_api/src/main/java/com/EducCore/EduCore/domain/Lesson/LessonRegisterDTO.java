package com.EducCore.EduCore.domain.Lesson;

public record LessonRegisterDTO(
        String name,
        String description,
        String videoUrl,
        Integer order,
        Boolean status
) {}
