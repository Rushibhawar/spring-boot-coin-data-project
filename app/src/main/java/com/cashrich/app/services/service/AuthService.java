package com.cashrich.app.services.service;


import com.cashrich.app.dao.request.SignInRequest;
import com.cashrich.app.dao.request.SignUpRequest;
import com.cashrich.app.dao.response.JwtResponse;
import com.cashrich.app.model.User;

public interface AuthService {

    User signup(SignUpRequest request) throws Exception;

    JwtResponse signin(SignInRequest request) throws Exception;


}
