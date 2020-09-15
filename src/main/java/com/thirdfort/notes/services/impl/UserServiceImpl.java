package com.thirdfort.notes.services.impl;

import com.thirdfort.notes.exceptions.userexceptions.UserServiceException;
import com.thirdfort.notes.io.entities.UserEntity;
import com.thirdfort.notes.io.repositories.UserRepository;
import com.thirdfort.notes.services.UserService;
import com.thirdfort.notes.shared.Utils;
import com.thirdfort.notes.shared.dtos.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserDto createUser(UserDto user) {
        List<UserEntity> alreadyCreatedUserByEmail = userRepository.findByEmail(user.getEmail());
        if (alreadyCreatedUserByEmail.size() > 0) {
            throw new UserServiceException("Record already exist");
        }

        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUid(publicUserId);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity repoResponse = userRepository.save(userEntity);
        return modelMapper.map(repoResponse, UserDto.class);
    }

    @Override
    public UserDto getUser(String email) {
        List<UserEntity> users = userRepository.findByEmail(email);
        if (users.size() == 0) {
            throw new UserServiceException("User does not exist");
        }

        return modelMapper.map(users.get(0), UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<UserEntity> users = userRepository.findByEmail(email);

        if (users.size() == 0) throw new UsernameNotFoundException(email);

        UserEntity loadedUser = users.get(0);

        return new User(loadedUser.getEmail(), loadedUser.getEncryptedPassword(), new ArrayList<>());
    }
}
