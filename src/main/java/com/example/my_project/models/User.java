package com.example.my_project.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Getter
public class User{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false,unique = true, length = 50);
  private String username;

  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "join_date", nullable = false)
  private LocalDate joinDate;

  @Min(5)
  @Max(120)
  @Column(nullable = false)
  private Integer age;

  @Column(nullable = false)
  private LocalDate dob;

  @Column(name = "profile_picture", length = 255)
  private String profilePicture;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  public String getFullName() {
    return firstName + " " + lastName;
  }
}

public enum UserRole {
  STUDENT,
  TEACHER
}