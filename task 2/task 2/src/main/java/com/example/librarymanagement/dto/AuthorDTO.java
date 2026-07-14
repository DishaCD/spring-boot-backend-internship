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
public class AuthorDTO {
    private Long id;
    
    @NotBlank(message = "Author name must not be blank")
    @Size(max = 150)
    private String name;
    
    @Size(max = 100)
    private String nationality;
    
    private Integer birthYear;
    
    private List<BookSummaryDTO> books;
}
