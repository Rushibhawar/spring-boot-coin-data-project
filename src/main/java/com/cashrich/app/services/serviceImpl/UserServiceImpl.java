package com.cashrich.app.services.serviceImpl;

import com.cashrich.app.dao.request.SignUpRequest;
import com.cashrich.app.dao.request.UpdateRequest;
import com.cashrich.app.exceptions.ExceptionClass;
import com.cashrich.app.exceptions.ExceptionClass.*;
import com.cashrich.app.model.User;
import com.cashrich.app.repository.UserRepository;
import com.cashrich.app.services.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public User updateUser(String id, UpdateRequest request) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User does not exists"));

        Optional.ofNullable(request.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(request.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(request.getMobile()).ifPresent(user::setMobile);
        Optional.ofNullable(request.getUserPassword()).ifPresent(password -> user.setPassword(encoder.encode(password)));

        return userRepository.save(user);
    }
}
