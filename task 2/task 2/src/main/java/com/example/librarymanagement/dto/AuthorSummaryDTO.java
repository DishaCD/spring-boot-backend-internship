package com.example.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorSummaryDTO {
    private Long id;
    private String name;
    private String nationality;
    private Integer birthYear;
}
