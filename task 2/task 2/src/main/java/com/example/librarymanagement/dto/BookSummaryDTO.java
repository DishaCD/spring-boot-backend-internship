package com.example.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookSummaryDTO {
    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Integer availableCopies;
    private Integer totalCopies;
    private String categoryName;
}
