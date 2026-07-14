package com.example.librarymanagement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/books should return all sample books")
    void testGetAllBooksEndpoint() throws Exception {
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(10))))
                .andExpect(jsonPath("$[0].title", notNullValue()))
                .andExpect(jsonPath("$[0].author.name", notNullValue()))
                .andExpect(jsonPath("$[0].category.name", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/authors should return all sample authors")
    void testGetAllAuthorsEndpoint() throws Exception {
        mockMvc.perform(get("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(8))))
                .andExpect(jsonPath("$[?(@.name == 'J.K. Rowling')]", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/categories should return all sample categories")
    void testGetAllCategoriesEndpoint() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(7))))
                .andExpect(jsonPath("$[?(@.name == 'Fiction')]", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/queries/books-by-category should return join results")
    void testBooksByCategoryQueryEndpoint() throws Exception {
        mockMvc.perform(get("/api/queries/books-by-category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(10))))
                .andExpect(jsonPath("$[0].title", notNullValue()))
                .andExpect(jsonPath("$[0].categoryName", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/queries/books-count-per-category should return aggregate counts")
    void testBooksCountPerCategoryQueryEndpoint() throws Exception {
        mockMvc.perform(get("/api/queries/books-count-per-category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(7))))
                .andExpect(jsonPath("$[0].categoryName", notNullValue()))
                .andExpect(jsonPath("$[0].bookCount", notNullValue()));
    }

    @Test
    @DisplayName("DELETE /api/authors/{id} should fail with Conflict (409) when author has books")
    void testDeleteAuthorWithBooksShouldFail() throws Exception {
        mockMvc.perform(delete("/api/authors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} should fail with Conflict (409) when category has books")
    void testDeleteCategoryWithBooksShouldFail() throws Exception {
        mockMvc.perform(delete("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
