package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Faq.Faq;
import com.EducCore.EduCore.domain.Faq.FaqRegisterDTO;
import com.EducCore.EduCore.repositories.FaqRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
@Tag(name = "FAQs", description = "Endpoints para gerenciamento de FAQs")
public class FaqController {

    @Autowired
    private FaqRepository repository;

    // CREATE
    @PostMapping
    public ResponseEntity<Faq> create(@RequestBody @Valid FaqRegisterDTO data) {
        Faq novoFaq = new Faq(data);
        repository.save(novoFaq);
        return ResponseEntity.ok(novoFaq);
    }

    // READ
    @GetMapping
    public ResponseEntity<List<Faq>> getAll() {
        var faqs = repository.findAll();
        return ResponseEntity.ok(faqs);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Faq> update(@PathVariable Long id, @RequestBody @Valid FaqRegisterDTO data) {
        var faqOpt = repository.findById(id);

        if (faqOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Faq faq = faqOpt.get();
        faq.updateFromDTO(data);
        repository.save(faq);

        return ResponseEntity.ok(faq);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
