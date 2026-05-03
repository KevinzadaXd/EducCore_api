package com.EducCore.EduCore.domain.Empresa;

import java.io.Serializable;

public record AddressData(
        String street,
        String number,
        String neighborhood,
        String city,
        String state
) implements Serializable {}
