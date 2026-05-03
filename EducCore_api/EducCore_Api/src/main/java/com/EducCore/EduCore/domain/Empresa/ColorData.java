package com.EducCore.EduCore.domain.Empresa;

import java.io.Serializable;

public record ColorData(
        String primary,
        String secondary,
        String background,
        String text
) implements Serializable {}
