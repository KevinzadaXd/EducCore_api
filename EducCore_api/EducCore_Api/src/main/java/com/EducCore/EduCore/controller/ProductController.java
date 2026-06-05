package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.Course.Course;
import com.EducCore.EduCore.domain.Product.CourseProduct;
import com.EducCore.EduCore.domain.Product.Product;
import com.EducCore.EduCore.domain.Product.ProductType;
import com.EducCore.EduCore.repositories.CourseRepository;
import com.EducCore.EduCore.repositories.CourseProductRepository;
import com.EducCore.EduCore.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseProductRepository courseProductRepository;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private String uploadImageToSupabase(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) return null;

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String uploadUrl = supabaseUrl + "/storage/v1/object/product/" + fileName;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.parseMediaType(
                file.getContentType() != null ? file.getContentType() : "image/jpeg"
        ));

        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
        restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);

        return supabaseUrl + "/storage/v1/object/public/product/" + fileName;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            List<Map<String, Object>> response = products.stream()
                    .map(this::toMap)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(p -> ResponseEntity.ok(toMap(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductRequestDTO dto = mapper.readValue(productJson, ProductRequestDTO.class);

            Product product = new Product();
            product.setName(dto.name);
            product.setPrice(dto.price);
            product.setDiscount(dto.discount);
            product.setCategory(dto.category != null ? dto.category : "Geral");
            product.setType(dto.type != null ? ProductType.valueOf(dto.type.toUpperCase()) : ProductType.STANDARD);
            product.setImageUrl(dto.imageUrl != null ? dto.imageUrl : "");

            if (file != null && !file.isEmpty()) {
                product.setImageUrl(uploadImageToSupabase(file));
            }

            Product saved = productRepository.saveAndFlush(product);

            if (dto.cursosIds != null) {
                for (Long courseId : dto.cursosIds) {
                    courseRepository.findById(courseId).ifPresent(course -> {
                        CourseProduct cp = new CourseProduct();
                        cp.setProduct(saved);
                        cp.setCourse(course);
                        courseProductRepository.save(cp);
                        saved.getCursos().add(cp);
                    });
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductRequestDTO dto = mapper.readValue(productJson, ProductRequestDTO.class);

            Optional<Product> productOpt = productRepository.findById(id);
            if (!productOpt.isPresent()) return ResponseEntity.notFound().build();

            Product product = productOpt.get();
            product.setName(dto.name);
            product.setPrice(dto.price);
            product.setDiscount(dto.discount);
            product.setCategory(dto.category != null ? dto.category : "Geral");
            product.setType(dto.type != null ? ProductType.valueOf(dto.type.toUpperCase()) : ProductType.STANDARD);

            if (file != null && !file.isEmpty()) {
                product.setImageUrl(uploadImageToSupabase(file));
            } else if (dto.imageUrl != null && !dto.imageUrl.isEmpty()) {
                product.setImageUrl(dto.imageUrl);
            }

            courseProductRepository.deleteByProductId(product.getId());
            product.getCursos().clear();
            productRepository.saveAndFlush(product);

            if (dto.cursosIds != null) {
                for (Long courseId : dto.cursosIds) {
                    courseRepository.findById(courseId).ifPresent(course -> {
                        CourseProduct cp = new CourseProduct();
                        cp.setProduct(product);
                        cp.setCourse(course);
                        courseProductRepository.save(cp);
                        product.getCursos().add(cp);
                    });
                }
            }

            return ResponseEntity.ok(toMap(productRepository.saveAndFlush(product)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            if (!productRepository.existsById(id)) return ResponseEntity.notFound().build();
            courseProductRepository.deleteByProductId(id);
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Map<String, Object> toMap(Product p) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", p.getId());
        map.put("name", p.getName());
        map.put("price", p.getPrice());
        map.put("discount", p.getDiscount());
        map.put("imageUrl", p.getImageUrl());
        map.put("category", p.getCategory());
        map.put("type", p.getType());
        map.put("cursosIds", p.getCursos().stream()
                .map(cp -> cp.getCourse().getId())
                .collect(Collectors.toList()));
        return map;
    }

    static class ProductRequestDTO {
        public String name;
        public String price;
        public String discount;
        public String imageUrl;
        public String category;
        public String type;
        public List<Long> cursosIds;
    }
}