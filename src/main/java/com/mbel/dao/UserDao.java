package com.mbel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbel.model.UserEntity;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Integer> {
    UserEntity findByEmail(String email);
}
