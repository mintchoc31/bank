package com.tenco.bank.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tenco.bank.dto.KakaoProfile;
import com.tenco.bank.dto.OAuthToken;
import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.service.UserService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired // DI 처리 
	private UserService userService;
	
	@Autowired
	private HttpSession httpSession;
	
	/**
	 * 회원 가입 페이지 요청 
	 * @return signUp.jsp 파일 리턴
	 */
	@GetMapping("/sign-up")
	public String signUpPage() {
	   //   prefix: /WEB-INF/view/
	   //   suffix: .jsp
		return "user/signUp";
	}
	
	/**
	 * 회원 가입 요청 
	 * @param dto
	 * @return 로그인 페이지(/sign-in)
	 */
	@PostMapping("/sign-up")
	public String signProc(SignUpFormDto dto) {
		
		System.out.println("dto : " + dto.toString());
		System.out.println(dto.getCustomFile().getOriginalFilename());
		// 1. 인증검사 x 
		// 2. 유효성 검사 
		if(dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfulException("username을 입력 하세요", 
					HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("password을 입력 하세요", 
					HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getFullname() == null || dto.getFullname().isEmpty()) {
			throw new CustomRestfulException("fullname을 입력 하세요", 
					HttpStatus.BAD_REQUEST);
		}		
		
		// 파일 업로드
		MultipartFile file = dto.getCustomFile();
		System.out.println("file" + file.getOriginalFilename());
		if(file.isEmpty() == false) {
			// 사용자가 이미지를 업로드했다면 기능 구현
			// 파일 사이즈 체크 
			// 20MB 
			if(file.getSize() > Define.MAX_FILE_SIZE) {
				throw new CustomRestfulException("파일 크기는 20MB를 넘을 수 없습니다.", HttpStatus.BAD_REQUEST);
			}
			
			// 서버 컴퓨터에 파일을 넣을 디렉토리가 있는지 검사
			String saveDirectory = Define.UPLOAD_FILE_DIRECTORY;
			// 폴더가 없다면 오류 발생(파일 생성시)
			File dir = new File(saveDirectory);
			if(dir.exists() == false) {
				dir.mkdir(); // 폴더가 없으면 폴더 생성
			}
			
			// 파일 이름 (중복 처리 예방)
			UUID uuid = UUID.randomUUID();
			String fileName = uuid + "_" + file.getOriginalFilename();
			System.out.println("fileName : " + fileName);
			// C:\\work_spring\\upload
			String uploadPath = Define.UPLOAD_FILE_DIRECTORY + File.separator + fileName;
			File destination = new File(uploadPath);
			
			try {
				file.transferTo(destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 객체 상태 변경
			dto.setOriginFileName(file.getOriginalFilename());
			dto.setUploadFileName(fileName);
		}
		
//		userService.createUser(dto);
		return "redirect:/user/sign-in"; 
	}
	
	/**
	 * 로그인 페이지 요청 
	 * @return dto
	 */
	@GetMapping("/sign-in")
	public String signInPage() {
		return "user/signIn";
	}
	
	/**
	 * 로그인 요청 처리 
	 * @param SignInFormDto 
	 * @return account/list.jsp
	 */
	@PostMapping("/sign-in")
	public String signInProc(SignInFormDto dto) {
		// 1. 유효성 검사
		if(dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfulException("username을 입력하시오", 
					HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("password을 입력하시오", 
					HttpStatus.BAD_REQUEST);
		}
		// 서비스 호출 예정 
		User user = userService.readUser(dto);
		httpSession.setAttribute(Define.PRINCIPAL, user);
		return "redirect:/account/list";
	}
		
		// 로그아웃 기능 만들기
		@GetMapping("/logout")
		public String logout() {
			httpSession.invalidate();
			return "redirect:/user/sign-in";
	}
		// 카카오 로그인
		// http://localhost:80/user/kakao-callback?code="xxxxxxxxx"
		@GetMapping("/kakao-callback")
		public String kakaoCallback(@RequestParam String code) {

			// POST 방식,  Header 구성, body 구성
			RestTemplate rt1 = new RestTemplate();

			// 헤더 구성
			HttpHeaders headers1 = new HttpHeaders();
			headers1.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			
			// body 구성
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", "authorization_code"); // 카카오 디벨로퍼에서 복사
			params.add("client_id", "c7a8d368247a723ba56f002c40ad7d4b");
			params.add("redirect_uri", "http://localhost:80/user/kakao-callback");
			params.add("code", code);
			
			// 헤더 + 바디 결합
			HttpEntity<MultiValueMap<String, String>> reqMsg
				= new HttpEntity<>(params, headers1);
			
			ResponseEntity<OAuthToken> response =  rt1.exchange("https://kauth.kakao.com/oauth/token", 
					HttpMethod.POST, reqMsg, OAuthToken.class); // POST로 던짐
			
			// 다시 요청하기 -- 인증 토큰 사용자 정보 요청
			RestTemplate rt2 = new RestTemplate();
			
			// 헤더
			HttpHeaders headers2 = new HttpHeaders();
			headers2.add("Authorization","Bearer " + response.getBody().getAccessToken());
			headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			// 바디 x - GET 요청이라 바디 쓰지 않음
			
			// 결합 --> 요청
			HttpEntity<MultiValueMap<String, String>> kakaoInfo
			 	= new HttpEntity<>(headers2);
			ResponseEntity<KakaoProfile> response2 =
			rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoInfo, KakaoProfile.class);
			
			System.out.println(response2.getBody());
		
			KakaoProfile kakaoProfile = response2.getBody();
			
			// 최초 사용자 판단 여부 -- 사용자 이름 (username) 존재 여부 확인
			// 우리 사이트 --> 카카오
			SignUpFormDto dto = SignUpFormDto.builder()
					.username("OAuth_" + kakaoProfile.getProperties().getNickname())
					.fullname("Kakao")
					.password("asd1234")
					.build();
					// 소셜 로그인 패스워드 주의  - 보안처리 필요		
			User oldUser = userService.readUserByUserName(dto.getUsername());
			// null <-- 
			if(oldUser == null) {
				userService.createUser(dto);
				/////////////////////////////
				oldUser = new User();
				oldUser.setUsername(dto.getUsername());
				oldUser.setFullname(dto.getFullname());
				
			}
			oldUser.setPassword(null);
			// 로그인 처리
			httpSession.setAttribute(Define.PRINCIPAL, oldUser);
			
			// 단 최초 요청 사용자라 -->  회원 후 로그인 처리
			
			// dto 설계
			return "redirect:/account/list";
			
			
		}
		@GetMapping("/naver-callback")
		@ResponseBody
		public String naverCallback(@RequestParam String code, @RequestParam String state) {
			RestTemplate restTemplate = new RestTemplate();
			
			// 헤더
			HttpHeaders headers = new HttpHeaders();
			
			// 헤더와 바디 담은 객체
			HttpEntity<MultiValueMap<String, String>> naverInfo = new HttpEntity<>(headers);
			String url = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=kLY23tlHi58ddXlDIiYG&client_secret=7SmcUu17Yk&"
					+ "code=" + code
					+ "&state=" + state ;
			ResponseEntity<String> navToken = restTemplate.exchange(url, HttpMethod.POST, naverInfo, String.class);
			//todo 토큰값 받아오는 dto 만들어서 restTemplate 제네릭 변경하기.
			// 토큰 dto.getAccessToken()으로 토큰 받아와서 Authorization에 추가하기
			
			
			RestTemplate restTemplate2 = new RestTemplate();
			
			HttpHeaders headers2 = new HttpHeaders();
			// headers2.add("Authorization", "Bearer " + 접근토큰 AAAAPIuf0L+qfDkMABQ3IJ8heq2mlw71DojBj3oc2Z6OxMQESVSrtR0dbvsiQbPbP1/cxva23n7mQShtfK4pchdk/rc");
			HttpEntity<MultiValueMap<String, String>> naverInfo2 = new HttpEntity<>(headers2);
			ResponseEntity<String> navInfo = restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.POST, naverInfo2, String.class); 
			
			
			return navInfo.getBody();
			
		}
}