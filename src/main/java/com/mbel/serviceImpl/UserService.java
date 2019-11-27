package com.mbel.serviceImpl;

import java.util.List;

import com.mbel.dto.UserDto;
import com.mbel.model.UserEntity;
public interface UserService {

    UserEntity save(UserDto user);
    
    List<UserEntity> findAll();
    
    void delete(long id);
    
    UserEntity findOne(String username);
    

    UserEntity findById(Long id);
}
