package com.example.librarymanagement.Service;
import static org.junit.jupiter.api.Assertions.*;
import com.example.librarymanagement.Module.User;
import com.example.librarymanagement.Repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepo userRepo;
    @Test
    public void getUserById_first_successCase()
    {
        User user1=User.builder().id(1).name("Khushi").phn_no(346980L).build();
        Optional<User> u=Optional.ofNullable(user1);
        when(userRepo.findById(1)).thenReturn(u);
        User user=userService.findUserByID(1);
        assertNotNull(user);
        assertEquals(1,user.getId());
    }
    @Test
    public void getUserById_second_successCase()
    {
        User user=null;
        Optional<User> u=Optional.ofNullable(user);
        when(userRepo.findById(any())).thenReturn(u);
        User user1=userService.findUserByID(null);
        assertNull(user1);
    }
    @Test
    public void getUserById_third_successCase()
    {

        User user1=User.builder().id(1).name("Khushi").phn_no(346980L).build();
        User user2=User.builder().id(2).name("Lavi").phn_no(9876450L).build();
        when(userRepo.findById(1)).thenReturn(Optional.ofNullable(user1));
        User user = userService.findUserByID(1);
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertNotEquals(user1.getId(), user2.getId());
        assertEquals(1, userRepo.findById(1).get().getId());
    }
    @Test
    public void getAllUsers_first_successCase()
    {
        List<User> userList = Arrays.asList(
                User.builder().id(1).name("Khushi").phn_no(346980L).build(),
                User.builder().id(2).name("Lavi").phn_no(9876450L).build()
        );
        when(userRepo.findAll()).thenReturn(userList);
        List<User> users = userService.findAllUsers();
        assertNotNull(users);
        assertEquals(1, users.get(0).getId());
        assertEquals(2, users.get(1).getId());
    }
    @Test
    public void saveUsers_first_successCase()
    {
        User userToSave = User.builder().id(1).name("Khushi").phn_no(346980L).build();
        when(userRepo.save(any(User.class))).thenReturn(userToSave);
        User savedUser = userService.saveUser(userToSave);
        assertNotNull(savedUser);
        assertEquals("Khushi", savedUser.getName());
    }
}
