package uj.jwzp2021.gp.VetApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.modelmapper.ModelMapper;

@SpringBootApplication
@EnableScheduling
public class VetAppApplication {

	public static void main(String[] args) {

		SpringApplication.run(VetAppApplication.class, args);

		System.out.println("I AM ALIVE!!!");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
