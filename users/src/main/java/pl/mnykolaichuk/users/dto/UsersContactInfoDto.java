package pl.mnykolaichuk.users.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "users")
@Getter @Setter
public class UsersContactInfoDto {

    private String message;
    private Map<String, String> contactDetails;
    private List<String> onCallSupport;
}
