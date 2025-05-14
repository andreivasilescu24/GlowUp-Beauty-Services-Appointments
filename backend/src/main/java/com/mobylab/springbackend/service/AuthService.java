package com.mobylab.springbackend.service;

import com.mobylab.springbackend.config.security.JwtGenerator;
import com.mobylab.springbackend.entity.Role;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.RoleRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.auth.LoginDto;
import com.mobylab.springbackend.service.dto.auth.LoginResponseDto;
import com.mobylab.springbackend.service.dto.auth.RegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private LoginResponseDto loginResponseDto;

    public void register(RegisterDto registerDto) {
        if(userRepository.existsUserByEmail(registerDto.getEmail())) {
            throw new BadRequestException("Email is already used");
        }

        String roleName = registerDto.getRole().toUpperCase();

        if (!roleName.equals("USER") && !roleName.equals("OWNER")) {
            throw new BadRequestException("Invalid role. Allowed roles are USER or OWNER.");
        }

        List<Role> roleList = new ArrayList<>();
        roleList.add(roleRepository.findRoleByName(roleName).get());

        userRepository.save(new User()
                .setEmail(registerDto.getEmail())
                .setPassword(passwordEncoder.encode(registerDto.getPassword()))
                .setName(registerDto.getUsername())
                .setRoles(roleList));
    }

    public LoginResponseDto login(LoginDto loginDto) {
        Optional<User> optionalUser = userRepository.findUserByEmail(loginDto.getEmail());
        if(optionalUser.isEmpty()) {
            throw new BadRequestException("Wrong credentials");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Get the user's role
        User user = optionalUser.get();
        String role = user.getRoles().get(0).getName(); // Assuming one role per user
        
        // Generate token and set response
        String token = jwtGenerator.generateToken(authentication);
        return loginResponseDto
                .setToken(token)
                .setRole(role);
    }
}
