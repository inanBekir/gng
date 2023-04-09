package com.example.gng.service;

import com.example.gng.dto.UserDto;
import com.example.gng.model.Role;
import com.example.gng.model.User;
import com.example.gng.repository.RoleRepository;
import com.example.gng.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        List<Role> roles = roleRepository.findByUsersId(id);
        if (user != null) {
            user.setRoles(new HashSet<>(roles));
        }
        return user;
    }

    public User createUserWithRole(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(hashedPassword);
        user.setType(userDto.getType());
        Set<Role> roles = new HashSet<>();
        for(String roleName: userDto.getRoles()) {
            Role role = roleRepository.findByRole(roleName);
            roles.add(role);
        }
        // Add roles to the user
        user.setRoles(roles);

        return userRepository.save(user);
    }
}

