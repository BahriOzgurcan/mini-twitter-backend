package com.workintech.minitwitter.controller;

import com.workintech.minitwitter.dto.MemberResponseDto;
import com.workintech.minitwitter.entity.Member;
import com.workintech.minitwitter.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public List<MemberResponseDto> get(){
        return memberService.find();
    }

    @GetMapping("/email/{email}")
    public Member getByEmail(@PathVariable String email){
        return memberService.findMemberByEmail(email);
    }

    @GetMapping("/username/{username}")
    public UserDetails getByUsername(@PathVariable String username){
        return memberService.loadUserByUsername(username);
    }
}
