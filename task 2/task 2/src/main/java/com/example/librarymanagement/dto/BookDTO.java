package com.example.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Integer availableCopies;
    private Integer totalCopies;
    private AuthorSummaryDTO author;
    private CategorySummaryDTO category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
