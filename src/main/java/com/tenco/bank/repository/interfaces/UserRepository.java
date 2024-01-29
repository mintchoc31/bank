package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bank.repository.entity.User;

//interface + xml 연결
@Mapper // 반드시 선언
public interface UserRepository {
	//    결과집합
	public int insert(User user);
	public int updateById(User user);
	public int deleteById(Integer id);
	public User findById(Integer id);
	public List<User> findAll();

}
