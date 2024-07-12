package pl.mnykolaichuk.users.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.mnykolaichuk.users.dto.DecrementStockAmountDto;
import pl.mnykolaichuk.users.dto.StockDto;
import pl.mnykolaichuk.users.dto.UserStockDto;
import pl.mnykolaichuk.users.entity.UserDetail;
import pl.mnykolaichuk.users.entity.UserStock;
import pl.mnykolaichuk.users.exception.NotEnoughStockAvailableException;
import pl.mnykolaichuk.users.exception.UserDetailNotFoundException;
import pl.mnykolaichuk.users.mapper.UserStockMapper;
import pl.mnykolaichuk.users.repository.UserDetailRepository;
import pl.mnykolaichuk.users.repository.UserStockRepository;
import pl.mnykolaichuk.users.service.IUserStockService;
import pl.mnykolaichuk.users.service.client.StockFeignClient;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserStockService implements IUserStockService {

    private static final Logger logger = LoggerFactory.getLogger(UserStockService.class);

    private StockFeignClient stockFeignClient;
    private UserDetailRepository userDetailRepository;
    private UserStockRepository userStockRepository;

    /**
     * Method will check if its enough stock available and buy this stock.
     * Buy mean relation in UserStock table and decrement avoid amount of stocks in Stock-MS
     * Create ManyToMany relation between User-MS (user_detail table) and Stock-MS (stock table)
     *
     * @param userStockDto - Data Transfer Object contain userDetailId, stockId and stockAmount
     */
    @Override
    public void buyStock(UserStockDto userStockDto) {

        Optional<Integer> optionalAvailableAmount =
                Optional.ofNullable(stockFeignClient.getStockAvailableAmount(userStockDto.getStockId()).getBody());

        Integer availableAmount = optionalAvailableAmount.orElse(0);

        if (availableAmount - userStockDto.getStockAmount() < 0) {
            throw new NotEnoughStockAvailableException(
                    "Available Amount of Stocks", "stockId", userStockDto.getStockId().toString());
        }

        UserStock userStock = UserStockMapper.mapToUserStock(userStockDto, new UserStock());
        Optional<UserDetail> optionalUserDetail =
                Optional.ofNullable(userDetailRepository.getUserDetailByUserDetailId(userStockDto.getUserDetailId()));

        userStock.setUserDetail(optionalUserDetail.orElseThrow(
                () -> new UserDetailNotFoundException("UserDetail", "userId", userStockDto.getUserDetailId().toString())
        ));

        userStockRepository.save(userStock);

        stockFeignClient.decrementAvailableAmountStock(new DecrementStockAmountDto(
                userStockDto.getStockId(), userStockDto.getStockAmount()));
    }

    /**
     * Method will check if its enough stock available in user and sell this stock.
     * Buy mean relation in UserStock table and decrement avoid amount of stocks in Stock-MS
     * Create ManyToMany relation between User-MS (user_detail table) and Stock-MS (stock table)
     *
     * @param userStockDto - Data Transfer Object contain userDetailId, stockId and stockAmount
     */
    @Override
    public void sellStock(UserStockDto userStockDto) {

    }
}
