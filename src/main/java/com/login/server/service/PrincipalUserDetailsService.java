package com.login.server.service;

import com.login.server.model.UserPrincipal;
import com.login.server.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    public PrincipalUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepo.findByUsername(username);

        if (user == null) {
            System.err.println("Username not found");
        }

        return new UserPrincipal(user);
    }
}
