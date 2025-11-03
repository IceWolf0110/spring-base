package com.login.server.service;
import com.login.server.model.User;
import com.login.server.repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    public UserService(
            UserRepo userRepo,
            AuthenticationManager authManager,
            JWTService jwtService
    ) {
        this.userRepo = userRepo;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        user.setPassword(
                encoder.encode(user.getPassword())
        );
        return userRepo.save(user);
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public String verifyUser(User user) {
        var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
            user.getUsername(), user.getPassword()
        ));

        if (auth.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }

        return "fail";
    }
}
