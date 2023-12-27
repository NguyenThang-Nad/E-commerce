package com.javadev.shopaap.service;

import com.javadev.shopaap.dto.CategoryDTO;
import com.javadev.shopaap.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO   getCategoryById(Long id);
    List<CategoryEntity> getAllCategories();
    CategoryDTO updateCategory(Long id,CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
