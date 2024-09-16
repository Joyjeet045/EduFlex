package com.example.my_project.controllers;

import java.util.List;
import java.util.Optional;

import com.example.my_project.models.User;
import com.example.my_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
      User foundUser = user.get();
      String userInfo = String.format("User found: ID=%d, Username=%s, Email=%s", foundUser.getId(), foundUser.getUsername(), foundUser.getEmail());
      return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    } 
    else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + id);
    }
  }

  @GetMapping("/all")
  @ResponseBody
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }
  
  @GetMapping("/current")
  public ResponseEntity<String> getCurrentUser() {
    Optional<User> currentUser = userService.getCurrentUser();
    if (currentUser.isPresent()) {
        User user = currentUser.get();
        String userInfo = String.format("Current user: ID=%d, Username=%s, Email=%s",user.getId(),user.getUsername(),user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No current user found. Please log in.");
    }
  }

  @GetMapping("/is-teacher")
  public ResponseEntity<String> isCurrentUserTeacher() {
    boolean isTeacher = userService.isCurrentUserTeacher();
    String responseMessage = isTeacher ? "Current user is a teacher." : "Current user is not a teacher.";
    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }
}