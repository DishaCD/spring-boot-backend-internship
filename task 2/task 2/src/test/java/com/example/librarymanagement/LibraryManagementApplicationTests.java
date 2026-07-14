package com.example.librarymanagement;

import com.example.librarymanagement.entity.Author;
import com.example.librarymanagement.entity.Book;
import com.example.librarymanagement.entity.Category;
import com.example.librarymanagement.repository.AuthorRepository;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LibraryManagementApplicationTests {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Context loads and Flyway migrations create all tables and insert sample data")
    void testFlywayMigrationsAndSampleDataSeeding() {
        // Verify flyway_schema_history exists and has 6 migrations applied
        Integer migrationCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE success = true", Integer.class);
        assertThat(migrationCount).isNotNull().isGreaterThanOrEqualTo(6);

        // Verify authors table seeding
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSizeGreaterThanOrEqualTo(8);
        assertThat(authors).anyMatch(a -> a.getName().equals("J.K. Rowling") && a.getNationality().equals("British"));
        assertThat(authors).anyMatch(a -> a.getName().equals("Paulo Coelho") && a.getBirthYear().equals(1947));

        // Verify categories table seeding
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSizeGreaterThanOrEqualTo(7);
        assertThat(categories).anyMatch(c -> c.getName().equals("Fantasy"));
        assertThat(categories).anyMatch(c -> c.getName().equals("Self Help"));

        // Verify books table seeding
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSizeGreaterThanOrEqualTo(10);
        assertThat(books).anyMatch(b -> b.getTitle().equals("Atomic Habits") && b.getIsbn().equals("9780735211292"));
        assertThat(books).anyMatch(b -> b.getTitle().equals("Sapiens: A Brief History of Humankind"));
    }
}
