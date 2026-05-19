package com.EducCore.EduCore.domain.Empresa;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Table(name = "pages")
@Entity(name = "PageEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    private String name;

    @Column(name = "\"order\"") // Aspas evitam conflito com a palavra reservada ORDER do SQL
    private Long order;

    private Boolean status;

    private String description;
}