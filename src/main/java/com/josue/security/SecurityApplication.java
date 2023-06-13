package com.josue.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.josue.security.models.ERole;
import com.josue.security.models.RoleEntity;
import com.josue.security.models.UserEntity;
import com.josue.security.repositories.UserRepository;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	CommandLineRunner init() {
		return args -> {
			UserEntity userEntity = UserEntity.builder()
				.email("test1@test.com")
				.username("Test 1")
				.password(passwordEncoder.encode("123456"))
				.roles(Set.of(RoleEntity.builder()
					.name(ERole.valueOf(ERole.ADMIN.name()))
					.build()))
				.build();

				UserEntity userEntity2 = UserEntity.builder()
				.email("test2@test.com")
				.username("Test 2")
				.password(passwordEncoder.encode("123456"))
				.roles(Set.of(RoleEntity.builder()
				.name(ERole.valueOf(ERole.USER.name()))
				.build()))
				.build();
				
				UserEntity userEntity3 = UserEntity.builder()
				.email("test3@test.com")
				.username("Test 3")
				.password(passwordEncoder.encode("123456"))
				.roles(Set.of(RoleEntity.builder()
				.name(ERole.valueOf(ERole.INVITED.name()))
				.build()))
				.build();
				
			userRepository.save(userEntity2);
			userRepository.save(userEntity);
			userRepository.save(userEntity3);
		};
	}

}
