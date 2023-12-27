package com.javadev.shopaap.service.impl;

import com.javadev.shopaap.dto.CategoryDTO;
import com.javadev.shopaap.entity.CategoryEntity;
import com.javadev.shopaap.repositories.CategoryRepository;
import com.javadev.shopaap.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryImpl implements CategoryService {
@Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity =modelMapper.map(categoryDTO,CategoryEntity.class);
        CategoryEntity savedCategory=categoryRepository.save(categoryEntity);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
       Optional <CategoryEntity> findCategory = categoryRepository.findById(id);

        return modelMapper.map(findCategory,CategoryDTO.class);
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
    CategoryEntity existingCategory = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Not found"));
    existingCategory.setName(categoryDTO.getName());
    categoryRepository.save(existingCategory);

      return modelMapper.map(existingCategory,CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Long id) {
      categoryRepository.deleteById(id);
    }
}
