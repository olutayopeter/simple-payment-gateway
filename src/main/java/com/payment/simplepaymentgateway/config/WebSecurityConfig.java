package com.payment.simplepaymentgateway.config;


import com.payment.simplepaymentgateway.util.AESUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use NoOpPasswordEncoder since basic authentication is used
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // You can change the key size as needed
        SecretKey secretKey = keyGenerator.generateKey();

        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/swagger-ui.html","/swagger-ui/**", "/swagger-resources/**", "/api-docs/", "/api-docs/**","/v2/api-docs", "/webjars/**", "/h2-console/**").permitAll()
                .antMatchers("/v1/payment").permitAll() // Permit access to your /v1/payment path
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
        http.headers().frameOptions().disable();
    }
}
