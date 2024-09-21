package com.example.my_project.service;

import com.example.my_project.dao.UserDao;
import com.example.my_project.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  private final UserDao userDao;
  private final PasswordEncoder passwordEncoder;
  @Autowired
  public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
    this.userDao = userDao;
    this.passwordEncoder = passwordEncoder;
  }
  public boolean registerUser(User user) {
    Optional<User> existingUser = userDao.findByUsername(user.getUsername());
    if (existingUser.isPresent()) {
      return false; 
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userDao.saveUser(user);
    return true;
  }
  public Optional<User> loginUser(String username, String password) {
    Optional<User> userOptional = userDao.findByUsername(username);
    if (userOptional.isPresent()) {
        User user = userOptional.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(user); 
        }
    }
    return Optional.empty(); 
}

  public Optional<User> getUserById(Long id) {
    return userDao.findById(id);
  }
  public List<User> getAllUsers() {
    return userDao.findAll();
  }

  public Optional<User> getCurrentUser() {
    String currentUsername = getCurrentUsername();
    if (currentUsername == null) {
        return Optional.empty();
    }
    return userDao.findByUsername(currentUsername);
  }

  public String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    return authentication.getName();
  }
  public boolean isCurrentUserTeacher() {
    String currentUsername = getCurrentUsername();
    if (currentUsername == null) {
      return false;
    }
    return userDao.isTeacher(currentUsername);
  }
}