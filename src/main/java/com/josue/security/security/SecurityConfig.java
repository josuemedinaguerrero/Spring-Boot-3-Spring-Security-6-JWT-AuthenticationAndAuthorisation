package com.josue.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.josue.security.security.filters.JwtAuthenticationFilter;
import com.josue.security.security.filters.JwtAuthorizationFilter;
import com.josue.security.security.jwt.JwtUtils;

@Configuration
public class SecurityConfig {

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsService userDetailsService;
  
  @Autowired
  private JwtAuthorizationFilter jwtAuthorizationFilter;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
    jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
    jwtAuthenticationFilter.setFilterProcessesUrl("/login");

    return httpSecurity
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers("/hello").permitAll();
          auth.anyRequest().authenticated();
        })
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // .httpBasic()
        // .and()
        .addFilter(jwtAuthenticationFilter)
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  // @Bean
  // UserDetailsService userDetailsService() {
  //    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
  //    manager.createUser(User.withUsername("Vanessa")
  //        .password("123456")
  //        .roles()
  //        .build());
  //    return manager;
  // }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
    return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
        //  .userDetailsService(userDetailsService())
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder)
        .and().build();
  }

  public static void main(String[] args) {
	System.out.println(new BCryptPasswordEncoder().encode("123456"));
  }

}
