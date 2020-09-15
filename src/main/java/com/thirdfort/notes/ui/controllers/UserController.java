package com.thirdfort.notes.ui.controllers;

import com.thirdfort.notes.exceptions.ErrorMessages;
import com.thirdfort.notes.exceptions.userexceptions.UserServiceException;
import com.thirdfort.notes.shared.dtos.UserDto;
import com.thirdfort.notes.ui.models.requests.UserDetailsRequest;
import com.thirdfort.notes.ui.models.responses.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public UserResponse createUser(@RequestBody UserDetailsRequest userDetail) throws UserServiceException {
        if (userDetail.getEmail().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetail, UserDto.class);

        System.out.println(userDto);

        return modelMapper.map(userDto, UserResponse.class);
    }

}
