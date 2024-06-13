package com.example.librarymanagement.Controller;
import com.example.librarymanagement.Module.User;
import com.example.librarymanagement.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id)
    {
        return userService.findUserByID(id);
    }
    @GetMapping
    public List<User> getAllUsers()
    {
        return userService.findAllUsers();
    }
    @PostMapping
    public User addUser(@RequestBody User user)
    {
        return userService.saveUser(user);
    }
}
