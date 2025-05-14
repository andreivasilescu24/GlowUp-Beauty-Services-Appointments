package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.entity.Category;
import com.mobylab.springbackend.service.CategoryService;
import com.mobylab.springbackend.service.dto.category.CategoryDto;
import com.mobylab.springbackend.service.dto.category.CreateCategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController implements SecuredRestController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = this.categoryService.getAllCategories();
        return ResponseEntity.status(200).body(categories);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(CreateCategoryDto categoryDto) {
        CategoryDto createdCategory = this.categoryService.addCategory(categoryDto);
        return ResponseEntity.status(201).body(createdCategory);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID categoryId) {
        this.categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID categoryId, @RequestBody CreateCategoryDto categoryDto) {
        CategoryDto updatedCategory = this.categoryService.updateCategory(categoryId, categoryDto);
        return ResponseEntity.status(200).body(updatedCategory);
    }
}
