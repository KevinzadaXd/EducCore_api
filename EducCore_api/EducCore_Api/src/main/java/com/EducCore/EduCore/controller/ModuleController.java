package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Module.Module;
import com.EducCore.EduCore.domain.Module.ModuleRegisterDTO;
import com.EducCore.EduCore.repositories.ModuleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/modules")
@CrossOrigin(origins = "*")
@Tag(name = "Módulos", description = "Endpoints para gerenciamento de módulos de conteúdo")
public class ModuleController {

    @Autowired
    private ModuleRepository repository;

    @GetMapping
    @Operation(summary = "Lista todos os módulos ordenados por posição")
    public ResponseEntity<List<Module>> getAll() {
        return ResponseEntity.ok(repository.findAllByOrderByOrderAsc());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um módulo por ID")
    public ResponseEntity<Module> getById(@PathVariable Long id) {
        Optional<Module> module = repository.findById(id);
        return module.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Cria um novo módulo")
    public ResponseEntity<?> create(@RequestBody @Valid ModuleRegisterDTO data) {
        try {
            Module module = new Module();
            module.setName(data.name());
            module.setOrder(data.order());
            return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(module));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar módulo: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um módulo existente")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ModuleRegisterDTO data) {
        Optional<Module> optional = repository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Module module = optional.get();
        module.setName(data.name());
        module.setOrder(data.order());

        return ResponseEntity.ok(repository.save(module));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um módulo por ID")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Módulo com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
        return ResponseEntity.ok("{\"success\": true, \"message\": \"Módulo deletado com sucesso\"}");
    }
}
