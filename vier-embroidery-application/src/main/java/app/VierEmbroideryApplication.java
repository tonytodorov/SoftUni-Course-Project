package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class VierEmbroideryApplication {

	public static void main(String[] args) {
		SpringApplication.run(VierEmbroideryApplication.class, args);
	}

}
