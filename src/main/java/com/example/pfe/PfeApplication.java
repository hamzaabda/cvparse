package com.example.pfe;

import com.example.pfe.models.ApplicationUser;
import com.example.pfe.models.Role;
import com.example.pfe.repository.RoleRepository;
import com.example.pfe.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class PfeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PfeApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				if (roleRepository.findByAuthority("ADMIN").isPresent())
					return;
				Role adminRole = roleRepository.save(new Role("ADMIN"));
				roleRepository.save(new Role("USER"));

				Set<Role> roles = new HashSet<>();
				roles.add(adminRole);

				// Create a user with necessary parameters including email
				ApplicationUser admin = new ApplicationUser();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("password"));
				admin.setAuthorities(roles);
				admin.setEmail("admin@example.com");

				userRepository.save(admin);
			}
		};
	}
}
