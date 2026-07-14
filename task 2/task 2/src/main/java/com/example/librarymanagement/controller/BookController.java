package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.BookCreateDTO;
import com.example.librarymanagement.dto.BookDTO;
import com.example.librarymanagement.dto.BookUpdateDTO;
import com.example.librarymanagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Endpoints for managing and searching library books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Fetch all books", description = "Retrieves all books stored in the library system")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch a book by ID", description = "Retrieves detailed information of a book by its unique ID")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Adds a new book to the database with copy validation check")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookCreateDTO createDTO) {
        return new ResponseEntity<>(bookService.createBook(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book", description = "Updates details or copies of an existing book by ID")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookUpdateDTO updateDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Removes a book from the library database by ID")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/title")
    @Operation(summary = "Search books by title", description = "Finds books matching a title substring (case-insensitive)")
    public ResponseEntity<List<BookDTO>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchByTitle(title));
    }

    @GetMapping("/search/year")
    @Operation(summary = "Search books by publication year", description = "Finds books published in a specific year")
    public ResponseEntity<List<BookDTO>> searchByPublicationYear(@RequestParam Integer year) {
        return ResponseEntity.ok(bookService.searchByPublicationYear(year));
    }

    @GetMapping("/search/author")
    @Operation(summary = "Search books by author name", description = "Finds books written by authors matching the provided name substring")
    public ResponseEntity<List<BookDTO>> searchByAuthorName(@RequestParam String author) {
        return ResponseEntity.ok(bookService.searchByAuthorName(author));
    }

    @GetMapping("/search/category")
    @Operation(summary = "Search books by category name", description = "Finds books assigned to an exact category name")
    public ResponseEntity<List<BookDTO>> searchByCategoryName(@RequestParam String category) {
        return ResponseEntity.ok(bookService.searchByCategoryName(category));
    }

    @GetMapping("/available")
    @Operation(summary = "Fetch available books only", description = "Retrieves all books with available_copies > 0")
    public ResponseEntity<List<BookDTO>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @PostMapping("/{id}/borrow")
    @Operation(summary = "Borrow a book", description = "Decrements available_copies by 1 if copies are available")
    public ResponseEntity<BookDTO> borrowBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.borrowBook(id));
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Return a borrowed book", description = "Increments available_copies by 1 if copies are less than total_copies")
    public ResponseEntity<BookDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.returnBook(id));
    }
}
