package com.example.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class QueryResponses {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookCategoryResponseDTO {
        private String title;
        private String categoryName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookAuthorResponseDTO {
        private String title;
        private String authorName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryBookCountResponseDTO {
        private String categoryName;
        private Long bookCount;
    }
}
