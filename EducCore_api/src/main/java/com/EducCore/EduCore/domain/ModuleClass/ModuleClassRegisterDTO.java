package com.EducCore.EduCore.domain.ModuleClass;

import jakarta.validation.constraints.NotNull;

public record ModuleClassRegisterDTO(
        @NotNull Long idModule,
        @NotNull Long idClass
) {}
