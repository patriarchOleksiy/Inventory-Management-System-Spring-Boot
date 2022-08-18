package ru.kataaas.ims.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kataaas.ims.dto.RegisterDTO;
import ru.kataaas.ims.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterDTO registerDTO) {
        if (userService.checkIfPhoneNumberAlreadyUsed(registerDTO.getPhoneNumber())) {
            return ResponseEntity.badRequest().body("The phone number is already in use");
        }
        if (userService.checkIfEmailAlreadyUsed(registerDTO.getEmail())) {
            return ResponseEntity.badRequest().body("The email is already in use");
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(registerDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
    }

}
