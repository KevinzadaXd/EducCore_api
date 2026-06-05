package com.EducCore.EduCore.domain.Product;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "product")
@Entity(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    private String name;

    private String price;

    private String discount;

    @Column(name = "imageurl")
    private String imageUrl;

    private String category;

    @Setter
    @Enumerated(EnumType.STRING)
    private ProductType type;

    // ✅ CORREÇÃO: Substituído o @ElementCollection pelo mapeamento correto da tabela intermediária
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseProduct> cursos = new ArrayList<>();
}