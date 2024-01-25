package com.tenco.bank.repository.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
	
	private Integer id;
	private String number; // int->String
	private String password;
	private Long balance; // int -> Long
	private Integer user_id; // String -> Integer
	private Timestamp createdAt;
	
	// 출금 기능
	// 입금 기능
	// 패스워드 체크
	// 잔액 여부 확인 기능
	// 계좌 소유자 확인 기능
}
