package com.example.my_project.dao;

import com.example.my_project.models.User;
import com.example.my_project.models.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveUser(User user) {
        final String sql = "INSERT INTO USERS(username, password, first_name, last_name, email, join_date, age, dob, profile_picture, role) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
            user.getUsername(),
            user.getPassword(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getJoinDate(),
            user.getAge(),
            user.getDob(),
            user.getProfilePicture(),
            user.getRole().name()
        );
    }
    public Optional<User> findByUsername(String username) {
        final String sql = "SELECT * FROM USERS WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper<>(User.class)); // Added missing semicolon
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> findById(Long id) {
        final String sql = "SELECT * FROM USERS WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(User.class));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        final String sql = "SELECT * FROM USERS";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class)); 
        } catch (Exception e) {
            return Collections.emptyList(); 
        }
    }

    public boolean isTeacher(String username) {
        final String sql = "SELECT role FROM USERS WHERE username = ?";
        try {
            String role = jdbcTemplate.queryForObject(sql, new Object[]{username}, String.class);
            return UserRole.TEACHER.name().equals(role);
        } catch (Exception e) {
            return false;
        }
    }
}
