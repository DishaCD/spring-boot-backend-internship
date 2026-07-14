package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.QueryResponses;
import com.example.librarymanagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/queries")
@RequiredArgsConstructor
@Tag(name = "Sample SQL Queries Demonstration", description = "Endpoints demonstrating join and aggregate queries from the PRD")
public class QueryController {

    private final BookService bookService;

    @GetMapping("/books-by-category")
    @Operation(summary = "Books by category (Join Query)", description = "Executes: SELECT b.title, c.name FROM books b JOIN categories c ON b.category_id = c.id")
    public ResponseEntity<List<QueryResponses.BookCategoryResponseDTO>> getBooksByCategory() {
        return ResponseEntity.ok(bookService.getBooksWithCategoryName());
    }

    @GetMapping("/books-by-author")
    @Operation(summary = "Books by author (Join Query)", description = "Executes: SELECT b.title, a.name FROM books b JOIN authors a ON b.author_id = a.id")
    public ResponseEntity<List<QueryResponses.BookAuthorResponseDTO>> getBooksByAuthor() {
        return ResponseEntity.ok(bookService.getBooksWithAuthorName());
    }

    @GetMapping("/books-count-per-category")
    @Operation(summary = "Number of books per category (Aggregate Query)", description = "Executes: SELECT c.name, COUNT(*) FROM books b JOIN categories c ON b.category_id = c.id GROUP BY c.name")
    public ResponseEntity<List<QueryResponses.CategoryBookCountResponseDTO>> getBookCountPerCategory() {
        return ResponseEntity.ok(bookService.getBookCountPerCategory());
    }
}
