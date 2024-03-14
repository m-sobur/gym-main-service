package com.example.springcore.controller.impl;

import com.example.springcore.controller.AuthenticationApi;
import com.example.springcore.dto.NewPasswordDTO;
import com.example.springcore.dto.UserCredentialsDTO;
import com.example.springcore.service.AuthenticationService;
import com.example.springcore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationApi {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Override
    public ResponseEntity<Void> loginUserWithCredentials( String username, String password) {
       authenticationService.authenticationUser(
               username,
               password
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> changeLoginWithNewPassword(NewPasswordDTO newPasswordDTORequest) {
        userService.changeUserPassword(
                newPasswordDTORequest.getUsername(),
                newPasswordDTORequest.getPassword(),
                newPasswordDTORequest.getNewPassword()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}