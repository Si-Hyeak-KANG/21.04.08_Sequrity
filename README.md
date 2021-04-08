# 21.04.08_Sequrity
Spring_sequrity에 대해 공부하고 인증, 권한, 로그인에 대해 공부했습니다.


스프링 시큐리티 구조

1. 서블릿 컨테이너

- 톰켓과 같은 웹 어플레케이션을 서블릿 컨테이너라 함.
(이런 웹 애플리케이션(J2EE Application)은 기본적으로 필터와 서블릿으로 구성)

필터는 체인처럼 엮여있기 때문에 필터 체인이라고도 불림.
모든 Request는 이 필터 체인을 반드시 커쳐야만 서블릿 서비스에 도착하게 됨.

DispatchServlet -> url과 매칭이 되는 메소드들을 실행

2. 스프링 시큐리티

-스프링 시큐리티는 DelegatingFilterProxy라는 필터를 만들어
메인 필터체인에 끼워넣고, 그 아래 다시 SecurityFilterChain 그룹을 등록한다.

Proxy(대리자) 에서 선택적 필터 체인 사용
-> WebSecurityConfigurerAdapter : 필터 체인을 구성하는 configuration 클래스
: 이 필터체인은 반드시 한개 이상이고, url 패턴에 따라 적용되는 필터체인을 다르게 할 수 있음. 본래의 메인 필터를 반드시 통과해야만 서블릿에 들어갈 수 있는 단점을 본완하기 위해 필터체인 Proxy를 두었다.

web resource 의 경우 패텅을 따르더라도 필터를 무시(ignore)하고 통과시켜주기도 한다.

시큐리티 필터들
	1)HeaderWriterFilter : Http 해더를 검사.
	2)CorsFilter : 허가된 사이트나 클라이언트 요청인가?
	3)CsrfFilter : 내가 내보낸 리소스에서 올라온 요청인가
	4)LogoutFilter : 지금 로그아웃하겠다고 하는건가
	*5) UsernamePasswordAuthenticationFilter : username/ password로 로그인을 하는가? 만약 로그인이면 여기서 처리하고 가야 할 페이지로 보내줄게
	6) ConcurrentSessionFilter : 여기저기서 로그인 하는것 허용할 것인가?
	7) BearerTokenAuthenticationFilter : Authenticcation 해더에 Bearer토큰이 오면 인증 처리 해줄게
	8) BasicAuthenticationFilter : Authentication 해더에 Basic 토큰을 주면 검사해서 인정처리 해줄게.
	9) RequestCacheAwareFilter : 방금 요청한 request 이력이 다음에 필요할 수 있으니 캐시에 담아놓을게
	10) SecurityContextHolderAwarRequestFilter : 보안 관련 Servelt 3 스펙을 지원하기 위한 필터라고 한다.
	11) RememberMeAuthenticationFilter : 아직 Authentication 인증이 안된 경우라면 RememberMe 쿠키를 검사해서 인증 처리해줄게
	12) AnonymouseAuthenticationFilter : 아직도 인증이 안되었으면 너느 Anonymouse 사용자야
	13) SessionManagementFilter : 서버에서 지정한 세션정책을 검사할게
	14) ExceptionTranslationFilter : 나 이후에 인증이나 권한 예외가 발생하면 내가 잡아서 처리해 줄게.
	15) FilterSecurityInterceptor: 여기까지 살아서 왔다면 인증이 있다는 거니, 니가 들어가려고 하는 request에 들어갈 자격이 있는지 그리고 리턴한 결과를 너에게 
				                보내줘도 되는건지 마지막으로 내가 점검해줄게
	
	등등.. 이러한 필터가 있으며, 만들 수도 있다. 또는 OAUTH

