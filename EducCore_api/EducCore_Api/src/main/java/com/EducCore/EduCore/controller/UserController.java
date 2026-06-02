package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.User.User;
import com.EducCore.EduCore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE
})
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    // ✅ 1. READ ALL
    @GetMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<Long> grupos = entityManager
                    .createNativeQuery("SELECT id_group FROM user_groups WHERE id_user = ?")
                    .setParameter(1, user.getId())
                    .getResultList();
            user.setGruposIds(grupos != null ? grupos : new ArrayList<>());

            List<Long> cursos = entityManager
                    .createNativeQuery("SELECT id_course FROM course_user WHERE id_user = ?")
                    .setParameter(1, user.getId())
                    .getResultList();
            user.setCursosIds(cursos != null ? cursos : new ArrayList<>());
        }

        return ResponseEntity.ok(users);
    }

    // ✅ 2. READ BY ID
    @GetMapping("/{id}")
    @SuppressWarnings("unchecked")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()) return ResponseEntity.notFound().build();

        User user = optUser.get();

        List<Long> grupos = entityManager
                .createNativeQuery("SELECT id_group FROM user_groups WHERE id_user = ?")
                .setParameter(1, user.getId())
                .getResultList();
        user.setGruposIds(grupos != null ? grupos : new ArrayList<>());

        List<Long> cursos = entityManager
                .createNativeQuery("SELECT id_course FROM course_user WHERE id_user = ?")
                .setParameter(1, user.getId())
                .getResultList();
        user.setCursosIds(cursos != null ? cursos : new ArrayList<>());

        return ResponseEntity.ok(user);
    }

    // ✅ 3. CREATE
    @PostMapping
    @Transactional
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if (userRepository.findUserByLogin(user.getLogin()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"Este login já está em uso por outro usuário.\"}");
            }

            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"A senha é obrigatória para cadastrar um novo usuário.\"}");
            }

            List<Long> gruposParaSalvar = user.getGruposIds();
            List<Long> cursosParaSalvar = user.getCursosIds();

            // Limpa os @Transient antes de salvar (são ignorados pelo Hibernate de qualquer forma)
            user.setGruposIds(new ArrayList<>());
            user.setCursosIds(new ArrayList<>());

            User savedUser = userRepository.save(user);

            // ✅ Insere vínculos com colunas corretas do banco
            if (gruposParaSalvar != null) {
                for (Long grupoId : gruposParaSalvar) {
                    entityManager.createNativeQuery(
                                    "INSERT INTO user_groups (id_user, id_group) VALUES (?, ?)"
                            )
                            .setParameter(1, savedUser.getId())
                            .setParameter(2, grupoId)
                            .executeUpdate();
                }
            }

            if (cursosParaSalvar != null) {
                for (Long cursoId : cursosParaSalvar) {
                    entityManager.createNativeQuery(
                                    "INSERT INTO course_user (id_user, id_course) VALUES (?, ?)"
                            )
                            .setParameter(1, savedUser.getId())
                            .setParameter(2, cursoId)
                            .executeUpdate();
                }
            }

            savedUser.setGruposIds(gruposParaSalvar != null ? gruposParaSalvar : new ArrayList<>());
            savedUser.setCursosIds(cursosParaSalvar != null ? cursosParaSalvar : new ArrayList<>());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Erro ao salvar no banco de dados: " + e.getMessage() + "\"}");
        }
    }

    // ✅ 4. UPDATE
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            return userRepository.findById(id).map(user -> {

                User loginExistente = userRepository.findUserByLogin(userDetails.getLogin());
                if (loginExistente != null && !loginExistente.getId().equals(id)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("{\"error\": \"Este login já está em uso por outro usuário.\"}");
                }

                user.setName(userDetails.getName());
                user.setLogin(userDetails.getLogin());
                user.setTelephone(userDetails.getTelephone());
                user.setBirthDate(userDetails.getBirthDate());
                user.setRole(userDetails.getRole());

                if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
                    user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                }

                // ✅ Limpa vínculos antigos com colunas corretas
                entityManager.createNativeQuery(
                        "DELETE FROM user_groups WHERE id_user = ?"
                ).setParameter(1, user.getId()).executeUpdate();

                entityManager.createNativeQuery(
                        "DELETE FROM course_user WHERE id_user = ?"
                ).setParameter(1, user.getId()).executeUpdate();

                // ✅ Insere novos vínculos com colunas corretas
                if (userDetails.getGruposIds() != null) {
                    for (Long grupoId : userDetails.getGruposIds()) {
                        entityManager.createNativeQuery(
                                "INSERT INTO user_groups (id_user, id_group) VALUES (?, ?)"
                        ).setParameter(1, user.getId()).setParameter(2, grupoId).executeUpdate();
                    }
                    user.setGruposIds(userDetails.getGruposIds());
                } else {
                    user.setGruposIds(new ArrayList<>());
                }

                if (userDetails.getCursosIds() != null) {
                    for (Long cursoId : userDetails.getCursosIds()) {
                        entityManager.createNativeQuery(
                                "INSERT INTO course_user (id_user, id_course) VALUES (?, ?)"
                        ).setParameter(1, user.getId()).setParameter(2, cursoId).executeUpdate();
                    }
                    user.setCursosIds(userDetails.getCursosIds());
                } else {
                    user.setCursosIds(new ArrayList<>());
                }

                User updatedUser = userRepository.save(user);
                return ResponseEntity.ok(updatedUser);

            }).orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Erro ao atualizar dados no servidor: " + e.getMessage() + "\"}");
        }
    }

    // ✅ 5. DELETE
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        entityManager.createNativeQuery(
                "DELETE FROM user_groups WHERE id_user = ?"
        ).setParameter(1, id).executeUpdate();

        entityManager.createNativeQuery(
                "DELETE FROM course_user WHERE id_user = ?"
        ).setParameter(1, id).executeUpdate();

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}