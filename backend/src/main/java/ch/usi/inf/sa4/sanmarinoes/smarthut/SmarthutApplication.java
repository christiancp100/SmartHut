package ch.usi.inf.sa4.sanmarinoes.smarthut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories("ch.usi.inf.sa4.sanmarinoes.smarthut.models")
public class SmarthutApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmarthutApplication.class, args);
    }
}
