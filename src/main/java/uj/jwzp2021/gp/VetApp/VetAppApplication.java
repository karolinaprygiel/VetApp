package uj.jwzp2021.gp.VetApp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class VetAppApplication {

  public static void main(String[] args) {

    SpringApplication.run(VetAppApplication.class, args);
    log.info("VetApp is running");
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
