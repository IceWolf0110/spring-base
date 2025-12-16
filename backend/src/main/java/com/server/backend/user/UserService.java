package com.server.backend.user;

import com.server.backend.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public ResponseEntity<List<UserResponse>> getUserList() {
        var userList = new ArrayList<UserResponse>();

        for (User user : userRepo.findAll()) {
            userList.add(getUserResponse(user));
        }

        return ResponseEntity.ok(userList);
    }

    public UserResponse getUserResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();
    }

}
