package com.EducCore.EduCore.domain.ModuleCourse;

import jakarta.validation.constraints.NotNull;

public record ModuleCourseRegisterDTO(
        @NotNull Long idModule,
        @NotNull Long idCourse
) {}
