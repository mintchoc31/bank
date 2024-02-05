package com.tenco.bank.dto;

import lombok.Data;

@Data
public class BoardDto {

	private Integer id;
	private String title;
	private String body;
	private Integer userId;
	private boolean completed;
}