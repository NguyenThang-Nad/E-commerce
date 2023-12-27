package com.javadev.shopaap.service;

import com.javadev.shopaap.dto.ProductDTO;
import com.javadev.shopaap.dto.ProductImageDTO;
import com.javadev.shopaap.dto.responses.ProductResponse;
import com.javadev.shopaap.entity.ProductEntity;
import com.javadev.shopaap.entity.ProductImageEntity;
import com.javadev.shopaap.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {
    ProductEntity createProduct(ProductDTO productDTO) throws DataNotFoundException;
    ProductEntity getProductById(Long id) throws DataNotFoundException;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    ProductEntity updateProduct(Long id,ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(Long id);
    boolean exitsByName(String name);
    ProductImageEntity createImage(Long id, ProductImageDTO productImageDTO) throws Exception;

}
