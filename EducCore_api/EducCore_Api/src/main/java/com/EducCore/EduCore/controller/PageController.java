package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Empresa.Page;
import com.EducCore.EduCore.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pages")
@CrossOrigin(origins = "*")
public class PageController {

    @Autowired
    private PageRepository repository;

    // 🔍 BUSCAR TODAS AS PÁGINAS
    @GetMapping
    public ResponseEntity<List<Page>> getAllPages() {
        try {
            List<Page> pages = repository.findAll();
            return ResponseEntity.ok(pages);
        } catch (Exception e) {
            System.err.println("Erro ao listar páginas: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    // 🔍 BUSCAR UMA PÁGINA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Page> getPageById(@PathVariable Long id) {
        Optional<Page> page = repository.findById(id);
        return page.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ➕ CRIAR NOVA PÁGINA
    @PostMapping
    public ResponseEntity<Page> createPage(@RequestBody Page pageData) {
        Page newPage = repository.save(pageData);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPage);
    }

    // 📝 EDITAR PÁGINA EXISTENTE
    @PutMapping("/{id}")
    public ResponseEntity<Page> updatePage(@PathVariable Long id, @RequestBody Page pageData) {
        Optional<Page> optionalPage = repository.findById(id);

        if (optionalPage.isPresent()) {
            Page existingPage = optionalPage.get();
            existingPage.setName(pageData.getName());
            existingPage.setOrder(pageData.getOrder());
            existingPage.setStatus(pageData.getStatus());
            existingPage.setDescription(pageData.getDescription());

            Page updatedPage = repository.save(existingPage);
            return ResponseEntity.ok(updatedPage);
        }

        return ResponseEntity.notFound().build();
    }

    // ❌ EXCLUIR PÁGINA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePage(@PathVariable Long id) {
        Optional<Page> optionalPage = repository.findById(id);

        if (optionalPage.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}