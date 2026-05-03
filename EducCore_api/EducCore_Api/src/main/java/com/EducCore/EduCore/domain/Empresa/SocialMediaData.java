package com.EducCore.EduCore.domain.Empresa;

import java.io.Serializable;

public record SocialMediaData(
                String instagram,
                String facebook,
                String linkedin) implements Serializable {
}
