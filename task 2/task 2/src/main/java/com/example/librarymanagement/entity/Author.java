package com.example.librarymanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Author name must not be blank")
    @Size(max = 150, message = "Author name must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String name;

    @Size(max = 100, message = "Nationality must not exceed 100 characters")
    @Column(length = 100)
    private String nationality;

    @Column(name = "birth_year")
    private Integer birthYear;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"author", "category"})
    @Builder.Default
    private List<Book> books = new ArrayList<>();
}
