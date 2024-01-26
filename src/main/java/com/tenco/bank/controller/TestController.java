package com.tenco.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test1") // 대문
public class TestController {
	
	// 주소 설계
	// http://localhost:80(기본 포트라 80 생략가능)/test/main
	@GetMapping("/main")
	public String mainPage() {
		// 인증 검사 
		// 유효성 검사 
		// 뷰 리졸브 --> 해당하는 파일 찾아 (data)
		// yml에 prefix, suffix를 설정 했기 때문에 web-inf~(pre-)/~.jsp(suf-)는 자동으로 설정됨
		return "layout/main";
	}

}
 