package com.EducCore.EduCore.domain.User;

import jakarta.persistence.*;
import lombok.*;

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

    private String title;

    @Column(name = "imageurl")
    private String imageurl;

    private Boolean status;
}