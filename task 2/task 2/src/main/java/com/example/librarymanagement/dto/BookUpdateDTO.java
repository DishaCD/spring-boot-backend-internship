package com.example.librarymanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookUpdateDTO {

    @Size(max = 255)
    private String title;

    @Size(max = 20)
    private String isbn;

    private Integer publicationYear;

    @Min(value = 0, message = "Available copies cannot be negative")
    private Integer availableCopies;

    @Min(value = 0, message = "Total copies cannot be negative")
    private Integer totalCopies;

    private Long authorId;

    private Long categoryId;
}
