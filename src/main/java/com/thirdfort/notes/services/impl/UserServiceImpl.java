package com.thirdfort.notes.services.impl;

import com.thirdfort.notes.services.UserService;
import com.thirdfort.notes.shared.dtos.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto createUser(UserDto user) {
        return null;
    }

    @Override
    public UserDto getUser(String email) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
