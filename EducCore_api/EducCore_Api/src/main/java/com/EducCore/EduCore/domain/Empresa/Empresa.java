package com.EducCore.EduCore.domain.Empresa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    private String name;
    
    private String slogan;
    
    private String cnpj;
    
    private String email;
    
    private String telephone;

    @Column(name = "wathsapp")
    private String whatsapp;

    @Column(name = "schedule")
    private String operatingHours;

    @Column(name = "cep")
    private String zipCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "adress")
    private AddressData address;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "socialmedia")
    private SocialMediaData socialMedia;

    @Column(name = "logourl", columnDefinition = "TEXT")
    private String logourl;

    @Column(name = "bannerurl", columnDefinition = "TEXT")
    private String bannerurl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "color")
    private ColorData color;

    public Empresa(EmpresaRegisterDTO data) {
        this.name = data.name();
        this.slogan = data.slogan();
        this.cnpj = data.cnpj();
        this.email = data.email();
        this.telephone = data.telephone();
        this.whatsapp = data.whatsapp();
        this.operatingHours = data.operatingHours();
        this.zipCode = data.zipCode();
        this.address = data.address();
        this.socialMedia = data.socialMedia();
        this.color = data.color();
        this.logourl = data.logourl();
        this.bannerurl = data.bannerurl();
    }
}
