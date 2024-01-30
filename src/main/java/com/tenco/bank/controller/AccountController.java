package com.tenco.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.AccountSaveFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.service.AccountService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;

// 데이터를 반환하고 싶으면 restcontroller
@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired // 가독성 때문에 씀
	private HttpSession session;

	@Autowired
	private AccountService accountService;

	// 페이지 요청
	// http://localhost:80/account/save;
	/**
	 * 계좌 생성 페이지 요청
	 * 
	 * @return saveForm.jsp
	 */
	@GetMapping("/save")
	public String savePage() {
		// 인증 검사
		// 다운 캐스팅
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if (principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요.", HttpStatus.UNAUTHORIZED);
		}

		return "account/saveForm";

	}
	
	/**
	 * 계좌 생성 처리
	 * @param dto
	 * @return list.jsp
	 */
	@PostMapping("/save") // body --> String --> 파싱(DTO)
	public String saveProc(AccountSaveFormDto dto) {

		// 1. 인증검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if (principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요.", HttpStatus.UNAUTHORIZED);
		}
		// 2. 유효성 검사
		if (dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new CustomRestfulException("계좌 번호를 입력하세요", HttpStatus.BAD_REQUEST);
		}
		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("계좌 비밀번호를 입력하세요", HttpStatus.BAD_REQUEST);
		}
		if (dto.getBalance() == null || dto.getBalance() < 0) {
			throw new CustomRestfulException("잘못된 금액입니다.", HttpStatus.BAD_REQUEST);
		}
		// 3. 서비스 호출
		
		accountService.createAccount(dto, principal.getId());
		
		// 4. 응답 처리
		return "redirect:/user/sign-in";
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	// http://localhost:80/account/list or http://localhost:80/account/
	@GetMapping({ "/list", "/" }) // 매핑을 두개 만드려면 중괄호로 감싼다
	public String listPage(Model model) { // 콜백으로 개체를 받음?
		// 1. 인증검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if (principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요.", HttpStatus.UNAUTHORIZED);
		}
		
		List<Account> accountList =
				accountService.readAccountListByUserId(principal.getId());
		
		if(accountList.isEmpty()) {
			model.addAttribute("accountList", null);
		} else {
			model.addAttribute("accountList", accountList);
		}
		
		return "account/list";
	}

}
