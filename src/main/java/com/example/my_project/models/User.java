package com.example.my_project.models;

import java.time.LocalDate;
import lombok.Data;

@Data
public class User {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate joinDate;
    private Integer age;
    private LocalDate dob;
    private String profilePicture;
    private UserRole role;

    public User() {
        this.role = UserRole.GUEST; 
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
