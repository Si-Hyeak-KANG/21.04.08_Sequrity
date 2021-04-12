package com.sp.fc.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Order(1)//두개 이상의 필터체인을 구성하고 싶을 땐 다른 SecurityConfig를 만들어 주면 된다. 대신 순서가 중요.
@EnableWebSecurity(debug = true)//Request가 올 때마다 이 Request는 어떤 필터체인을 타고있어
@EnableGlobalMethodSecurity(prePostEnabled = true)//이제부터 prePost로 권한을 체크하겠다
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//  application.yml에는 user를 1명만 추가가 가능하기 때문에 여기서 추가
//  또한 Authentication provider을 추가하게 되면 application.yml 설정은 적용 안됨

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                .username("user2")
                .password(passwordEncoder().encode("2222"))//사용자의 패스워드를 인코딩하지 않으면 오류 발생
                .roles("USER")
                )
                .withUser(User.builder()
                .username("admin")
                .password(passwordEncoder().encode("3333"))
                .roles("ADMIN"))
                ;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
//  spring security는 기본적으로 다 막고 시작
//  홈페이지는 누구나 접근할 수 있도록 하려면

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //특정 request에 필터체인 적용
        http.antMatcher("/admin");

        //어떤 request이든 인증받은 상태에서 접근 해라
        http.authorizeRequests((requests) ->
                requests.antMatchers("/").permitAll()//하지만 홈페이지는 모두 접근 허용한다
                        .anyRequest().authenticated()
        );
        http.formLogin();
        http.httpBasic();
        //disable() 필터체인을 쓰지 않겠다.
    }
}
