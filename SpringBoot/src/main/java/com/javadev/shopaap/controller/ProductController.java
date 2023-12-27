package com.javadev.shopaap.controller;

import com.github.javafaker.Faker;
import com.javadev.shopaap.dto.ProductDTO;
import com.javadev.shopaap.dto.ProductImageDTO;
import com.javadev.shopaap.dto.responses.ProductListResponse;
import com.javadev.shopaap.dto.responses.ProductResponse;
import com.javadev.shopaap.entity.ProductEntity;
import com.javadev.shopaap.entity.ProductImageEntity;
import com.javadev.shopaap.repositories.ProductRepository;
import com.javadev.shopaap.service.ProductService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "${api.prefix}/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;
    @PostMapping(value = "")

    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            ProductEntity newProduct= productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //POST http://localhost:8088/v1/api/products
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ){
        try {
            ProductEntity existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
//            if(files.size() > ProductImageEntity.MAXIMUM_IMAGES_PER_PRODUCT) {
//                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
//            }
            List<ProductImageEntity> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if(file.getSize() == 0) {
                    continue;
                }
                if(file.getSize() > 10 * 1024 * 1024)
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");

                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/"))
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                String filename = storeFile(file);
                ProductImageDTO productImageDTO = new ProductImageDTO();
                productImageDTO.setImageUrl(filename);
                ProductImageEntity productImage = productService.createImage(
                        existingProduct.getId(),
                        productImageDTO);

                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir))
            Files.createDirectories(uploadDir);
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page")     int page,
            @RequestParam("limit")    int limit
    ) {
        PageRequest pageRequest=PageRequest.of(page,limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productEntities=productService.getAllProducts(pageRequest);
        int totalPages=productEntities.getTotalPages();
        List<ProductResponse> products=productEntities.getContent();

        ProductListResponse productListResponse = new ProductListResponse();
        productListResponse.setProducts(products);
        productListResponse.setTotalPages(totalPages);

        return ResponseEntity.ok(productListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") Long productId
    ) {
        try {
            ProductEntity existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("generateFakeProduct")
    private ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();

        for (int i = 0; i < 10_000; i++) {
            String productName = faker.commerce().productName() + "-" + UUID.randomUUID().toString();
            if (productService.exitsByName(productName)) {
                continue;
            }

            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(productName);
            productDTO.setPrice((float) faker.number().numberBetween(10, 90_000_000));
            productDTO.setDescription(faker.lorem().sentence());
            productDTO.setThumbnail("");
            productDTO.setCategoryId((long) faker.number().numberBetween(1, 4));

            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                System.err.println("Error creating product: " + e.getMessage());
            }
        }

        return ResponseEntity.ok( " Fake Products created successfully");
    }


}
