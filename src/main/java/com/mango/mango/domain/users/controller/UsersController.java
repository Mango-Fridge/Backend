package com.mango.mango.domain.users.controller;

import com.mango.mango.domain.users.dto.request.UserSignUpRequestDto;
import com.mango.mango.domain.users.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    
    private final UsersService usersService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        Long userId = usersService.signUp(requestDto);
        return ResponseEntity.ok(userId);
    }
} 