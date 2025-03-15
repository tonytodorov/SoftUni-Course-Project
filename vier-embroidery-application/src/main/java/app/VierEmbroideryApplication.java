package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VierEmbroideryApplication {

	public static void main(String[] args) {
		SpringApplication.run(VierEmbroideryApplication.class, args);
	}

}
