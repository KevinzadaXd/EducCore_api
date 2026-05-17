package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.User.Banner;
import com.EducCore.EduCore.repositories.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/banners")
@CrossOrigin(origins = "*") // Garante a comunicação direta se necessário, igual ao de professores
@Tag(name = "Banners", description = "Endpoints para gerenciamento de banners da escola")
public class BannerController {

    @Autowired
    private BannerRepository repository;

    @Value("${supabase.key}")
    private String supabaseKey;

    // Utilizando a mesma URL base do seu projeto Supabase
    private final String supabaseUrl = "https://ofwqrpbwkgevkyyfsnox.supabase.co";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<?> getAllBanners() {
        return ResponseEntity.ok(repository.findAllByOrderByPosicaoAsc());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createBanner(
            @RequestParam("nome") String nome,
            @RequestParam("posicao") Long posicao,
            @RequestParam("status") String status,
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem
    ) {
        try {
            Banner banner = new Banner();
            banner.setTitle(nome);
            banner.setPosicao(posicao);
            banner.setDataInicio(dataInicio);
            banner.setDataFim(dataFim);

            if ("Ativo".equalsIgnoreCase(status) || "Publicado".equalsIgnoreCase(status) || "true".equalsIgnoreCase(status)) {
                banner.setStatus(true);
            } else {
                banner.setStatus(false);
            }

            if (imagem != null && !imagem.isEmpty()) {
                String publicImageUrl = uploadImageToSupabase(imagem);
                if (publicImageUrl != null) {
                    banner.setImageurl(publicImageUrl);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao subir imagem do banner para o storage.");
                }
            }

            Banner savedBanner = repository.save(banner);
            return ResponseEntity.ok(savedBanner);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro interno ao criar banner: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateBanner(
            @PathVariable("id") Long id,
            @RequestParam("nome") String nome,
            @RequestParam("posicao") Long posicao,
            @RequestParam("status") String status,
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem
    ) {
        try {
            Optional<Banner> optionalBanner = repository.findById(id);
            if (optionalBanner.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Banner não encontrado.");
            }

            Banner banner = optionalBanner.get();
            banner.setTitle(nome);
            banner.setPosicao(posicao);
            banner.setDataInicio(dataInicio);
            banner.setDataFim(dataFim);

            if ("Ativo".equalsIgnoreCase(status) || "Publicado".equalsIgnoreCase(status) || "true".equalsIgnoreCase(status)) {
                banner.setStatus(true);
            } else {
                banner.setStatus(false);
            }

            // Altera a imagem apenas se um novo arquivo foi enviado pelo modal de edição
            if (imagem != null && !imagem.isEmpty()) {
                String publicImageUrl = uploadImageToSupabase(imagem);
                if (publicImageUrl != null) {
                    banner.setImageurl(publicImageUrl);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao atualizar imagem do banner.");
                }
            }

            Banner updatedBanner = repository.save(banner);
            return ResponseEntity.ok(updatedBanner);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao atualizar banner: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable("id") Long id) {
        try {
            System.out.println("[Banner Controller] Tentando deletar banner com ID: " + id);

            if (!repository.existsById(id)) {
                System.out.println("[Banner Controller] ID " + id + " não existe no banco.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Banner com o ID " + id + " não foi encontrado no banco de dados.");
            }

            repository.deleteById(id);
            System.out.println("[Banner Controller] ID " + id + " deletado com sucesso!");

            // Estrutura de retorno em JSON idêntica ao que você implementou em professores
            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Banner deletado com sucesso\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao deletar banner no banco de dados: " + e.getMessage());
        }
    }

    // Método auxiliar isolado configurado para a pasta /banner dentro do bucket do Supabase
    private String uploadImageToSupabase(MultipartFile image) {
        try {
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // Aponta para a pasta /banner/ no storage do Supabase
            String uploadUrl = supabaseUrl + "/storage/v1/object/banner/" + uniqueFilename;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);
            headers.setContentType(MediaType.parseMediaType(image.getContentType()));

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(image.getBytes(), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl, HttpMethod.POST, requestEntity, String.class
            );

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                return supabaseUrl + "/storage/v1/object/public/banner/" + uniqueFilename;
            }
        } catch (Exception e) {
            System.err.println("Erro no upload do Banner para o Supabase Storage: " + e.getMessage());
        }
        return null;
    }
}