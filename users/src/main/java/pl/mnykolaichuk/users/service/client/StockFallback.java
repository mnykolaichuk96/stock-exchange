package pl.mnykolaichuk.users.service.client;

import org.springframework.http.ResponseEntity;
import pl.mnykolaichuk.users.dto.DecrementStockAmountDto;
import pl.mnykolaichuk.users.dto.ResponseDto;
import pl.mnykolaichuk.users.dto.StockDto;

public class StockFallback implements StockFeignClient{

    @Override
    public ResponseEntity<Integer> getStockAvailableAmount(Long stockId) { return null; }

    @Override
    public ResponseEntity<ResponseDto> decrementAvailableAmountStock(DecrementStockAmountDto decrementStockAmountDto) { return null; }
}
