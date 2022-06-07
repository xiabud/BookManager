package com.xiecode.config;

import com.xiecode.service.UserAuthService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Resource
    UserAuthService userAuthService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//首先需要配置哪些请求会被拦截，哪些请求必须具有什么角色才能访问
                .antMatchers("/static/**").permitAll()//静态资源，使用permitAll来运行任何人访问（注意一定要放在前面）
                .antMatchers("/**").hasRole("user")//所有请求必须登陆并且是user角色才可以访问（不包含上面的静态资源）
                .and()
                .formLogin()//配置Form表单登陆
                .loginPage("/login")//登陆页面地址（GET）
                .loginProcessingUrl("/toLogin")//form表单提交地址（POST）
                .defaultSuccessUrl("/index")//登陆成功后跳转的页面，也可以通过Handler实现高度自定义
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")//退出登陆的请求地址
                .logoutSuccessUrl("/login")//退出登陆的请求地址
                .and()
                .csrf().disable();//关闭csrf验证

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userAuthService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
