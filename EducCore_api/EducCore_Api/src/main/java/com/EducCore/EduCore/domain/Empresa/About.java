package com.EducCore.EduCore.domain.Empresa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "about")
@Entity(name = "AboutEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class About {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_page")
    @JsonBackReference
    private Page page;

    @Column(name = "image_url")
    private String imageUrl;
}