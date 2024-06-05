package pl.mnykolaichuk.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import pl.mnykolaichuk.users.dto.UsersContactInfoDto;


@SpringBootApplication
@EnableDiscoveryClient
// Informacja o klasach, które powinne byc powiązane z plikiem application.yml
@EnableConfigurationProperties(value = {UsersContactInfoDto.class})
public class UsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

}
