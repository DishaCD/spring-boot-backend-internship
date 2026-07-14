package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.BookSummaryDTO;
import com.example.librarymanagement.dto.CategoryDTO;
import com.example.librarymanagement.dto.CategorySummaryDTO;
import com.example.librarymanagement.entity.Book;
import com.example.librarymanagement.entity.Category;
import com.example.librarymanagement.exception.DuplicateResourceException;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToDTO(category);
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new DuplicateResourceException("Category already exists with name: " + categoryDTO.getName());
        }
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        if (categoryDTO.getName() != null && !categoryDTO.getName().equals(category.getName())) {
            if (categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
                throw new DuplicateResourceException("Category already exists with name: " + categoryDTO.getName());
            }
            category.setName(categoryDTO.getName());
        }
        if (categoryDTO.getDescription() != null) {
            category.setDescription(categoryDTO.getDescription());
        }
        Category updatedCategory = categoryRepository.save(category);
        return mapToDTO(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(Category category) {
        List<BookSummaryDTO> bookSummaries = category.getBooks() != null ?
                category.getBooks().stream().map(this::mapBookToSummary).collect(Collectors.toList()) : List.of();
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .books(bookSummaries)
                .build();
    }

    private BookSummaryDTO mapBookToSummary(Book book) {
        return BookSummaryDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publicationYear(book.getPublicationYear())
                .availableCopies(book.getAvailableCopies())
                .totalCopies(book.getTotalCopies())
                .categoryName(book.getCategory() != null ? book.getCategory().getName() : null)
                .build();
    }

    public static CategorySummaryDTO mapToSummary(Category category) {
        return CategorySummaryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
