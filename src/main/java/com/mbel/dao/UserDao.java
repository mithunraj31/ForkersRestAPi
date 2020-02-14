package com.mbel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mbel.model.UserEntity;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Integer> {
    UserEntity findByEmail(String email);

    @Transactional
	@Modifying
    @Query(value="INSERT INTO `user_role` (`user_userid`,`role_roleid`)VALUES(?1,?2) ", nativeQuery = true)
	public void saveRelation(int userId, int roleId);
}

