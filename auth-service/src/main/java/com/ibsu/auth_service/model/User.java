package com.ibsu.auth_service.model;

import com.ibsu.common.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class User {
    @Id
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private LocalDate deactivationDate;
    private Boolean isActive;
    private LocalDate registrationDate;

    public User(String username, String password, RoleEnum role, String email, String phone, String firstName, String lastName, LocalDate deactivationDate, Boolean isActive) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deactivationDate = deactivationDate;
        this.isActive = isActive;
        this.registrationDate = LocalDate.now();
    }
}
