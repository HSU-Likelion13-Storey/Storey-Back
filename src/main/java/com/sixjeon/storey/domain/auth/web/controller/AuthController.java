package com.sixjeon.storey.domain.auth.web.controller;

import com.sixjeon.storey.domain.auth.service.AuthService;
import com.sixjeon.storey.domain.auth.web.dto.LoginReq;
import com.sixjeon.storey.domain.auth.web.dto.LoginRes;
import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.auth.web.dto.SignupRes;
import com.sixjeon.storey.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<?>> signUp(@Valid @RequestBody SignupReq signupReq) {
        SignupRes signupRes = authService.signUp(signupReq);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.created(signupRes));
    }

    @PostMapping("/owner/login")
    public ResponseEntity<SuccessResponse<?>> ownerLogin(@Valid @RequestBody LoginReq loginReq) {
        LoginRes loginRes = authService.ownerSignin(loginReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(loginRes));
    }

    @PostMapping("/user/login")
    public ResponseEntity<SuccessResponse<?>> userLogin(@Valid @RequestBody LoginReq loginReq) {
        LoginRes loginRes = authService.userSignin(loginReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(loginRes));
    }

    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<?>> refreshToken(@RequestBody String refreshToken) {
        LoginRes loginRes = authService.refreshAccessToken(refreshToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(loginRes));
    }

}
