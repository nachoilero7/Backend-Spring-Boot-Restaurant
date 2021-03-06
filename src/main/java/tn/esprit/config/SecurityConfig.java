package tn.esprit.config;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import tn.esprit.services.UserSecurityService;

@Configuration
@EnableGlobalMethodSecurity (securedEnabled = true, prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserSecurityService userSecurityService;
	
	private BCryptPasswordEncoder passwordEncoder(){
		return SecurityUtility.passwordEncoder();
	}
		
    private static final String[] PUBLIC_MATCHES = {
    		"/css/**",
    		"/js/**",
    		"/image/**",
    		"/user/**"
    };
    
    @Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
	}
    
    @Override
	protected void configure(HttpSecurity http) throws Exception {
		
    	http.csrf().disable().cors().disable().httpBasic().and().authorizeRequests()
    	.antMatchers(PUBLIC_MATCHES).permitAll().anyRequest().authenticated();
	}
    
    @Bean
    public HttpSessionStrategy httpSessionStrategy(){
    	return new HeaderHttpSessionStrategy();
    }
    
    
    

}
