package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Lesson.Lesson;
import com.EducCore.EduCore.domain.Lesson.LessonRegisterDTO;
import com.EducCore.EduCore.repositories.LessonRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*")
@Tag(name = "Aulas", description = "Endpoints para gerenciamento de aulas")
public class LessonController {

    @Autowired
    private LessonRepository repository;

    @GetMapping
    @Operation(summary = "Lista todas as aulas ordenadas por posição")
    public ResponseEntity<List<Lesson>> getAll() {
        return ResponseEntity.ok(repository.findAllByOrderByOrderAsc());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma aula por ID")
    public ResponseEntity<Lesson> getById(@PathVariable Long id) {
        Optional<Lesson> lesson = repository.findById(id);
        return lesson.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Cria uma nova aula")
    public ResponseEntity<?> create(@RequestBody LessonRegisterDTO data) {
        try {
            Lesson lesson = new Lesson();
            lesson.setName(data.name());
            lesson.setDescription(data.description());
            lesson.setVideoUrl(data.videoUrl());
            lesson.setOrder(data.order());
            lesson.setStatus(data.status() != null ? data.status() : false);
            return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(lesson));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar aula: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma aula existente")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody LessonRegisterDTO data) {
        Optional<Lesson> optional = repository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Lesson lesson = optional.get();

        if (data.name() != null) lesson.setName(data.name());
        if (data.description() != null) lesson.setDescription(data.description());
        if (data.videoUrl() != null) lesson.setVideoUrl(data.videoUrl());
        if (data.order() != null) lesson.setOrder(data.order());
        if (data.status() != null) lesson.setStatus(data.status());

        return ResponseEntity.ok(repository.save(lesson));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma aula por ID")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aula com ID " + id + " não encontrada.");
        }
        repository.deleteById(id);
        return ResponseEntity.ok("{\"success\": true, \"message\": \"Aula deletada com sucesso\"}");
    }
}
