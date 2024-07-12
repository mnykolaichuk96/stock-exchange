package pl.mnykolaichuk.users.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mnykolaichuk.users.dto.DecrementStockAmountDto;
import pl.mnykolaichuk.users.dto.ResponseDto;
import pl.mnykolaichuk.users.dto.StockDto;

@FeignClient(name = "stock", fallback = StockFallback.class)
public interface StockFeignClient {

    @GetMapping(value = "/api/stock/get-available-amount", consumes = "application/json")
    ResponseEntity<Integer> getStockAvailableAmount(@RequestParam Long stockId);

    @PutMapping("/api/stock/decrement-available-amount")
    public ResponseEntity<ResponseDto> decrementAvailableAmountStock(@RequestBody DecrementStockAmountDto decrementStockAmountDto);
}
