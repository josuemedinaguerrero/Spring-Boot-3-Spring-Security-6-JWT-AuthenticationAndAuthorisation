package com.josue.security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
      return httpSecurity
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> {
             auth.requestMatchers("/hello").permitAll();
             auth.anyRequest().authenticated();
          })
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .httpBasic()
          .and()
          .build();
   }

   @Bean
   UserDetailsService userDetailsService() {
      InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
      manager.createUser(User.withUsername("Vanessa")
          .password("123456")
          .roles()
          .build());
      return manager;
   }

   @Bean
   PasswordEncoder passwordEncoder() {
      return NoOpPasswordEncoder.getInstance();
   }

   @Bean
   AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
      return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
          .userDetailsService(userDetailsService())
          .passwordEncoder(passwordEncoder)
          .and().build();
   }

}
