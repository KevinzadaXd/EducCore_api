package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Course.Course;
import com.EducCore.EduCore.domain.Module.Module;
import com.EducCore.EduCore.domain.ModuleCourse.ModuleCourse;
import com.EducCore.EduCore.domain.ModuleCourse.ModuleCourseRegisterDTO;
import com.EducCore.EduCore.repositories.CourseRepository;
import com.EducCore.EduCore.repositories.ModuleCourseRepository;
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
@RequestMapping("/api/module-course")
@CrossOrigin(origins = "*")
@Tag(name = "Módulo-Curso", description = "Endpoints para vincular e desvincular módulos a cursos")
public class ModuleCourseController {

    @Autowired
    private ModuleCourseRepository repository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    @Operation(summary = "Lista todos os vínculos módulo-curso")
    public ResponseEntity<List<ModuleCourse>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/course/{idCourse}")
    @Operation(summary = "Lista todos os módulos vinculados a um curso")
    public ResponseEntity<?> getByCourse(@PathVariable Long idCourse) {
        if (!courseRepository.existsById(idCourse)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Curso com ID " + idCourse + " não encontrado.");
        }
        return ResponseEntity.ok(repository.findByIdCourse(idCourse));
    }

    @PostMapping
    @Operation(summary = "Vincula um módulo a um curso")
    public ResponseEntity<?> create(@RequestBody @Valid ModuleCourseRegisterDTO data) {
        Optional<Module> moduleOpt = moduleRepository.findById(data.idModule());
        if (moduleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Módulo com ID " + data.idModule() + " não encontrado.");
        }

        Optional<Course> courseOpt = courseRepository.findById(data.idCourse());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Curso com ID " + data.idCourse() + " não encontrado.");
        }

        if (repository.existsByIdModuleAndIdCourse(data.idModule(), data.idCourse())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Este módulo já está vinculado a este curso.");
        }

        ModuleCourse moduleCourse = new ModuleCourse();
        moduleCourse.setIdModule(data.idModule());
        moduleCourse.setIdCourse(data.idCourse());
        moduleCourse.setNameModule(moduleOpt.get().getName());
        moduleCourse.setNameCourse(courseOpt.get().getTitle());

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(moduleCourse));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove vínculo módulo-curso pelo ID")
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
    @Operation(summary = "Desvincula um módulo de um curso pelo par idModule + idCourse")
    public ResponseEntity<?> unlink(
            @RequestParam Long idModule,
            @RequestParam Long idCourse
    ) {
        if (!repository.existsByIdModuleAndIdCourse(idModule, idCourse)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vínculo entre módulo " + idModule + " e curso " + idCourse + " não encontrado.");
        }
        repository.deleteByIdModuleAndIdCourse(idModule, idCourse);
        return ResponseEntity.ok("{\"success\": true, \"message\": \"Vínculo removido com sucesso\"}");
    }
}
