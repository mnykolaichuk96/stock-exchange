package pl.mnykolaichuk.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Schema(
        name = "UserDetail",
        description = "Schema (DTO) to hold and transfer User Detail data"
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDetailDto {

    @Schema(
            description = "User id in authentication server", example = "8dd3bd34-475f-4ce2-a1ec-f6682f7ff236"
    )
    @NotNull(message = "User must have id in authentication server")
    private String userId;

    @Schema(
            description = "User balance (cash)", example = "100.00"
    )
    @PositiveOrZero(message = "Total amount used should be equal or greater than zero")
    private BigDecimal balance;

    @Schema(
            description = "User first name", example = "John"
    )
    @NotEmpty(message = "First name can not be a null or empty")
    @Size(min = 2, max = 30, message = "The length of user first name should be between 2 and 30")
    private String firstName;

    @Schema(
            description = "User last name", example = "Smith"
    )
    @NotEmpty(message = "Last name can not be a null or empty")
    @Size(min = 2, max = 40, message = "The length of user first name should be between 2 and 40")
    private String lastName;

    @Schema(
            description = "User email address", example = "Smith@gmail.com"
    )
    @NotEmpty(message = "Email address can not be a null or empty")
    @Email
    private String email;

}
