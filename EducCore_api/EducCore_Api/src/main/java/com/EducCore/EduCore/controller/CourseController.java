package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Course.Course;
import com.EducCore.EduCore.repositories.CourseRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
@Tag(name = "Cursos", description = "Endpoints para gerenciamento de cursos")
public class CourseController {

    @Autowired
    private CourseRepository repository;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final String supabaseUrl = "https://ofwqrpbwkgevkyyfsnox.supabase.co";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.ok(repository.findAllByOrderByCreatedAtDesc());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createCourse(
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("workload") String workload,
            @RequestParam("price") String price,
            @RequestParam("status") String status,
            @RequestParam(value = "cover", required = false) MultipartFile cover
    ) {
        try {
            Course course = new Course();
            course.setTitle(title);
            course.setCategory(category);
            course.setDescription(description);
            course.setWorkload(workload);
            course.setPrice(price);

            // Converte o status do switch/string para boolean
            course.setStatus("true".equalsIgnoreCase(status) || "Ativo".equalsIgnoreCase(status));

            if (cover != null && !cover.isEmpty()) {
                String publicImageUrl = uploadImageToSupabase(cover);
                if (publicImageUrl != null) {
                    course.setCover(publicImageUrl);
                }
            }

            return ResponseEntity.ok(repository.save(course));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar curso: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateCourse(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("workload") String workload,
            @RequestParam("price") String price,
            @RequestParam("status") String status,
            @RequestParam(value = "cover", required = false) MultipartFile cover
    ) {
        try {
            Optional<Course> optionalCourse = repository.findById(id);
            if (optionalCourse.isEmpty()) return ResponseEntity.notFound().build();

            Course course = optionalCourse.get();
            course.setTitle(title);
            course.setCategory(category);
            course.setDescription(description);
            course.setWorkload(workload);
            course.setPrice(price);
            course.setStatus("true".equalsIgnoreCase(status) || "Ativo".equalsIgnoreCase(status));

            if (cover != null && !cover.isEmpty()) {
                String publicImageUrl = uploadImageToSupabase(cover);
                if (publicImageUrl != null) course.setCover(publicImageUrl);
            }

            return ResponseEntity.ok(repository.save(course));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar curso: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable("id") Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Curso deletado com sucesso\"}");
    }

    // Método de Upload ajustado para o bucket 'Course' conforme sua imagem
    private String uploadImageToSupabase(MultipartFile image) {
        try {
            String extension = ".jpg";
            if (image.getOriginalFilename() != null && image.getOriginalFilename().contains(".")) {
                extension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // URL para upload (conforme imagem do storage que você enviou: bucket 'Course')
            String uploadUrl = supabaseUrl + "/storage/v1/object/Course/" + uniqueFilename;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);
            headers.setContentType(MediaType.parseMediaType(image.getContentType()));

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(image.getBytes(), headers);

            ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Retorna a URL pública para o campo 'cover'
                return supabaseUrl + "/storage/v1/object/public/Course/" + uniqueFilename;
            }
        } catch (Exception e) {
            System.err.println("Erro no upload: " + e.getMessage());
        }
        return null;
    }
}