package pl.mnykolaichuk.stock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.mnykolaichuk.stock.constants.StockConstants;
import pl.mnykolaichuk.stock.dto.*;
import pl.mnykolaichuk.stock.entity.Company;
import pl.mnykolaichuk.stock.entity.Stock;
import pl.mnykolaichuk.stock.exception.CompanyNotFoundException;
import pl.mnykolaichuk.stock.exception.StockNotFoundException;
import pl.mnykolaichuk.stock.mapper.StockMapper;
import pl.mnykolaichuk.stock.service.ICompanyService;
import pl.mnykolaichuk.stock.service.IStockService;

import java.util.Optional;
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
    @PostMapping("/company/create")
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
    @PostMapping("/company/create-with-stocks")
    public ResponseEntity<ResponseDto> createCompanyWithStocks(@RequestBody
                                                               CompanyWithStocksDto companyWithStocksDto) {

        iCompanyService.createCompanyWithStocks(
                companyWithStocksDto.getCompanyName(), companyWithStocksDto.getStockDtos());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseDto.builder()
                        .statusCode(StockConstants.STATUS_201)
                        .statusMsg(StockConstants.MESSAGE_201)
                        .build()
        );
    }

    @Operation(
            summary = "Add stocks to existing company REST API",
            description = "REST API to assign stocks to existing company in Stock Exchange"
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
    @PostMapping("/company/add-stocks")
    public ResponseEntity<ResponseDto> addStocksToCompany(@RequestBody
                                                          CompanyWithStocksDto companyWithStocksDto) {
        Company company = iCompanyService.getCompanyByName(companyWithStocksDto.getCompanyName());

        iStockService.addStocks(company.getCompanyId(), companyWithStocksDto.getStockDtos());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseDto.builder()
                        .statusCode(StockConstants.STATUS_201)
                        .statusMsg(StockConstants.MESSAGE_201)
                        .build()
        );
    }

    @Operation(
            summary = "Get stock available amount by its name REST API",
            description = "REST API for getting available amount of stock by its name in Stock Exchange"
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
    @GetMapping("/stock/get-available-amount")
    public ResponseEntity<Integer> getStockAvailableAmount(@RequestParam Long stockId) {

        Integer availableAmount = iStockService.getStockAvailableAmount(stockId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                availableAmount
        );
    }

    @Operation(
            summary = "Update stock REST API",
            description = "REST API for updating stock in Stock Exchange"
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
    @PutMapping("/stock/update")
    public ResponseEntity<ResponseDto> updateStock(@RequestBody StockDto stockDto) {

        iStockService.updateStock(stockDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(StockConstants.STATUS_200, StockConstants.MESSAGE_200));
    }

    @Operation(
            summary = "Decrement available amount of stock REST API",
            description = "REST API for decrementation available amount of stock by stock ID and value to decrement." +
                    "Better be available only in Feign Client in Users-MS, could implement by using token"
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
    @PutMapping("/stock/decrement-available-amount")
    public ResponseEntity<ResponseDto> decrementAvailableAmountStock(@RequestBody DecrementStockAmountDto decrementStockAmountDto) {

        Optional<Stock> optionalStock =
                Optional.ofNullable(iStockService.getStockById(decrementStockAmountDto.getStockId()));

        Stock stock = optionalStock.orElseThrow(
                () -> new StockNotFoundException("Stock", "stockId", decrementStockAmountDto.getStockId().toString())
        );

        stock.setAvailableAmount(stock.getAvailableAmount() - decrementStockAmountDto.getStockAmountToDecrement());

        StockDto stockDto = StockMapper.mapToStockDto(stock, new StockDto());

        iStockService.updateStock(stockDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(StockConstants.STATUS_200, StockConstants.MESSAGE_200));
    }
}
