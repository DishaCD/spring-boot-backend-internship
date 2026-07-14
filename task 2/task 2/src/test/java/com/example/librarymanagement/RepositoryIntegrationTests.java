package com.example.librarymanagement;

import com.example.librarymanagement.entity.Book;
import com.example.librarymanagement.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RepositoryIntegrationTests {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Test custom repository method: findByTitleContainingIgnoreCase")
    void testFindByTitleContainingIgnoreCase() {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase("harry potter");
        assertThat(books).hasSizeGreaterThanOrEqualTo(2);
        assertThat(books).allMatch(b -> b.getTitle().toLowerCase().contains("harry potter"));
    }

    @Test
    @DisplayName("Test custom repository method: findByPublicationYear")
    void testFindByPublicationYear() {
        List<Book> books = bookRepository.findByPublicationYear(1997);
        assertThat(books).hasSizeGreaterThanOrEqualTo(2); // Harry Potter and Rich Dad Poor Dad
        assertThat(books).allMatch(b -> b.getPublicationYear().equals(1997));
    }

    @Test
    @DisplayName("Test custom repository method: findByAuthorNameContainingIgnoreCase")
    void testFindByAuthorNameContainingIgnoreCase() {
        List<Book> books = bookRepository.findByAuthorNameContainingIgnoreCase("orwell");
        assertThat(books).hasSizeGreaterThanOrEqualTo(2); // 1984 and Animal Farm
        assertThat(books).allMatch(b -> b.getAuthor().getName().toLowerCase().contains("orwell"));
    }

    @Test
    @DisplayName("Test custom repository method: findByCategoryName")
    void testFindByCategoryName() {
        List<Book> books = bookRepository.findByCategoryName("Mystery");
        assertThat(books).hasSizeGreaterThanOrEqualTo(2); // Angels and Demons and The Da Vinci Code
        assertThat(books).allMatch(b -> b.getCategory().getName().equals("Mystery"));
    }

    @Test
    @DisplayName("Test custom repository method: findByAvailableCopiesGreaterThan")
    void testFindByAvailableCopiesGreaterThan() {
        List<Book> books = bookRepository.findByAvailableCopiesGreaterThan(10);
        assertThat(books).hasSizeGreaterThanOrEqualTo(2); // Atomic Habits (15) and The Da Vinci Code (12)
        assertThat(books).allMatch(b -> b.getAvailableCopies() > 10);
    }

    @Test
    @DisplayName("Test JPQL join and aggregate query methods")
    void testJpqlQueryMethods() {
        List<BookRepository.BookCategoryProjection> categoryProjections = bookRepository.findBooksWithCategoryName();
        assertThat(categoryProjections).isNotEmpty();

        List<BookRepository.BookAuthorProjection> authorProjections = bookRepository.findBooksWithAuthorName();
        assertThat(authorProjections).isNotEmpty();

        List<BookRepository.CategoryBookCountProjection> countProjections = bookRepository.countBooksPerCategory();
        assertThat(countProjections).isNotEmpty();
        assertThat(countProjections).anyMatch(c -> c.getCategoryName().equals("Fantasy") && c.getBookCount() >= 2);
    }
}
