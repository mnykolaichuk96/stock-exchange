package pl.mnykolaichuk.stock.mapper;


import pl.mnykolaichuk.stock.dto.StockDto;
import pl.mnykolaichuk.stock.entity.Stock;

public class StockMapper {

    /**
     *
     * @param stockDto from StockDto object
     * @param stock to entity Stock object
     * @return Stock object
     */
    public static Stock mapToStock(StockDto stockDto, Stock stock){
        stock.setName(stockDto.getName());
        stock.setPriceId(stockDto.getPriceId());
        stock.setAvoidAmount(stockDto.getAvoidAmount());

        return stock;
    }

    /**
     *
     * @param stock from Stock object
     * @param stockDto to StockDto object
     * @return StockDto object
     */
    public static StockDto mapToStockDto(Stock stock, StockDto stockDto){
        stockDto.setName(stock.getName());
        stockDto.setPriceId(stock.getPriceId());
        stockDto.setAvoidAmount(stock.getAvoidAmount());

        return stockDto;
    }
}
