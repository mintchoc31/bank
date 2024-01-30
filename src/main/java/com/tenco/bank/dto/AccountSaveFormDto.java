package com.tenco.bank.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountSaveFormDto {
	
	private String number;
	private String password;
	private Long balance;

}
