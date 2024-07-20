package com.nur.service;

import com.nur.dto.UserRegistrationDto;
import com.nur.entity.UserInfo;
import com.nur.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserInfoRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(UserRegistrationDto userRegistrationDto) {
        UserInfo userInfo = createUserInfo(userRegistrationDto);
        userRepository.save(userInfo);
        LOGGER.debug("User registered with username: {}", userRegistrationDto.getName());
        return "User Added Successfully";
    }

    private UserInfo createUserInfo(UserRegistrationDto userRegistrationDto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userRegistrationDto.getName());
        userInfo.setEmail(userRegistrationDto.getEmail());
        userInfo.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword())); // Use password from DTO
        userInfo.setRoles("ROLE_" + userRegistrationDto.getRoles());
        return userInfo;
    }
}
