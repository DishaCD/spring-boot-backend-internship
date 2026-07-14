package com.example.librarymanagement.repository;

import com.example.librarymanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByPublicationYear(Integer year);

    List<Book> findByAuthorNameContainingIgnoreCase(String author);

    List<Book> findByCategoryName(String category);

    List<Book> findByAvailableCopiesGreaterThan(Integer copies);

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT b.title AS title, c.name AS categoryName FROM Book b JOIN b.category c")
    List<BookCategoryProjection> findBooksWithCategoryName();

    @Query("SELECT b.title AS title, a.name AS authorName FROM Book b JOIN b.author a")
    List<BookAuthorProjection> findBooksWithAuthorName();

    @Query("SELECT c.name AS categoryName, COUNT(b) AS bookCount FROM Book b JOIN b.category c GROUP BY c.name")
    List<CategoryBookCountProjection> countBooksPerCategory();

    interface BookCategoryProjection {
        String getTitle();
        String getCategoryName();
    }

    interface BookAuthorProjection {
        String getTitle();
        String getAuthorName();
    }

    interface CategoryBookCountProjection {
        String getCategoryName();
        Long getBookCount();
    }
}
