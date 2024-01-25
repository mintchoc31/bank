package com.tenco.bank.repository.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// 기본 생성자 만드는 annotation
@NoArgsConstructor
// 사용자 정의 생성자 만드는 annotation
@AllArgsConstructor
@Builder
public class User {
	
	private Integer id;
	private String username;
	private String password;
	private String fullname;
	private Timestamp createdAt;

}
