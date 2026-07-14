package com.example.librarymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;
    
    @NotBlank(message = "Category name must not be blank")
    @Size(max = 100)
    private String name;
    
    private String description;
    
    private List<BookSummaryDTO> books;
}
