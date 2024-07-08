package pl.mnykolaichuk.stock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mnykolaichuk.stock.constants.StockConstants;
import pl.mnykolaichuk.stock.dto.ErrorResponseDto;
import pl.mnykolaichuk.stock.dto.ResponseDto;
import pl.mnykolaichuk.stock.dto.StockDto;
import pl.mnykolaichuk.stock.service.ICompanyService;
import pl.mnykolaichuk.stock.service.IStockService;

import java.util.Set;

@RestController
@RequestMapping(path = "/api")
@Validated
public class StockController {
    private final IStockService iStockService;
    private final ICompanyService iCompanyService;

    public StockController(IStockService iStockService, ICompanyService iCompanyService) {
        this.iStockService = iStockService;
        this.iCompanyService = iCompanyService;
    }

    @Operation(
            summary = "Create Company REST API",
            description = "REST API to create new Company without assigned stocks in Stock Exchange"
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
    @PostMapping("/create/company")
    public ResponseEntity<ResponseDto> createCompany(@RequestParam
            String companyName) {

        iCompanyService.createCompany(companyName);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseDto.builder()
                        .statusCode(StockConstants.STATUS_201)
                        .statusMsg(StockConstants.MESSAGE_201)
                        .build()
        );
    }

    @Operation(
            summary = "Create Company REST API",
            description = "REST API to create new Company with assigned stocks in Stock Exchange"
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
    @PostMapping("/create/company-with-stocks")
    public ResponseEntity<ResponseDto> createCompanyWithStocks(@RequestParam
                                        String companyName,
                                        Set<StockDto> stockDtos) {

        iCompanyService.createCompanyWithStocks(companyName, stockDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseDto.builder()
                        .statusCode(StockConstants.STATUS_201)
                        .statusMsg(StockConstants.MESSAGE_201)
                        .build()
        );
    }
}
