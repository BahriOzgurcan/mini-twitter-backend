package com.workintech.minitwitter.dao;

import com.workintech.minitwitter.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role, Integer> {

    @Query("SELECT r FROM Role r WHERE r.authority=:authority ")
    Optional<Role> findByAuthority(String authority);

}
