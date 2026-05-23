package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Professor.Teacher;
import com.EducCore.EduCore.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/professor")
@CrossOrigin(origins = "*") // Garante que o Next.js consiga se comunicar sem bloqueio de CORS
public class TeacherController {

    @Autowired
    private TeacherRepository repository;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final String supabaseUrl = "https://ofwqrpbwkgevkyyfsnox.supabase.co";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<?> getAllTeachers() {
        return ResponseEntity.ok(repository.findAllByOrderByPositionAsc());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createTeacher(
            @RequestParam("name") String name,
            @RequestParam("position") Long order,
            @RequestParam("status") String status,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            Teacher teacher = new Teacher();
            teacher.setName(name);
            teacher.setPosition(order);
            teacher.setDescription(description);

            if ("Publicado".equalsIgnoreCase(status) || "Ativo".equalsIgnoreCase(status) || "true".equalsIgnoreCase(status)) {
                teacher.setStatus(true);
            } else {
                teacher.setStatus(false);
            }

            if (image != null && !image.isEmpty()) {
                String publicImageUrl = uploadImageToSupabase(image);
                if (publicImageUrl != null) {
                    teacher.setImageUrl(publicImageUrl);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao subir imagem para o storage.");
                }
            }

            Teacher savedTeacher = repository.save(teacher);
            return ResponseEntity.ok(savedTeacher);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro interno: " + e.getMessage());
        }
    }

    // 📝 NOVA ROTA: EDITAR PROFESSOR (PUT)
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateTeacher(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("position") Long order,
            @RequestParam("status") String status,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            Optional<Teacher> optionalTeacher = repository.findById(id);
            if (optionalTeacher.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Professor não encontrado.");
            }

            Teacher teacher = optionalTeacher.get();
            teacher.setName(name);
            teacher.setPosition(order);
            teacher.setDescription(description);

            if ("Publicado".equalsIgnoreCase(status) || "Ativo".equalsIgnoreCase(status) || "true".equalsIgnoreCase(status)) {
                teacher.setStatus(true);
            } else {
                teacher.setStatus(false);
            }

            // Se uma nova imagem foi enviada, fazemos o upload e substituímos a antiga URL
            if (image != null && !image.isEmpty()) {
                String publicImageUrl = uploadImageToSupabase(image);
                if (publicImageUrl != null) {
                    teacher.setImageUrl(publicImageUrl);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao subir nova imagem.");
                }
            }

            Teacher updatedTeacher = repository.save(teacher);
            return ResponseEntity.ok(updatedTeacher);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao atualizar: " + e.getMessage());
        }
    }

    // ❌ NOVA ROTA: DELETAR PROFESSOR (DELETE)
    // ❌ ROTA PARA DELETAR PROFESSOR (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable("id") Long id) {
        try {
            System.out.println("[Java Controller] Tentando deletar professor com ID: " + id);

            if (!repository.existsById(id)) {
                System.out.println("[Java Controller] ID " + id + " não existe no banco.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Professor com o ID " + id + " não foi encontrado no banco de dados.");
            }

            repository.deleteById(id);
            System.out.println("[Java Controller] ID " + id + " deletado com sucesso!");

            // Retornamos um JSON simples para evitar que o Next.js reclame de corpo vazio
            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Deletado com sucesso\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao deletar no banco de dados: " + e.getMessage());
        }
    }

    // Método auxiliar isolado para não duplicar código de upload de imagem no Supabase
    private String uploadImageToSupabase(MultipartFile image) {
        try {
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            String uploadUrl = supabaseUrl + "/storage/v1/object/teacher/" + uniqueFilename;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);
            headers.setContentType(MediaType.parseMediaType(image.getContentType()));

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(image.getBytes(), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl, HttpMethod.POST, requestEntity, String.class
            );

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                return supabaseUrl + "/storage/v1/object/public/teacher/" + uniqueFilename;
            }
        } catch (Exception e) {
            System.err.println("Erro no upload do Supabase Storage: " + e.getMessage());
        }
        return null;
    }
}