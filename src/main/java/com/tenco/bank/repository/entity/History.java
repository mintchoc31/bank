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
	private Long w_account_id; // integer -> Long
	private Long d_account_id; // integer -> Long
	private Integer w_balance; // Long -> Integer
	private Integer d_balance; // Long -> Integer
	private Timestamp createdAt;

}
