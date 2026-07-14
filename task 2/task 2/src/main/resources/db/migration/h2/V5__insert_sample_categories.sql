-- V5__insert_sample_categories.sql (H2 compatible)
-- Inserts sample categories into the categories table

INSERT INTO categories (id, name, description) VALUES
(1, 'Fiction', 'Imaginative literature including novels and short stories'),
(2, 'Fantasy', 'Literature featuring magical and supernatural elements in fictional universes'),
(3, 'Self Help', 'Books focusing on personal growth, self-improvement, and building effective habits'),
(4, 'Finance', 'Literature covering personal finance, investing, wealth building, and financial literacy'),
(5, 'History', 'Records and analyses of past human events and societal evolutions'),
(6, 'Mystery', 'Stories focused on solving puzzling crimes, secrets, or suspenseful events'),
(7, 'Science', 'Works explaining natural phenomena, scientific discoveries, and theoretical physics');

-- Sync the sequence so future auto-generated IDs don't collide
ALTER TABLE categories ALTER COLUMN id RESTART WITH 8;
