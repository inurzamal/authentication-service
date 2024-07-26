package com.nur.controller;

import com.nur.dto.UserLoginDto;
import com.nur.dto.UserRegistrationDto;
import com.nur.service.UserService;
import com.nur.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        String msg = userService.registerUser(userRegistrationDto);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/token")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody UserLoginDto userLoginDto) {
        try {
            LOGGER.debug("Authenticating user: {}", userLoginDto.getUsername());
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(userLoginDto.getUsername());
                LOGGER.debug("Generated token for user: {}", userLoginDto.getUsername());
                return ResponseEntity.ok(token);
            } else {
                throw new UsernameNotFoundException("Invalid user request!");
            }
        } catch (Exception e) {
            LOGGER.error("Authentication failed: ", e);
            throw e;
        }
    }
}
