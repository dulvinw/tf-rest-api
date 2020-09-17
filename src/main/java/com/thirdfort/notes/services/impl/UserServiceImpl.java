package com.thirdfort.notes.services.impl;

import com.thirdfort.notes.exceptions.userexceptions.UserServiceException;
import com.thirdfort.notes.io.documents.UserDocument;
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

    /**
     * Create a new user
     * @param user user's information
     * @return created user
     */
    @Override
    public UserDto createUser(UserDto user) {
        List<UserDocument> alreadyCreatedUserByEmail = userRepository.findByEmail(user.getEmail());
        if (alreadyCreatedUserByEmail.size() > 0) {
            throw new UserServiceException("Record already exist");
        }

        UserDocument userDocument = modelMapper.map(user, UserDocument.class);

        String publicUserId = utils.generateUserId(30);
        userDocument.setUid(publicUserId);

        userDocument.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserDocument repoResponse = userRepository.save(userDocument);
        return modelMapper.map(repoResponse, UserDto.class);
    }

    /**
     * Get user's information given user's public id
     * @param uid user's public id
     * @return user information
     */
    @Override
    public UserDto getUser(String uid) {
        UserDocument user = userRepository.findById(uid).orElse(null);
        if (user == null) {
            throw new UserServiceException("User does not exist");
        }

        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Find a user using email. Used in the authentication filter
     * @param email email of the user trying to log in
     * @return userDetails object to be used in the authentication service
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<UserDocument> users = userRepository.findByEmail(email);

        if (users.size() == 0) throw new UsernameNotFoundException(email);

        UserDocument loadedUser = users.get(0);

        return new User(loadedUser.getUid(), loadedUser.getEncryptedPassword(), new ArrayList<>());
    }
}
