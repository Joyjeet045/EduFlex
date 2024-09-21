package com.example.my_project.controllers;

import java.util.List;
import java.util.Optional;

import com.example.my_project.models.User;
import com.example.my_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;
  @Autowired
  public UserController(UserService userService) {
      this.userService = userService;
  }
  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody @Valid User user) {
    boolean done = userService.registerUser(user);
    if (done) {
      return ResponseEntity.ok("User registered successfully!");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed! User may already exist.");
    }
  }
  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
      Optional<User> user = userService.loginUser(username, password);
      if(user.isPresent()) {
        return ResponseEntity.ok("Login successful! Welcome " + user.get().getUsername());
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body("Invalid username or password.");
      }
  }
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    Optional<User> user = userService.getUserById(id);
    if (user.isPresent()) {
        return ResponseEntity.ok(user.get());
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @GetMapping("/all")
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/current")
  public ResponseEntity<User> getCurrentUser() {
    Optional<User> currentUser = userService.getCurrentUser();
    if (currentUser.isPresent()) {
      return ResponseEntity.ok(currentUser.get());
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
    }
  }
  @GetMapping("/is-teacher")
  public ResponseEntity<Boolean> isCurrentUserTeacher() {
      Boolean isTeacher = userService.isCurrentUserTeacher();
      return ResponseEntity.status(HttpStatus.OK).body(isTeacher);
  }

}