-- V4__insert_sample_authors.sql (H2 compatible)
-- Inserts sample authors into the authors table

INSERT INTO authors (id, name, nationality, birth_year) VALUES
(1, 'J.K. Rowling', 'British', 1965),
(2, 'Paulo Coelho', 'Brazilian', 1947),
(3, 'George Orwell', 'British', 1903),
(4, 'James Clear', 'American', 1986),
(5, 'Robert Kiyosaki', 'American', 1947),
(6, 'Dan Brown', 'American', 1964),
(7, 'Yuval Noah Harari', 'Israeli', 1976),
(8, 'Stephen Hawking', 'British', 1942);

-- Sync the sequence so future auto-generated IDs don't collide
ALTER TABLE authors ALTER COLUMN id RESTART WITH 9;
