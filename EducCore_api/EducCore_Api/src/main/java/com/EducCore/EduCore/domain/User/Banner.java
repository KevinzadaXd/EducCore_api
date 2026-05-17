package com.EducCore.EduCore.domain.User;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity(name = "banner")
@Table(name = "banner")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // Mapeia o 'nome' vindo do formulário

    @Column(name = "imageurl")
    private String imageurl;

    private Boolean status;

    private Long posicao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;
}