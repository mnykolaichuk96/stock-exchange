package pl.mnykolaichuk.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mnykolaichuk.users.dto.ErrorResponseDto;
import pl.mnykolaichuk.users.dto.UsersContactInfoDto;

@Tag(
        name = "REST APIs for users in Stock Exchange",
        description = "REST APIs for users in Stock Exchange implemented in Users MS"
)
@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    UsersContactInfoDto usersContactInfoDto;

    @Operation(
            summary = "Get Contact Info",
            description = "Contact info details that can be reached out in case of any issues"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/contact-info")
    public ResponseEntity<UsersContactInfoDto> getContactInfo() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usersContactInfoDto);
    }
}


