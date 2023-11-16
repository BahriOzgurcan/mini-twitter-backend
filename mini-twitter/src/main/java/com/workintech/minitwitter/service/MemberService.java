package com.workintech.minitwitter.service;

import com.workintech.minitwitter.dao.MemberDao;
import com.workintech.minitwitter.dto.MemberResponseDto;
import com.workintech.minitwitter.entity.Member;
import com.workintech.minitwitter.exceptions.MemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService implements UserDetailsService {
    private MemberDao memberDao;

    @Autowired
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberDao.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User is not valid"));
    }

    public Member findMemberByEmail(String email) {
        Optional<Member> existingMember = memberDao.findByEmail(email);
        if(existingMember.isPresent()){
            return existingMember.get();
        }
        throw new MemberException("No account registered with this email: " + email, HttpStatus.BAD_REQUEST);
    }


    public List<MemberResponseDto> find() {
    List<Member> members = memberDao.findAll();
        if(members.isEmpty()){
            throw new MemberException("There are no accounts in the database.", HttpStatus.BAD_REQUEST);
        }
    return members.stream()
            .map(member -> new MemberResponseDto(member.getId(), member.getUsername(), member.getEmail()))
            .collect(Collectors.toList());
    }
}
