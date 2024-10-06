package com.example.my_project.service;

import com.example.my_project.dao.UserDao;
import com.example.my_project.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (userDao.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
        return true;
    }

    public User loginUser(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User getCurrentUser() {
        String currentUsername = getCurrentUsername();
        if (currentUsername != null) {
            return userDao.findByUsername(currentUsername);
        }
        return null;
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : null;
    }

    public User findUser(String username) {
        return userDao.findByUsername(username);
    }

    public boolean isCurrentUserTeacher() {
        String currentUsername = getCurrentUsername();
        return currentUsername != null && userDao.isTeacher(currentUsername);
    }
}

