package ug.edu.animal.user.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ug.edu.animal.user.service.UserService;
import ug.edu.animal.user.service.serviceimpl.UserLoginInput;
import ug.edu.animal.user.service.serviceimpl.UserOutput;
import ug.edu.animal.user.service.serviceimpl.UserRegistrationInput;

@RestController
@RequestMapping("api/v1/authorization")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    ResponseEntity<UserOutput> login(@RequestBody UserLoginInput userLoginInput){
        UserOutput userOutput = userService.loginUser(userLoginInput);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(userLoginInput.email(), userLoginInput.password());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, headers.getFirst("Authorization")).body(userOutput);
    }

    @PostMapping("/register")
    ResponseEntity<UserOutput> register(@RequestBody UserRegistrationInput userRegistrationInput){
        UserOutput userOutput = userService.registerUser(userRegistrationInput);
        return ResponseEntity.status(HttpStatus.OK).body(userOutput);
    }


}
