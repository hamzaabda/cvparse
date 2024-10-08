package com.example.pfe;


import com.example.pfe.models.Role;
import com.example.pfe.repository.RoleRepository;
;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class PfeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PfeApplication.class, args);
	}
	@Bean
	public CommandLineRunner createRoles(RoleRepository roleRepository) {
		return args -> {
			if (!roleRepository.findByAuthority("ADMIN").isPresent()) {
				roleRepository.save(new Role("ADMIN"));
			}
			if (!roleRepository.findByAuthority("USER").isPresent()) {
				roleRepository.save(new Role("USER"));
			}
		};
	}


}
