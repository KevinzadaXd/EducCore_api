package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Empresa.About;
import com.EducCore.EduCore.domain.Empresa.Page;
import com.EducCore.EduCore.repositories.PageRepository;
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
@RequestMapping("/api/about")
@CrossOrigin(origins = "*")
@Tag(name = "About", description = "Endpoints para gerenciamento da página institucional Sobre Nós")
public class AboutController {

    @Autowired
    private PageRepository pageRepository;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final String supabaseUrl = "https://ofwqrpbwkgevkyyfsnox.supabase.co";
    private final RestTemplate restTemplate = new RestTemplate();

    // ─── GET: Busca os dados da página Sobre (ID 1) ───────────────────────────
    @GetMapping
    public ResponseEntity<Page> getAboutPage() {
        Optional<Page> page = pageRepository.findById(1L);
        return page.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ─── PUT: Atualiza textos e faz upload no bucket 'about' ──────────────────
    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateAboutPage(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("status") String status,
            @RequestParam(value = "current_image_url", required = false) String currentImageUrl,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            Optional<Page> pageOptional = pageRepository.findById(1L);
            if (pageOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Página institucional não encontrada.");
            }

            Page page = pageOptional.get();
            page.setName(name);
            page.setDescription(description);
            page.setStatus(
                    "Publicado".equalsIgnoreCase(status) ||
                            "Ativo".equalsIgnoreCase(status) ||
                            "true".equalsIgnoreCase(status)
            );

            String finalImageUrl = currentImageUrl;

            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadedUrl = uploadImageToSupabaseAbout(imageFile);
                if (uploadedUrl != null) {
                    finalImageUrl = uploadedUrl;
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Falha ao subir imagem para o Supabase Storage.");
                }
            }

            if (page.getAbout() == null) {
                About about = new About();
                about.setImageUrl(finalImageUrl);
                about.setPage(page);
                page.setAbout(about);
            } else {
                page.getAbout().setImageUrl(finalImageUrl);
            }

            Page updatedPage = pageRepository.save(page);
            return ResponseEntity.ok(updatedPage);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar dados da página Sobre: " + e.getMessage());
        }
    }

    private String uploadImageToSupabaseAbout(MultipartFile image) {
        try {
            String originalFilename = image.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            String uploadUrl = supabaseUrl + "/storage/v1/object/about/" + uniqueFilename;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);
            headers.setContentType(MediaType.parseMediaType(image.getContentType()));

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(image.getBytes(), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl, HttpMethod.POST, requestEntity, String.class
            );

            if (response.getStatusCode() == HttpStatus.OK ||
                    response.getStatusCode() == HttpStatus.CREATED) {
                return supabaseUrl + "/storage/v1/object/public/about/" + uniqueFilename;
            }
        } catch (Exception e) {
            System.err.println("Erro no upload para o Supabase Storage: " + e.getMessage());
        }
        return null;
    }
}