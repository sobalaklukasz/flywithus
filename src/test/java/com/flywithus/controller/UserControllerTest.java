package com.flywithus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flywithus.dao.UserRepository;
import com.flywithus.dto.UserToAddDto;
import com.flywithus.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldReturnBadRequestDueToValidation() throws Exception {
        UserToAddDto invalidUser = UserToAddDto.builder().email("incorrectMail").password("pass").build();

        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(invalidUser)).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email or password is not correct")));
    }

    @Test
    public void shouldReturnBadRequestDueToDatabaseProblem() throws Exception {
        when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);
        UserToAddDto validUser = UserToAddDto.builder().email("correct@mail.com").password("pass").build();

        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(validUser)).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User cannot be created.")));
    }

    @Test
    public void shouldCorrectlyAddUser() throws Exception {
        UserToAddDto validUser = UserToAddDto.builder().email("correct@mail.com").password("pass").build();
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(validUser)).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        verify(userRepository, times(1)).save(userCaptor.capture());
        User expectedUser = new User();
        expectedUser.setEmail(validUser.getEmail());
        expectedUser.setPassword(validUser.getPassword());
        assertThat(userCaptor.getValue()).isEqualTo(expectedUser);
    }
}
