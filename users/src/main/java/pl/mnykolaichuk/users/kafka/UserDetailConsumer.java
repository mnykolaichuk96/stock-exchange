package pl.mnykolaichuk.users.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.mnykolaichuk.users.dto.UserDetailDto;
import pl.mnykolaichuk.users.service.IUserDetailService;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class UserDetailConsumer {

    private static final Logger log = LoggerFactory.getLogger(UserDetailConsumer.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final IUserDetailService iUserDetailService;

    public UserDetailConsumer(IUserDetailService iUserDetailService) { this.iUserDetailService = iUserDetailService; }


    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> record) {

        String jsonString = convertToJson(record.value());

        try {
            UserDetailDto userDetailDto = objectMapper.readValue(jsonString, UserDetailDto.class);
            if (userDetailDto.getBalance() == null) {
                userDetailDto.setBalance(BigDecimal.ZERO);
            }

            log.info("First Name: " + userDetailDto.getFirstName());
            log.info("Last Name: " + userDetailDto.getLastName());
            log.info("User ID: " + userDetailDto.getUserId());
            log.info("Email: " + userDetailDto.getEmail());
            log.info("Balance: " + userDetailDto.getBalance());

            iUserDetailService.saveUserDetail(userDetailDto);

        } catch (IOException e) {
            log.error("Failed to parse JSON", e);
        }
    }
    private String convertToJson(String value) {
        value = value.replace("=", "\":\"")
                .replace(", ", "\", \"")
                .replace("{", "{\"")
                .replace("}", "\"}");
        return value;
    }
}

