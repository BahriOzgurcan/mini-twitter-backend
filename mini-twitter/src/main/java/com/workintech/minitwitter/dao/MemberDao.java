package com.workintech.minitwitter.dao;

import com.workintech.minitwitter.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberDao extends JpaRepository<Member, Integer> {

    @Query("SELECT m FROM Member m WHERE m.email=:email ")
    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.username=:username ")
    Optional<Member> findByUsername(String username);
}
