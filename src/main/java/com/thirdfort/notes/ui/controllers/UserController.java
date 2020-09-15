package com.thirdfort.notes.ui.controllers;

import com.thirdfort.notes.exceptions.ErrorMessages;
import com.thirdfort.notes.exceptions.userexceptions.UserServiceException;
import com.thirdfort.notes.services.UserService;
import com.thirdfort.notes.shared.dtos.UserDto;
import com.thirdfort.notes.ui.models.requests.UserDetailsRequest;
import com.thirdfort.notes.ui.models.responses.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserDetailsRequest userDetail) throws UserServiceException {
        if (userDetail.getEmail().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetail, UserDto.class);

        UserDto newUser = userService.createUser(userDto);

        return modelMapper.map(newUser, UserResponse.class);
    }

    @GetMapping("/test")
    public UserResponse test() throws UserServiceException{
        return new UserResponse("test", "test", "test");
    }

}
