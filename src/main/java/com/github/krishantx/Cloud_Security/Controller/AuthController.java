package com.github.krishantx.Cloud_Security.controller;

import com.github.krishantx.Cloud_Security.dto.RegisterRequest;
import com.github.krishantx.Cloud_Security.dto.UserDto;
import com.github.krishantx.Cloud_Security.model.UserModel;
import com.github.krishantx.Cloud_Security.service.AuthService;
import com.github.krishantx.Cloud_Security.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    private AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequest req) {
        UserModel u = userService.registerUser(req);
        UserDto dto = new UserDto(u.getId(), u.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody RegisterRequest req) {
        String token = authService.login(req);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails ud) {
        if (ud == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }

        return userService.findByUsername(ud.getUsername())
                .map(u -> {
                    UserDto dto = new UserDto(u.getId(), u.getUsername());
                    Map<String, Object> response = Map.of("user", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found")));
    }
}