package com.xiecode.config;

import com.xiecode.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Resource
    UserAuthService userAuthService;

    @Resource
    PersistentTokenRepository repository;

    @Bean
    public PersistentTokenRepository jdbcRepository(@Autowired DataSource dataSource){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();  //使用基于JDBC的实现
        repository.setDataSource(dataSource);   //配置数据源
        //repository.setCreateTableOnStartup(true);   //启动时自动创建用于存储Token的表（建议第一次启动之后删除该行）
        return repository;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()//首先需要配置哪些请求会被拦截，哪些请求必须具有什么角色才能访问
                .antMatchers("/static/**").permitAll()//静态资源，使用permitAll来运行任何人访问（注意一定要放在前面）
                .antMatchers("/index").hasAnyRole("user", "admin")
                .anyRequest().hasRole("admin")//所有请求必须登陆并且是user角色才可以访问（不包含上面的静态资源）
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
                .csrf().disable()//关闭csrf验证
                //这里使用的是直接在内存中保存的TokenRepository实现
                //TokenRepository有很多种实现，InMemoryTokenRepositoryImpl直接基于Map实现的，缺点就是占内存、服务器重启后记住我功能将失效
                //后面我们还会讲解如何使用数据库来持久化保存Token信息
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                .tokenRepository(repository);



    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userAuthService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
