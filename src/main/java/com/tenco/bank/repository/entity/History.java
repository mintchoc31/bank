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
public class History {
	
	private Integer id;
	private Long amount; // int -> Long
	private Integer wAccountId; // integer -> Long
	private Integer dAccountId; // integer -> Long
	private Long wBalance; // Long -> Integer
	private Long dBalance; // Long -> Integer
	private Timestamp createdAt;

	
}
