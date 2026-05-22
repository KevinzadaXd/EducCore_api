package com.EducCore.EduCore.domain.Faq;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    private String question;

    private String response;

    @Column(name = "\"order\"")
    private Long order;

    private Boolean status;

    public Faq(FaqRegisterDTO data) {
        this.question = data.question();
        this.response = data.response();
        this.order = data.order();
        this.status = data.status() != null ? data.status() : true;
    }

    public void updateFromDTO(FaqRegisterDTO data) {
        if (data.question() != null) this.question = data.question();
        if (data.response() != null) this.response = data.response();
        if (data.order() != null) this.order = data.order();
        if (data.status() != null) this.status = data.status();
    }
}
