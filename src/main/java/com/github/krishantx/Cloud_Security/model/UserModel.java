package com.github.krishantx.Cloud_Security.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public void setUsername(String username) {
        this.username = username != null ? username.toLowerCase() : null;
    }

    public UserModel(String username, String password) {
        this.username = username != null ? username.toLowerCase() : null;
        this.password = password;
    }

    @Override
    public String toString() {
        return username + " " + password;
    }
}