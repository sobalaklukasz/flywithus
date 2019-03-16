package com.flywithus.dao;

import com.flywithus.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindAllUsers() {
        List<User> allUsers = userRepository.findAll();

        assertThat(allUsers).extracting("id").containsExactlyInAnyOrder(2L, 3L);
        assertThat(allUsers).extracting("email").containsExactlyInAnyOrder("user1@mail.com", "user2@mail.com");
        assertThat(allUsers).extracting("password").containsExactlyInAnyOrder("password", "password");
    }

    @Test
    public void shouldFindUserById() {
        User user = userRepository.findUserById(2L);

        assertThat(user).extracting(User::getId).isEqualTo(2L);
        assertThat(user).extracting(User::getEmail).isEqualTo("user1@mail.com");
        assertThat(user).extracting(User::getPassword).isEqualTo("password");
    }

    @Test
    public void shouldFindUserByEmail() {
        User user = userRepository.findUserByEmail("user1@mail.com");

        assertThat(user).extracting(User::getId).isEqualTo(2L);
        assertThat(user).extracting(User::getEmail).isEqualTo("user1@mail.com");
        assertThat(user).extracting(User::getPassword).isEqualTo("password");
    }
}
