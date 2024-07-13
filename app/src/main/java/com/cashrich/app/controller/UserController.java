package com.cashrich.app.controller;

import com.cashrich.app.dao.request.UpdateRequest;
import com.cashrich.app.dao.response.APIResponse;
import com.cashrich.app.model.CoinData;
import com.cashrich.app.model.User;
import com.cashrich.app.repository.UserRepository;
import com.cashrich.app.security.services.UserDetailsImpl;
import com.cashrich.app.services.service.CoinDataService;
import com.cashrich.app.services.serviceImpl.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    CoinDataService coinDataService;

    @Autowired
    UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> updateUser(@PathVariable String id, @Valid @RequestBody UpdateRequest request) throws Exception {

        User user = userService.updateUser(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new APIResponse<Object>(HttpStatus.OK, true, user,"Updated successfully."));
    }

    @GetMapping("/coins")
    public ResponseEntity<APIResponse<Object>> fetchAndSaveCoinData(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                    @RequestParam("symbols") String symbols) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found "));
        CoinData coinData = coinDataService.fetchAndSaveCoinData(user, symbols);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new APIResponse<Object>(HttpStatus.OK, true, coinData,"Updated successfully."));
    }
}
