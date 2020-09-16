package com.mbel.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    
	@Query("FROM UserEntity u WHERE u.userId IN :ids")
	public List<UserEntity> getByUserIds(@Param("ids") List<Integer> ids);
}

