package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.*;
import com.example.librarymanagement.entity.Author;
import com.example.librarymanagement.entity.Book;
import com.example.librarymanagement.entity.Category;
import com.example.librarymanagement.exception.DuplicateResourceException;
import com.example.librarymanagement.exception.InvalidCopyOperationException;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.repository.AuthorRepository;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToDTO(book);
    }

    @Transactional(readOnly = true)
    public List<BookDTO> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookDTO> searchByPublicationYear(Integer year) {
        return bookRepository.findByPublicationYear(year).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookDTO> searchByAuthorName(String authorName) {
        return bookRepository.findByAuthorNameContainingIgnoreCase(authorName).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookDTO> searchByCategoryName(String categoryName) {
        return bookRepository.findByCategoryName(categoryName).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDTO createBook(BookCreateDTO createDTO) {
        if (createDTO.getAvailableCopies() > createDTO.getTotalCopies()) {
            throw new InvalidCopyOperationException("Available copies (" + createDTO.getAvailableCopies() + 
                    ") cannot exceed total copies (" + createDTO.getTotalCopies() + ")");
        }
        if (bookRepository.findByIsbn(createDTO.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("Book with ISBN " + createDTO.getIsbn() + " already exists");
        }
        Author author = authorRepository.findById(createDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + createDTO.getAuthorId()));
        Category category = categoryRepository.findById(createDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + createDTO.getCategoryId()));

        Book book = Book.builder()
                .title(createDTO.getTitle())
                .isbn(createDTO.getIsbn())
                .publicationYear(createDTO.getPublicationYear())
                .availableCopies(createDTO.getAvailableCopies())
                .totalCopies(createDTO.getTotalCopies())
                .author(author)
                .category(category)
                .build();

        Book savedBook = bookRepository.save(book);
        return mapToDTO(savedBook);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookUpdateDTO updateDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        if (updateDTO.getTitle() != null) book.setTitle(updateDTO.getTitle());
        if (updateDTO.getIsbn() != null && !updateDTO.getIsbn().equals(book.getIsbn())) {
            if (bookRepository.findByIsbn(updateDTO.getIsbn()).isPresent()) {
                throw new DuplicateResourceException("Book with ISBN " + updateDTO.getIsbn() + " already exists");
            }
            book.setIsbn(updateDTO.getIsbn());
        }
        if (updateDTO.getPublicationYear() != null) book.setPublicationYear(updateDTO.getPublicationYear());

        Integer newTotal = updateDTO.getTotalCopies() != null ? updateDTO.getTotalCopies() : book.getTotalCopies();
        Integer newAvailable = updateDTO.getAvailableCopies() != null ? updateDTO.getAvailableCopies() : book.getAvailableCopies();

        if (newAvailable > newTotal) {
            throw new InvalidCopyOperationException("Available copies (" + newAvailable + ") cannot exceed total copies (" + newTotal + ")");
        }
        book.setTotalCopies(newTotal);
        book.setAvailableCopies(newAvailable);

        if (updateDTO.getAuthorId() != null && !updateDTO.getAuthorId().equals(book.getAuthor().getId())) {
            Author author = authorRepository.findById(updateDTO.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + updateDTO.getAuthorId()));
            book.setAuthor(author);
        }

        if (updateDTO.getCategoryId() != null && !updateDTO.getCategoryId().equals(book.getCategory().getId())) {
            Category category = categoryRepository.findById(updateDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + updateDTO.getCategoryId()));
            book.setCategory(category);
        }

        Book updatedBook = bookRepository.save(book);
        return mapToDTO(updatedBook);
    }

    @Transactional
    public BookDTO borrowBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        if (book.getAvailableCopies() <= 0) {
            throw new InvalidCopyOperationException("No copies available for book id: " + id);
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        return mapToDTO(bookRepository.save(book));
    }

    @Transactional
    public BookDTO returnBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        if (book.getAvailableCopies() >= book.getTotalCopies()) {
            throw new InvalidCopyOperationException("All copies are already returned for book id: " + id);
        }
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        return mapToDTO(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<QueryResponses.BookCategoryResponseDTO> getBooksWithCategoryName() {
        return bookRepository.findBooksWithCategoryName().stream()
                .map(proj -> new QueryResponses.BookCategoryResponseDTO(proj.getTitle(), proj.getCategoryName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QueryResponses.BookAuthorResponseDTO> getBooksWithAuthorName() {
        return bookRepository.findBooksWithAuthorName().stream()
                .map(proj -> new QueryResponses.BookAuthorResponseDTO(proj.getTitle(), proj.getAuthorName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QueryResponses.CategoryBookCountResponseDTO> getBookCountPerCategory() {
        return bookRepository.countBooksPerCategory().stream()
                .map(proj -> new QueryResponses.CategoryBookCountResponseDTO(proj.getCategoryName(), proj.getBookCount()))
                .collect(Collectors.toList());
    }

    private BookDTO mapToDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publicationYear(book.getPublicationYear())
                .availableCopies(book.getAvailableCopies())
                .totalCopies(book.getTotalCopies())
                .author(AuthorService.mapToSummary(book.getAuthor()))
                .category(CategoryService.mapToSummary(book.getCategory()))
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
