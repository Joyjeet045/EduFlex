package com.example.my_project.dao;

import com.example.my_project.models.User;
import com.example.my_project.models.UserRole;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveUser(User user) {
        final String sql = "INSERT INTO users(username, password, first_name, last_name, email, join_date, age, dob, profile_picture, role) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

    public User findByUsername(String username) {
        final String sql = "SELECT * FROM users WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username);
        return users.isEmpty() ? null : users.get(0);
    }

    public User findById(Long id) {
        final String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id);
        return users.isEmpty() ? null : users.get(0);
    }

    public List<User> findAll() {
        final String sql = "SELECT * FROM users";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public boolean isTeacher(String username) {
        final String sql = "SELECT role FROM users WHERE username = ?";
        List<String> roles = jdbcTemplate.queryForList(sql, String.class, username);
        return roles.stream().anyMatch(role -> UserRole.TEACHER.name().equals(role));
    }
}
