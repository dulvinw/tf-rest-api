package com.thirdfort.notes.services;

import com.thirdfort.notes.shared.dtos.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);
    UserDto getUser(String uid);
}
