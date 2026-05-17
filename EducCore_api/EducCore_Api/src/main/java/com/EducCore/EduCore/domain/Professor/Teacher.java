package com.EducCore.EduCore.domain.Professor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Table(name = "teachers")
@Entity(name = "Teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    private String name;

    @Column(name = "imageurl")
    private String imageUrl;

    private String description;

    // Atualizado aqui: Mapeando diretamente para a nova coluna "position" do seu banco
    private Long position;

    private Boolean status;
}