package com.cashrich.app.services.serviceImpl;

import com.cashrich.app.controller.AuthController;
import com.cashrich.app.dao.request.SignInRequest;
import com.cashrich.app.dao.request.SignUpRequest;
import com.cashrich.app.dao.response.JwtResponse;
import com.cashrich.app.exceptions.ExceptionClass;
import com.cashrich.app.exceptions.ExceptionClass.*;
import com.cashrich.app.model.User;
import com.cashrich.app.repository.UserRepository;
import com.cashrich.app.security.jwt.JwtService;
import com.cashrich.app.security.services.UserDetailsImpl;
import com.cashrich.app.services.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthServiceimpl implements AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceimpl.class);

    @Override
    public User signup(SignUpRequest request) throws Exception {
            //Check user exists or not
            if (userRepository.existsByEmail(request.getUserEmail()) ||
                    userRepository.existsByUsername(request.getUserEmail()))
                throw new UserAlreadyExistsException("User already exists.");

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .email(request.getUserEmail())
                    .username(request.getUsername())
                    .password(encoder.encode(request.getUserPassword()))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .mobile(request.getMobile())
                    .build();

            User savedUser = userRepository.save(user);
            logger.info("Saved user : {}", savedUser);
            return savedUser;

    }

    @Override
    public JwtResponse signin(SignInRequest request) throws Exception {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getUserPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Invalid username"));

        if (!encoder.matches(request.getUserPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtService.generateToken(userDetails);

        return new JwtResponse(jwt, userDetails);

    }

}
