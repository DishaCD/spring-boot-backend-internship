-- V6__insert_sample_books.sql
-- Inserts sample books into the books table (12 books total across all authors and categories)

INSERT INTO books (id, title, isbn, publication_year, available_copies, total_copies, author_id, category_id, created_at, updated_at) VALUES
(1, 'Harry Potter and the Philosopher''s Stone', '9780747532743', 1997, 10, 12, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'The Alchemist', '9780062315007', 1988, 8, 10, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Atomic Habits', '9780735211292', 2018, 15, 20, 4, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Rich Dad Poor Dad', '9781612680194', 1997, 5, 8, 5, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, '1984', '9780451524935', 1949, 4, 10, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Sapiens: A Brief History of Humankind', '9780062316097', 2011, 7, 9, 7, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Angels and Demons', '9780671027360', 2000, 6, 6, 6, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Animal Farm', '9780451526342', 1945, 3, 5, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Harry Potter and the Chamber of Secrets', '9780747538493', 1998, 9, 11, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'The Da Vinci Code', '9780385504205', 2003, 12, 15, 6, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'Homo Deus: A Brief History of Tomorrow', '9780062464316', 2015, 6, 8, 7, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'A Brief History of Time', '9780553380163', 1988, 5, 7, 8, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Reset sequence to ensure future JPA insertions do not collide with sample IDs
SELECT setval('books_id_seq', (SELECT MAX(id) FROM books));
