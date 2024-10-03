package com.example.my_project.service;

import com.example.my_project.dao.UserDao;
import com.example.my_project.models.User;
import com.example.my_project.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
        if (userDao.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
        return true;
    }

    public Optional<User> loginUser(String username, String password) {
        return userDao.findByUsername(username)
            .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public Optional<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(getCurrentUsername())
            .flatMap(userDao::findByUsername);
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : null;
    }

    public boolean isCurrentUserTeacher() {
        return Optional.ofNullable(getCurrentUsername())
            .map(userDao::isTeacher)
            .orElse(false);
    }
}