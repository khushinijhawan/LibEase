package com.example.librarymanagement.Service;

import com.example.librarymanagement.Module.User;
import com.example.librarymanagement.Repo.UserRepo;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    public User findUserByID(Integer id){
        log.info("Request to get user with ID: {}", id);
        Optional<User> user= userRepo.findById(id);
        return user.orElse(null);
    }
    public List<User> findAllUsers()
    {
        log.info("Request to get all users");
        return userRepo.findAll();
    }
    public User saveUser(User user)
    {
        log.info("Request to add user: {}", user);
        return userRepo.save(user);
    }
}
