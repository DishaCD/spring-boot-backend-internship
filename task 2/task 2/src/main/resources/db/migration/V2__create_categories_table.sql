-- V2__create_categories_table.sql
-- Creates the categories table with unique constraint on name

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL CONSTRAINT uk_categories_name UNIQUE,
    description TEXT
);

CREATE INDEX idx_categories_name ON categories(name);
