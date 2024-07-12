package pl.mnykolaichuk.users.service;

import pl.mnykolaichuk.users.dto.UserStockDto;

public interface IUserStockService {

    /**
     * Method will check if its enough stock available in company and buy this stock.
     * Buy mean relation in UserStock table and decrement avoid amount of stocks in Stock-MS
     * Create ManyToMany relation between User-MS (user_detail table) and Stock-MS (stock table)
     *
     * @param userStockDto - Data Transfer Object contain userDetailId, stockId and stockAmount
     */
    void buyStock(UserStockDto userStockDto);

    /**
     * Method will check if its enough stock available in user and sell this stock.
     * Buy mean relation in UserStock table and decrement avoid amount of stocks in Stock-MS
     * Create ManyToMany relation between User-MS (user_detail table) and Stock-MS (stock table)
     *
     * @param userStockDto - Data Transfer Object contain userDetailId, stockId and stockAmount
     */
    void sellStock(UserStockDto userStockDto);
}
