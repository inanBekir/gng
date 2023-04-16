package com.example.gng.service;

import com.example.gng.dto.CreateUserDto;
import com.example.gng.model.Role;
import com.example.gng.model.User;
import com.example.gng.repository.RoleRepository;
import com.example.gng.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptService bCryptService;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    List<Role> roles = roleRepository.findByUsersId(id);
                    user.setRoles(new HashSet<>(roles));
                    return user;
                })
                .orElse(null);
    }

    public User createUserWithRole(CreateUserDto createUserDto) {
        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setPassword(bCryptService.encode(createUserDto.getPassword()));
        user.setType(createUserDto.getType());
        Set<Role> roles = new HashSet<>();
        for(String roleName: createUserDto.getRoles()) {
            Role role = roleRepository.findByRole(roleName);
            roles.add(role);
        }
        // Add roles to the user
        user.setRoles(roles);

        return userRepository.save(user);
    }
}

