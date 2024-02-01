package com.tenco.bank.dto;

import lombok.Data;

@Data
public class TransferFormDto {
	
	private Long amount;// 이체 금액
	private String wAccountNumber; // 출금 계좌 번호
	private String wAccountPassword;// 출금 계좌 비밀 번호
	private String dAccountNumber; // 입금(이체) 계좌번호
}
