package com.workintech.minitwitter.controller;

import com.workintech.minitwitter.dto.LoginRequestWithEmail;
import com.workintech.minitwitter.dto.LoginRequestWithUsername;
import com.workintech.minitwitter.dto.MemberResponseDto;
import com.workintech.minitwitter.dto.RegistrationMember;
import com.workintech.minitwitter.entity.Member;
import com.workintech.minitwitter.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public MemberResponseDto register(@RequestBody RegistrationMember registrationMember){
        Member member = authenticationService.register(registrationMember.email(), registrationMember.password(), registrationMember.username());
     return new MemberResponseDto(member.getId(), member.getUsername(), member.getEmail());
    }

    @PostMapping("/loginWithUsername")
    public MemberResponseDto loginWithUsername(@RequestBody LoginRequestWithUsername loginRequestWithUsername){
        Member member = authenticationService.loginWithUsername(loginRequestWithUsername.username(), loginRequestWithUsername.password());
        return new MemberResponseDto(member.getId(), member.getUsername(), member.getEmail());
    }

    @PostMapping("/loginWithEmail")
    public MemberResponseDto loginWithEmail(@RequestBody LoginRequestWithEmail loginRequestWithEmail){
        Member member = authenticationService.loginWithEmail(loginRequestWithEmail.email(), loginRequestWithEmail.password());
    return new MemberResponseDto(member.getId(), member.getUsername(), member.getEmail());
    }
}

