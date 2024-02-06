<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- header 가져오기 -->
<%@ include file="/WEB-INF/view/layout/header.jsp" %>
    
	<div class="col-sm-8">
		<h2>로그인</h2>
		<h5>어서오세요 환영합니다</h5>
		
		<form action="/user/sign-in" method="post" > 
	  <div class="form-group">
	    <label for="username">username:</label>
	    <input type="text" name="username" class="form-control" placeholder="Enter username" id="username">
	  </div>
	  <div class="form-group">
	    <label for="pwd">password:</label>
	    <input type="password" name="password" class="form-control" placeholder="Enter password" id="pwd">
	  </div>
	  
	  <button type="submit" class="btn btn-primary">로그인</button>
	  <!-- 카카오 인가코드 요청 -->
	  <a href="
	  https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=c7a8d368247a723ba56f002c40ad7d4b&redirect_uri=http://localhost:80/user/kakao-callback">
	  	<img alt="" src="/images/kakao_login_small.png" width="75" height="38">
	  </a>
	  <a href="
	  https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=kLY23tlHi58ddXlDIiYG&state=STATE_STRING&redirect_uri=http://localhost:80/user/naver-callback">
	 	 <img alt="" src="/images/btnG_short.png" width="111px" height="39.9967px">
	 	 
	  </a>
	</form>

	</div>
</br>
</div>
    
<!-- footer 가져오기 -->
	<%@ include file="/WEB-INF/view/layout/footer.jsp" %>