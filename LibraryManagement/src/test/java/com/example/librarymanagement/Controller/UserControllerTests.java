package com.example.librarymanagement.Controller;
import com.example.librarymanagement.Module.User;
import com.example.librarymanagement.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void getUser_successCase() throws Exception {

        User mockUser = User.builder().id(1).name("Khushi").phn_no(123456789L).build();
        when(userService.findUserByID(1)).thenReturn(mockUser);

        mockMvc.perform(get("/api/users/{id}",1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockUser)))
                 .andExpect(jsonPath("$.id").value(mockUser.getId()))
                .andExpect(jsonPath("$.name").value(mockUser.getName()));

        assertNotNull(mockUser);

    }
    @Test
    public void getAllUsers_successCase() throws Exception {
        List<User> userList = Arrays.asList(
                User.builder().id(1).name("Khushi").phn_no(346980L).build(),
                User.builder().id(2).name("Lavi").phn_no(9876450L).build()
        );

        when(userService.findAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userList)));

        assertNotNull(userList);
        assertEquals(2,userList.size());
    }
    @Test
    public void createUser_successCase() throws Exception{
        User user = User.builder().id(1).name("Khushi").phn_no(123456789L).build();

        when(userService.saveUser(user)).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(user.getId()))
                        .andExpect(jsonPath("$.name").value(user.getName()));

        assertNotNull(user);
    }
}

