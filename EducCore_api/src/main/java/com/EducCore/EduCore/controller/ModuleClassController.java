package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Lesson.Lesson;
import com.EducCore.EduCore.domain.Module.Module;
import com.EducCore.EduCore.domain.ModuleClass.ModuleClass;
import com.EducCore.EduCore.domain.ModuleClass.ModuleClassRegisterDTO;
import com.EducCore.EduCore.repositories.LessonRepository;
import com.EducCore.EduCore.repositories.ModuleClassRepository;
import com.EducCore.EduCore.repositories.ModuleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/module-class")
@CrossOrigin(origins = "*")
@Tag(name = "Módulo-Aula", description = "Endpoints para vincular e desvincular aulas a módulos")
public class ModuleClassController {

    @Autowired
    private ModuleClassRepository repository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping
    @Operation(summary = "Lista todos os vínculos módulo-aula")
    public ResponseEntity<List<ModuleClass>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/module/{idModule}")
    @Operation(summary = "Lista todas as aulas de um módulo específico")
    public ResponseEntity<?> getByModule(@PathVariable Long idModule) {
        if (!moduleRepository.existsById(idModule)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Módulo com ID " + idModule + " não encontrado.");
        }
        return ResponseEntity.ok(repository.findByIdModule(idModule));
    }

    @GetMapping("/class/{idClass}")
    @Operation(summary = "Lista todos os módulos que contêm uma aula específica")
    public ResponseEntity<?> getByClass(@PathVariable Long idClass) {
        if (!lessonRepository.existsById(idClass)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aula com ID " + idClass + " não encontrada.");
        }
        return ResponseEntity.ok(repository.findByIdClass(idClass));
    }

    @PostMapping
    @Operation(summary = "Vincula uma aula a um módulo")
    public ResponseEntity<?> create(@RequestBody @Valid ModuleClassRegisterDTO data) {
        // Verifica se o módulo existe
        Optional<Module> moduleOpt = moduleRepository.findById(data.idModule());
        if (moduleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Módulo com ID " + data.idModule() + " não encontrado.");
        }

        // Verifica se a aula existe
        Optional<Lesson> lessonOpt = lessonRepository.findById(data.idClass());
        if (lessonOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aula com ID " + data.idClass() + " não encontrada.");
        }

        // Verifica se o vínculo já existe (evita duplicata)
        if (repository.existsByIdModuleAndIdClass(data.idModule(), data.idClass())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Esta aula já está vinculada a este módulo.");
        }

        ModuleClass moduleClass = new ModuleClass();
        moduleClass.setIdModule(data.idModule());
        moduleClass.setIdClass(data.idClass());
        moduleClass.setNameModule(moduleOpt.get().getName());
        moduleClass.setNameClass(lessonOpt.get().getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(moduleClass));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um vínculo módulo-aula pelo ID do vínculo")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vínculo com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
        return ResponseEntity.ok("{\"success\": true, \"message\": \"Vínculo removido com sucesso\"}");
    }

    @DeleteMapping("/unlink")
    @Transactional
    @Operation(summary = "Desvincula uma aula de um módulo pelo idModule e idClass")
    public ResponseEntity<?> unlink(@RequestParam Long idModule, @RequestParam Long idClass) {
        if (!repository.existsByIdModuleAndIdClass(idModule, idClass)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vínculo entre módulo " + idModule + " e aula " + idClass + " não encontrado.");
        }
        repository.deleteByIdModuleAndIdClass(idModule, idClass);
        return ResponseEntity.ok("{\"success\": true, \"message\": \"Vínculo removido com sucesso\"}");
    }
}
