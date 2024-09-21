package com.example.my_project.dao;

import com.example.my_project.models.User;
import com.example.my_project.models.UserRole;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

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
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username)
                .stream()
                .findFirst();
    }

    public Optional<User> findById(Long id) {
        final String sql = "SELECT * FROM USERS WHERE id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id)
                .stream()
                .findFirst();
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
        return jdbcTemplate.queryForList(sql, String.class, username)
                .stream()
                .anyMatch(role -> UserRole.TEACHER.name().equals(role));
    }
}