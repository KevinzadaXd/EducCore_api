package com.EducCore.EduCore.domain.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "icons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Icon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "imageurl")
    private String imageurl;

    private Boolean status;
}