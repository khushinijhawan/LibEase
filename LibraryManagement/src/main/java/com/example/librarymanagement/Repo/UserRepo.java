package com.example.librarymanagement.Repo;

import com.example.librarymanagement.Module.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
