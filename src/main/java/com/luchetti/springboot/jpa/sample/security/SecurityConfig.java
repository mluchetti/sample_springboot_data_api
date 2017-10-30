package com.luchetti.springboot.jpa.sample.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/admin/health").authenticated()
				.antMatchers("/people/**","/persons/**","/admin/health","/profile/**").hasRole("USER")		
				.antMatchers("/admin/**").hasRole("ADMIN")	
			
			//this allows Basic Authentication
			.and().httpBasic()
			
			//this prompts for a login if not yet authenticated
			.and().formLogin()
			
			//forces SSL for any request
			//.and().requiresChannel().anyRequest().requiresSecure()
			
			//TODO We'll have to investigate this option further when enabling secure requests; https://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html 
			//without this disabled, nothing other than GET methods work.
			.and().csrf().disable()
			;	
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		//TODO implement LDAP or some other secure
//		.ldapAuthentication()
//		.and()
		.inMemoryAuthentication()
			.withUser("admin").password("password").roles("ADMIN","USER")
			.and()
			.withUser("user").password("user").roles("USER");
	}
}