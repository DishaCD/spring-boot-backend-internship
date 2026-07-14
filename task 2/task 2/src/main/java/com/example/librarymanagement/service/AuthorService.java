package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.AuthorDTO;
import com.example.librarymanagement.dto.AuthorSummaryDTO;
import com.example.librarymanagement.dto.BookSummaryDTO;
import com.example.librarymanagement.entity.Author;
import com.example.librarymanagement.entity.Book;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return mapToDTO(author);
    }

    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = Author.builder()
                .name(authorDTO.getName())
                .nationality(authorDTO.getNationality())
                .birthYear(authorDTO.getBirthYear())
                .build();
        Author savedAuthor = authorRepository.save(author);
        return mapToDTO(savedAuthor);
    }

    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        if (authorDTO.getName() != null) author.setName(authorDTO.getName());
        if (authorDTO.getNationality() != null) author.setNationality(authorDTO.getNationality());
        if (authorDTO.getBirthYear() != null) author.setBirthYear(authorDTO.getBirthYear());
        Author updatedAuthor = authorRepository.save(author);
        return mapToDTO(updatedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    private AuthorDTO mapToDTO(Author author) {
        List<BookSummaryDTO> bookSummaries = author.getBooks() != null ?
                author.getBooks().stream().map(this::mapBookToSummary).collect(Collectors.toList()) : List.of();
        return AuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .nationality(author.getNationality())
                .birthYear(author.getBirthYear())
                .books(bookSummaries)
                .build();
    }

    private BookSummaryDTO mapBookToSummary(Book book) {
        return BookSummaryDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publicationYear(book.getPublicationYear())
                .availableCopies(book.getAvailableCopies())
                .totalCopies(book.getTotalCopies())
                .categoryName(book.getCategory() != null ? book.getCategory().getName() : null)
                .build();
    }

    public static AuthorSummaryDTO mapToSummary(Author author) {
        return AuthorSummaryDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .nationality(author.getNationality())
                .birthYear(author.getBirthYear())
                .build();
    }
}
