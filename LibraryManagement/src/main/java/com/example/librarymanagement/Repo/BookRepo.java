package com.example.librarymanagement.Repo;
import com.example.librarymanagement.Module.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book,Integer> {
}
