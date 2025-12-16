package com.server.backend.auth;

import com.server.backend.auth.dto.LoginRequest;
import com.server.backend.auth.dto.LoginResponse;
import com.server.backend.auth.dto.RegisterRequest;
import com.server.backend.auth.dto.RegisterResponse;
import com.server.backend.jwt.JwtService;
import com.server.backend.user.User;
import com.server.backend.user.UserRepo;
import com.server.backend.user.UserRole;
import com.server.backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public ResponseEntity<RegisterResponse> register(RegisterRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(RegisterResponse.builder()
                            .message("User already exists!")
                            .build());
        }

        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(RegisterResponse.builder()
                            .message("This email is already being used!")
                            .build());
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(UserRole.USER)
                .build();

        userRepo.save(user);

        return ResponseEntity.ok(
                RegisterResponse.builder()
                        .message("User register successful!")
                        .build()
        );
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        final String username = request.getUsername();

        var user = userRepo.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(LoginResponse.builder()
                            .token(null)
                            .user(null)
                            .message("User not found with username: " + username)
                            .build()
                    );
        }

        final String password = request.getPassword();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        var jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(
                LoginResponse.builder()
                        .token(jwtToken)
                        .message("User login successful!")
                        .user(userService.getUserResponse(user))
                        .build()
        );
    }
}
