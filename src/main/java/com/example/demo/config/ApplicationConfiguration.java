package com.example.demo.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.Dao.UserDao;
import lombok.RequiredArgsConstructor;
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
	private final UserDao repo;
	@Bean
	 UserDetailsService userDetailsService() {
		return username->repo.findByUsername(username)
				 .orElseThrow(()->new UsernameNotFoundException("User not found"));
	} 
	@Bean
	 AuthenticationProvider authenticationProvider() { 	 	 	 	
		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	@Bean
	 AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	@Bean
	 PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();  
	}
}