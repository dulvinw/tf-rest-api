package com.thirdfort.notes.services;

import com.thirdfort.notes.exceptions.userexceptions.UserServiceException;
import com.thirdfort.notes.io.documents.UserDocument;
import com.thirdfort.notes.io.repositories.UserRepository;
import com.thirdfort.notes.services.impl.UserServiceImpl;
import com.thirdfort.notes.shared.Utils;
import com.thirdfort.notes.shared.dtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    private static final String PASSWORD = "test123#";

    private static final String USER_ID = "abc123fix";

    public static final String EMAIL = "dulvinw@gmail.com";

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Utils utils;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUser() {
        setupMocksForCreateUser();
        when(userRepository.findByEmail(anyString())).thenReturn(new ArrayList<>());

        UserDto response = userServiceImpl.createUser(getMockDTO());
        assertThat(USER_ID).isEqualTo(response.getUid());
        assertThat(EMAIL).isEqualTo(response.getEmail());
        assertThat(PASSWORD).isEqualTo(response.getEncryptedPassword());
    }

    @Test
    void testCreateDuplicateUser() {
        setupMocksForCreateUser();

        ArrayList<UserDocument> userList = new ArrayList<>();
        userList.add(getMockEntity());
        when(userRepository.findByEmail(anyString())).thenReturn(userList);

        Exception exception = assertThrows(UserServiceException.class, () -> userServiceImpl.createUser(getMockDTO()));
        assertThat(exception.getMessage()).isEqualTo("Record already exist");
    }

    @Test
    void testGetUser() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(getMockEntity()));

        UserDto response = userServiceImpl.getUser(USER_ID);
        assertThat(response.getEmail()).isEqualTo(EMAIL);
        assertThat(response.getUid()).isEqualTo(USER_ID);
        assertThat(response.getEncryptedPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void testGetInvalidUser() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserServiceException.class, () -> userServiceImpl.getUser(USER_ID));
        assertThat(exception.getMessage()).isEqualTo("User does not exist");
    }

    @Test
    void testLoadUserByUserName() {
        when(userRepository.findByEmail(anyString())).thenReturn(Collections.singletonList(getMockEntity()));

        User response = (User) userServiceImpl.loadUserByUsername(EMAIL);
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(USER_ID);
        assertThat(response.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void testLoadInvalidUserByUserName() {
        when(userRepository.findByEmail(anyString())).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(UsernameNotFoundException.class,
                                           () -> userServiceImpl.loadUserByUsername(EMAIL));
        assertThat(exception.getMessage()).isEqualTo(EMAIL);
    }

    private void setupMocksForCreateUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateUserId(anyInt())).thenReturn(USER_ID);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(PASSWORD);
        when(userRepository.save(any(UserDocument.class))).thenReturn(getMockEntity());
    }

    private UserDto getMockDTO() {
        UserDto dto = new UserDto();
        dto.setEmail(EMAIL);
        dto.setUid(USER_ID);
        dto.setPassword(PASSWORD);

        return dto;
    }

    private UserDocument getMockEntity() {
        UserDocument entity = new UserDocument();
        entity.setUid(USER_ID);
        entity.setEmail(EMAIL);
        entity.setEncryptedPassword(PASSWORD);

        return entity;
    }

}
