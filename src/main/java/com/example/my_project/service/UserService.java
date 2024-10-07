package com.example.my_project.service;

import com.example.my_project.dao.UserDao;
import com.example.my_project.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

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
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("User authenticated: " + user.getUsername());
            System.out.println("Security Context: " + SecurityContextHolder.getContext().getAuthentication());
            return user;
        }
        System.out.println("Authentication failed for username: " + username);
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
        System.out.println(currentUsername);
        if (currentUsername != null) {
            User user=userDao.findByUsername(currentUsername);
            return user;
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

