package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Empresa.Empresa;
import com.EducCore.EduCore.domain.Empresa.EmpresaRegisterDTO;
import com.EducCore.EduCore.repositories.EmpresaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("empresa")
@Tag(name = "Empresas", description = "Endpoints para gerenciamento de empresas/escolas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository repository;

    @PostMapping
    public ResponseEntity<Empresa> create(@RequestBody @Valid EmpresaRegisterDTO data) {
        Empresa novaEmpresa = new Empresa(data);
        repository.save(novaEmpresa);
        
        return ResponseEntity.ok(novaEmpresa);
    }
    
    @GetMapping
    public ResponseEntity<List<Empresa>> getAll() {
        var empresas = repository.findAll();
        return ResponseEntity.ok(empresas);
    }
}
