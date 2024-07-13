package com.cashrich.app.controller;

import com.cashrich.app.dao.request.SignInRequest;
import com.cashrich.app.dao.request.SignUpRequest;
import com.cashrich.app.dao.response.APIResponse;
import com.cashrich.app.dao.response.JwtResponse;
import com.cashrich.app.model.User;
import com.cashrich.app.security.jwt.AuthEntryPointJwt;
import com.cashrich.app.services.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authenticationService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signup")
    public ResponseEntity<APIResponse<Object>> signup(@Valid @RequestBody SignUpRequest request) throws Exception {

        logger.info("Sign Up request");
        User authenticationResponse = authenticationService.signup(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new APIResponse<Object>(HttpStatus.OK, true, "","Registered successfully."));
    }


    @PostMapping("/signin")
    public ResponseEntity<APIResponse<Object>> signin(@Valid @RequestBody SignInRequest request) throws Exception {

        JwtResponse jwtResponse = authenticationService.signin(request);
        logger.info("JWT Token : {}",jwtResponse.getToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new APIResponse<Object>(HttpStatus.OK, true, jwtResponse,"Login successful."));
    }

}
