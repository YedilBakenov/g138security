package com.security.securityg138.service;

import com.security.securityg138.model.User;
import com.security.securityg138.repository.PermissionRepository;
import com.security.securityg138.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public String addUser(User user, String rePassword) {

        User userFromBase = userRepository.getUserByEmail(user.getEmail());

        if (userFromBase != null) {
           return  "error";
        }

        if (!user.getPassword().equals(rePassword)) {
            return  "error";
        }

        user.setPassword(passwordEncoder.encode(rePassword));

        user.setPermissions(List.of(permissionRepository.getBasePermission()));

        userRepository.save(user);

        return "success";

    }

    public void changePassword(String oldPass, String newPass, String reNewPass) {
        if(!passwordEncoder.matches(oldPass, getCurrentUser().getPassword())){
            return;
        }

        if(!newPass.equals(reNewPass)){
            return;
        }

        getCurrentUser().setPassword(passwordEncoder.encode(newPass));

        userRepository.save(getCurrentUser());
    }
}
