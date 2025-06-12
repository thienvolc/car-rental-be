package fun.dashspace.carrentalsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaAuditing
public class CarRentalSytemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentalSytemApplication.class, args);
	}

}
