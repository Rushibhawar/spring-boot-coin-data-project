package com.cashrich.app.services.service;

import com.cashrich.app.dao.request.SignUpRequest;
import com.cashrich.app.dao.request.UpdateRequest;
import com.cashrich.app.model.User;

import java.util.Optional;

public interface UserService {

    User updateUser(String id, UpdateRequest request) throws Exception;

}
