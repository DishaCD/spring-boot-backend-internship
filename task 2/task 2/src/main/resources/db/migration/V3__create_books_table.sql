-- V3__create_books_table.sql
-- Creates the books table with foreign keys, unique ISBN constraint, and copy validation

CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) NOT NULL CONSTRAINT uk_books_isbn UNIQUE,
    publication_year INTEGER NOT NULL,
    available_copies INTEGER NOT NULL DEFAULT 0,
    total_copies INTEGER NOT NULL,
    author_id BIGINT NOT NULL CONSTRAINT fk_books_author REFERENCES authors(id) ON DELETE RESTRICT,
    category_id BIGINT NOT NULL CONSTRAINT fk_books_category REFERENCES categories(id) ON DELETE RESTRICT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_books_copy_validation CHECK (available_copies <= total_copies AND available_copies >= 0 AND total_copies >= 0)
);

CREATE INDEX idx_books_author_id ON books(author_id);
CREATE INDEX idx_books_category_id ON books(category_id);
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_publication_year ON books(publication_year);
CREATE INDEX idx_books_available_copies ON books(available_copies);
