-- V1__create_authors_table.sql
-- Creates the authors table to store book author details

CREATE TABLE authors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    nationality VARCHAR(100),
    birth_year INTEGER
);

CREATE INDEX idx_authors_name ON authors(name);
