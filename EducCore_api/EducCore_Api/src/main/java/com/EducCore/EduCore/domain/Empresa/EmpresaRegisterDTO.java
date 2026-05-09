package com.EducCore.EduCore.domain.Empresa;

public record EmpresaRegisterDTO(
        String name,
        String slogan,
        String cnpj,
        String email,
        String telephone,
        String whatsapp,
        String operatingHours,
        String zipCode,
        AddressData address,
        SocialMediaData socialMedia,
        String firstcolor,
        String secondcolor,
        String logourl,
        String bannerurl
) {}
