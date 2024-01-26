package com.tenco.bank.handler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomPageException extends RuntimeException {
	
	// 상태코드
	private HttpStatus httpStatus;
	
	public CustomPageException(String message, HttpStatus httpStatus) {
		// super: 부모 클래스 호출
		super(message);
		this.httpStatus = httpStatus;
	}
	
	// 사용하는 시점에 활용 가능
	// new CustomPageException("바보야", HttpStatus.ok);
}
