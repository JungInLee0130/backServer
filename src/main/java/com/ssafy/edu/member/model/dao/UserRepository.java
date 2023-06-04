package com.ssafy.edu.member.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.edu.member.model.dto.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	
}
