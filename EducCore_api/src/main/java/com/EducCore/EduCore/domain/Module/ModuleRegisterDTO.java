package com.EducCore.EduCore.domain.Module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModuleRegisterDTO(
        @NotBlank String name,
        @NotNull Integer order
) {}
