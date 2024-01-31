package com.tenco.bank.dto;

import lombok.Data;

// 파싱하기 위해 setter가 필요함
@Data
public class WithdrawFormDto {

	private Long amount; // 출금 금액
	private String wAccountNumber; // 출금 계좌번호
	private String wAccountPassword; // 출금 계좌 비밀번호
}
