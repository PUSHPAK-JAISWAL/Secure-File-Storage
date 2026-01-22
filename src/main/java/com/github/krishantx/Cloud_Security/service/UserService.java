package com.github.krishantx.Cloud_Security.service;

import com.github.krishantx.Cloud_Security.dto.RegisterRequest;
import com.github.krishantx.Cloud_Security.model.UserModel;
import com.github.krishantx.Cloud_Security.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserService(UserRepo userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel registerUser(RegisterRequest req) {
        UserModel user = new UserModel(req.getUsername(), passwordEncoder.encode(req.getPassword()));
        return userRepository.save(user);
    }

    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}