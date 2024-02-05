package com.tenco.bank.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tenco.bank.dto.BoardDto;
import com.tenco.bank.dto.Todo;

@Controller
public class RestControllerTest {
	// 클라이언트에서 접근하는 주소 설계
	@GetMapping("/exchange-test")
	public ResponseEntity<?> myTest1() {

		// 여기서 다른 서버로 자원을 요청한 다음
		// 다시 클라이언트에게 자원을 내려주자
		
		// 1. URI 객체 만들기
		URI uri = UriComponentsBuilder
				.fromUriString("https://jsonplaceholder.typicode.com")
				.path("/posts")
				.encode()
				.build()
				.toUri();
		
		// 2. 객체 생성
		RestTemplate restTemplate = new RestTemplate();
		// HTTP 통신 --> HTTP 메세지 헤더, 바디를 구성해서 보내야 한다.
		
		// 헤더 구성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json; charset=UTF-8");
		
		// 바디 구성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("title", "블로그 포스트1");
		params.add("body", "후미진 어느 언덕에서 도시락 소풍");
		params.add("userId", "1");
		
		// 헤더와 바디 결합
		HttpEntity<MultiValueMap<String, String>> requestMessage
			= new HttpEntity<>(params, headers);
		
		// http 요청처리
		// 파싱 처리 해야함
		ResponseEntity<String> response
			= restTemplate.exchange(uri, HttpMethod.POST, requestMessage,
					String.class);
		
		// http://localhost:80/exchange-test
		System.out.println("headers " + response.getHeaders());
		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	}
	
		// 주소 :
		// localhost:80/todos/3
		@GetMapping("/todos/{id}")
		public ResponseEntity<?> test2(@PathVariable Integer id) {
			// URI uri = new URI("https://jsonplaceholder.typicode.com/");
			URI uri = UriComponentsBuilder
					.fromUriString("https://jsonplaceholder.typicode.com")
					.path("/posts")
					.path("+id")
					.encode()
					.build()
					.toUri();
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Todo> response = restTemplate.getForEntity(uri, Todo.class); // GET 방식 요청 응답은?
			System.out.println(response.getHeaders());
			System.out.println(response.getBody());
			System.out.println(response.getBody().getTitle());
			
			return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
				
			}
			// 주소 :
			// localhost:80/my-test3/1
			@GetMapping("/my-test3/{id}")
			public ResponseEntity<?> test3(@PathVariable Integer id) {
	
				URI uri = UriComponentsBuilder
									.fromUriString("https://jsonplaceholder.typicode.com")
									.path("/posts")
									.path("/"+ id)
									.encode()
									.build()
									.toUri();
	
				RestTemplate restTemplate = new RestTemplate();
	
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-type", "application/json; charset=UTF-8");
	
				Map<String, String> params = new HashMap<>();
				params.put("title", "foo");
				params.put("body", "bar");
				params.put("userId", "1");
	
				// 헤더와 바디 결합 
				HttpEntity<Map<String, String>> requestMessage 
					= new HttpEntity<>(params, headers);
				// HTTP 요청 처리 
				// 파싱 처리 해야 한다. 
				ResponseEntity<BoardDto> response 
						=  restTemplate.exchange(uri, HttpMethod.PUT, requestMessage, 
														BoardDto.class);
				BoardDto boardDto = response.getBody();
				System.out.println("TEST : BDTO " + boardDto.toString());
				return ResponseEntity.status(HttpStatus.OK).body(response.getBody());

				
		
	}
	
	
}