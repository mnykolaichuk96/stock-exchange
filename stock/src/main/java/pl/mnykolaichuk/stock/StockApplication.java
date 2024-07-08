package pl.mnykolaichuk.stock;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")

@OpenAPIDefinition(
		info = @Info(
				title = "Stock microservice REST API Documentation",
				description = "Stock Exchange Stock microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Mykola Nykolaichuk",
						email = "150174@stud.prz.edu.pl"
				)
		)
)
public class StockApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockApplication.class, args);
	}

}
