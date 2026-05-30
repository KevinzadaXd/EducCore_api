package com.EducCore.EduCore.domain.User;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "user_groups")
@Entity(name = "UserGroup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_user", nullable = false)  // ✅ era user_id
    private Long userId;

    @Column(name = "id_group", nullable = false) // ✅ era grupo_id
    private Long grupoId;

    public UserGroup(Long userId, Long grupoId) {
        this.userId = userId;
        this.grupoId = grupoId;
    }
}