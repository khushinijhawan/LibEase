package com.example.librarymanagement.Module;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Builder
@Setter
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String author;
    private boolean borrowed;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User borrower;
}

