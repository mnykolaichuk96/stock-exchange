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
import org.springframework.web.bind.annotation.*;
import pl.mnykolaichuk.users.constants.UserConstants;
import pl.mnykolaichuk.users.dto.ErrorResponseDto;
import pl.mnykolaichuk.users.dto.ResponseDto;
import pl.mnykolaichuk.users.dto.UserStockDto;
import pl.mnykolaichuk.users.dto.UsersContactInfoDto;
import pl.mnykolaichuk.users.service.IUserStockService;

@Tag(
        name = "REST APIs for users in Stock Exchange",
        description = "REST APIs for users in Stock Exchange implemented in Users MS"
)
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UsersContactInfoDto usersContactInfoDto;

    private final IUserStockService iUserStockService;

    public UserController(IUserStockService iUserStockService) { this.iUserStockService = iUserStockService; }

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

    @Operation(
            summary = "Buy stocks for user REST API",
            description = "REST API to buying stocks for user in Stock Exchange. Buying operation only available if its" +
                    "enough stocks available in company. Buying decreases available amount of stocks in company" +
                    "and increases its in user. "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    // odpowiedź HTTP będzie zawierała informacje o błędzie w formacie zdefiniowanym przez klasę ErrorResponseDto
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("/stock/buy")
    public ResponseEntity<ResponseDto> buyStock(@RequestBody
                                                UserStockDto userStockDto) {

        iUserStockService.buyStock(userStockDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseDto.builder()
                        .statusCode(UserConstants.STATUS_201)
                        .statusMsg(UserConstants.MESSAGE_201)
                        .build()
        );
    }

        @Operation(
                summary = "Sell stocks from user REST API",
                description = "REST API to selling stocks from user back to company in Stock Exchange. " +
                        "Selling operation only available if its enough stocks available in user. " +
                        "Buying decreases available amount of stocks in user and increases its in company. "
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "201",
                        description = "HTTP Status CREATED"
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "HTTP Status Internal Server Error",
                        // odpowiedź HTTP będzie zawierała informacje o błędzie w formacie zdefiniowanym przez klasę ErrorResponseDto
                        content = @Content(
                                schema = @Schema(implementation = ErrorResponseDto.class)
                        )
                )
        })
        @PostMapping("/stock/sell")
        public ResponseEntity<ResponseDto> sellStock(@RequestBody
                UserStockDto userStockDto) {

            iUserStockService.sellStock(userStockDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseDto.builder()
                            .statusCode(UserConstants.STATUS_201)
                            .statusMsg(UserConstants.MESSAGE_201)
                            .build()
            );
    }
}


