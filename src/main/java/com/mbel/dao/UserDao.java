package com.mbel.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mbel.model.UserEntity;

@Repository
public interface UserDao extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
