package pl.mnykolaichuk.users;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import pl.mnykolaichuk.users.dto.UsersContactInfoDto;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
// Informacja o klasach, które powinne byc powiązane z plikiem application.yml
@EnableConfigurationProperties(value = {UsersContactInfoDto.class})

@OpenAPIDefinition(
		info = @Info(
				title = "UserDetail microservice REST API Documentation",
				description = "Stock Exchange UserDetail microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Mykola Nykolaichuk",
						email = "150174@stud.prz.edu.pl"
				)
		)
)
public class UsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

}
