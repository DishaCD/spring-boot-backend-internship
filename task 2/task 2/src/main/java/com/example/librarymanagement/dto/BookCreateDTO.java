package com.example.librarymanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateDTO {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "ISBN must not be blank")
    @Size(max = 20)
    private String isbn;

    @NotNull(message = "Publication year must not be null")
    private Integer publicationYear;

    @NotNull(message = "Available copies must not be null")
    @Min(value = 0, message = "Available copies cannot be negative")
    private Integer availableCopies;

    @NotNull(message = "Total copies must not be null")
    @Min(value = 0, message = "Total copies cannot be negative")
    private Integer totalCopies;

    @NotNull(message = "Author ID must not be null")
    private Long authorId;

    @NotNull(message = "Category ID must not be null")
    private Long categoryId;
}
