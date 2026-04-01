package com.EducCore.EduCore.domain.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "banner_register") // Nome exato da sua tabela no Supabase
@Getter
@Setter
public class BannerRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageurl;
    private Boolean status;
}