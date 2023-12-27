package com.javadev.shopaap.service.impl;

import com.javadev.shopaap.dto.ProductDTO;
import com.javadev.shopaap.dto.ProductImageDTO;
import com.javadev.shopaap.dto.responses.ProductResponse;
import com.javadev.shopaap.entity.CategoryEntity;
import com.javadev.shopaap.entity.ProductEntity;
import com.javadev.shopaap.entity.ProductImageEntity;
import com.javadev.shopaap.exception.DataNotFoundException;
import com.javadev.shopaap.exception.ImageException;
import com.javadev.shopaap.repositories.CategoryRepository;
import com.javadev.shopaap.repositories.ProductImageRepository;
import com.javadev.shopaap.repositories.ProductRepository;
import com.javadev.shopaap.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public ProductEntity createProduct(ProductDTO productDTO) throws DataNotFoundException {
        CategoryEntity exitingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category not found id :" + productDTO.getCategoryId()));
        ProductEntity newProduct = new ProductEntity();
        newProduct.setName(productDTO.getName());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setThumbnail(productDTO.getThumbnail());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setCategoryId(exitingCategory);

        return productRepository.save(newProduct);
    }

    @Override
    public ProductEntity getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Can not find products with id :" + id));
    }
    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        Page<ProductEntity> productEntity = productRepository.findAll(pageRequest);
        return productEntity.map(entity ->
                modelMapper.map(entity, ProductResponse.class));
    }

    @Override
    public ProductEntity updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        ProductEntity exitingProduct = getProductById(id);
        CategoryEntity exitingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category not found id :" + productDTO.getCategoryId()));
        exitingProduct.setName(productDTO.getName());
        exitingProduct.setCategoryId(exitingCategory);
        exitingProduct.setPrice(productDTO.getPrice());
        exitingProduct.setDescription(productDTO.getDescription());
        exitingProduct.setThumbnail(productDTO.getThumbnail());

        return productRepository.save(exitingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if (productEntity.isPresent()) productRepository.deleteById(id);
    }

    @Override
    public boolean exitsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImageEntity createImage(Long id, ProductImageDTO productImageDTO) throws Exception {
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found id :" + id));
        ProductImageEntity newProductImage = new ProductImageEntity();
        newProductImage.setProduct(existingProduct);
        newProductImage.setImageUrl(productImageDTO.getImageUrl());
        int size = productImageRepository.findByProductId(id).size();
        if (size >= 5)
            throw new ImageException("Number image must be <= 5");

        return productImageRepository.save(newProductImage);
    }
}
