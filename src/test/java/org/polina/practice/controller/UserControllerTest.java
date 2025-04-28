package org.polina.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.entity.Order;
import org.polina.practice.entity.Product;
import org.polina.practice.entity.Status;
import org.polina.practice.entity.User;
import org.polina.practice.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void whenGetAllUsers_thenReturnListOfUsers() throws Exception {
        User user1 = new User(1L, "User 1", "user1@example.com", null);
        User user2 = new User(2L, "User 2", "user2@example.com", null);

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/v1/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$[0].orders").doesNotExist())
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("User 2"))
                .andExpect(jsonPath("$[1].email").value("user2@example.com"))
                .andExpect(jsonPath("$[1].orders").doesNotExist());

        verify(userService).getAllUsers();
    }

    @Test
    void whenGetUserById_thenReturnUser() throws Exception {
        Long userId = 1L;

        Product product1 = new Product(1L, "Laptop", new BigDecimal("1000.00"), null);
        Product product2 = new Product(2L, "Smartphone", new BigDecimal("500.00"), null);

        Order order1 = new Order(1L, new BigDecimal("1500.00"), Status.PAID, null, Arrays.asList(product1, product2));
        Order order2 = new Order(2L, new BigDecimal("1000.00"), Status.PAID, null, Arrays.asList(product1));

        User user = new User(userId, "User 1", "user1@example.com", Arrays.asList(order1, order2));

        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/v1/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("User 1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.orders", hasSize(2)))
                .andExpect(jsonPath("$.orders[0].id").value(1))
                .andExpect(jsonPath("$.orders[0].totalPrice").value("1500.0"))
                .andExpect(jsonPath("$.orders[0].status").value("PAID"))
                .andExpect(jsonPath("$.orders[0].products", hasSize(2)))
                .andExpect(jsonPath("$.orders[0].products[0].id").value(1))
                .andExpect(jsonPath("$.orders[0].products[0].name").value("Laptop"))
                .andExpect(jsonPath("$.orders[0].products[0].price").value("1000.0"))
                .andExpect(jsonPath("$.orders[0].products[1].id").value(2))
                .andExpect(jsonPath("$.orders[0].products[1].name").value("Smartphone"))
                .andExpect(jsonPath("$.orders[0].products[1].price").value("500.0"))
                .andExpect(jsonPath("$.orders[1].id").value(2))
                .andExpect(jsonPath("$.orders[1].totalPrice").value("1000.0"))
                .andExpect(jsonPath("$.orders[1].status").value("PAID"))
                .andExpect(jsonPath("$.orders[1].products", hasSize(1)))
                .andExpect(jsonPath("$.orders[1].products[0].id").value(1))
                .andExpect(jsonPath("$.orders[1].products[0].name").value("Laptop"))
                .andExpect(jsonPath("$.orders[1].products[0].price").value("1000.0"));

        verify(userService).getUserById(userId);
    }

    @Test
    void whenCreateUser_thenReturnCreatedUser() throws Exception {
        User userToCreate = new User(null, "User 1", "user1@example.com", null);
        User createdUser = new User(1L, "User 1", "user1@example.com", null);

        when(userService.createUser(userToCreate)).thenReturn(createdUser);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("User 1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.orders").doesNotExist());

        verify(userService).createUser(userToCreate);
    }

    @Test
    void whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        Long userId = 1L;
        User updatedUserData = new User(null, "User 1", "user1@example.com", null);
        User updatedUser = new User(userId, "User 1", "user1@example.com", null);

        when(userService.updateUser(userId, updatedUserData)).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("User 1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.orders").doesNotExist());

        verify(userService).updateUser(userId, updatedUserData);
    }

    @Test
    void whenDeleteUser_thenReturnNoContent() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deleteUserById(userId);

        mockMvc.perform(delete("/api/v1/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(userId);
    }
}