package com.flywithus.controller;

import com.flywithus.dao.UserRepository;
import com.flywithus.dto.UserToAddDto;
import com.flywithus.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity addUser(@RequestBody UserToAddDto userToAddDto) {

        User userToAdd = new User();
        userToAdd.setPassword(userToAddDto.getPassword());
        userToAdd.setEmail(userToAddDto.getEmail());
        try {
            userRepository.save(userToAdd);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("User cannot be created.");
        }
        return ResponseEntity.ok().build();
    }

}
