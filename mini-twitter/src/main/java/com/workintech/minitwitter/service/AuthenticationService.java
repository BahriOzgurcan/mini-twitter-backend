package com.workintech.minitwitter.service;

import com.workintech.minitwitter.dao.MemberDao;
import com.workintech.minitwitter.dao.RoleDao;
import com.workintech.minitwitter.entity.Member;
import com.workintech.minitwitter.entity.Role;
import com.workintech.minitwitter.exceptions.TweetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {

    private MemberDao memberDao;
    private RoleDao roleDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(MemberDao memberDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public Member register(String email, String password, String username) {
        try {
            Optional<Member> foundMemberByEmail = memberDao.findByEmail(email);
            if (foundMemberByEmail.isPresent()) {
                throw new RuntimeException("User with given username already exists, please login.");
            }
            Optional<Member> foundMemberByUsername = memberDao.findByUsername(username);
            if (foundMemberByUsername.isPresent()) {
                throw new RuntimeException("User with given username already exists, please choose another.");
            }

            String encodedPassword = passwordEncoder.encode(password);
            Role memberRole = roleDao.findByAuthority("USER").get();

            List<Role> roleList = new ArrayList<>();
            roleList.add(memberRole);

            Member member = new Member();
            member.setEmail(email);
            member.setUsername(username);
            member.setPassword(encodedPassword);
            member.setAuthorities(roleList);

            return memberDao.save(member);
        } catch (RuntimeException e) {
            throw new TweetException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new TweetException("An unexpected error occurred during registration.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Member loginWithUsername(String username, String password) {
        Optional<Member> optionalMember = memberDao.findByUsername(username);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            boolean isSame = passwordEncoder.matches(password, member.getPassword());
            if (isSame) {
                return member;
            }
            throw new RuntimeException("Invalid Credentials");
        }
        throw new RuntimeException("Invalid Credentials");
    }


    public Member loginWithEmail(String email, String password) {
        Optional<Member> optionalMember = memberDao.findByEmail(email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            boolean isSame = passwordEncoder.matches(password, member.getPassword());
            if (isSame) {
                return member;
            }
            throw new RuntimeException("Invalid Credentials");
        }
        throw new RuntimeException("Invalid Credentials");
    }
}

