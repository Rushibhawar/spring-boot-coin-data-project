package com.cashrich.app.dao.response;

import com.cashrich.app.security.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private UserDetailsImpl user;
}

