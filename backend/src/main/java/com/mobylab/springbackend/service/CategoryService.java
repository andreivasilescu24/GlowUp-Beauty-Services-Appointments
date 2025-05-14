package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Category;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.CategoryRepository;
import com.mobylab.springbackend.service.dto.category.CategoryDto;
import com.mobylab.springbackend.service.dto.category.CreateCategoryDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        return categories.stream().map(category ->
                        new CategoryDto()
                                .setId(category.getId())
                                .setName(category.getName())).toList();
    }


    public CategoryDto addCategory(CreateCategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        categoryRepository.save(category);

        return new CategoryDto()
                .setId(category.getId())
                .setName(category.getName());
    }

    public void deleteCategory(UUID categoryId) {
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            this.categoryRepository.delete(category.get());
        } else {
            throw new ResourceNotFoundException("Category not found");
        }
    }

    public CategoryDto updateCategory(UUID categoryId, CreateCategoryDto categoryDto) {
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            Category existingCategory = category.get();
            existingCategory.setName(categoryDto.getName());
            categoryRepository.save(existingCategory);

            return new CategoryDto()
                    .setId(existingCategory.getId())
                    .setName(existingCategory.getName());
        } else {
            throw new ResourceNotFoundException("Category not found");
        }
    }
}
