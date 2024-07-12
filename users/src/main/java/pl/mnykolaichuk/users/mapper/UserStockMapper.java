package pl.mnykolaichuk.users.mapper;

import pl.mnykolaichuk.users.dto.UserStockDto;
import pl.mnykolaichuk.users.entity.UserStock;

public class UserStockMapper {

    /**
     *
     * @param userStockDto from UserStockDto object
     * @param userStock to UserStock entity object
     * @return UserStock entity object
     */
    public static UserStock mapToUserStock(UserStockDto userStockDto, UserStock userStock){
        userStock.setStockId(userStockDto.getStockId());
        userStock.setStockAmount(userStockDto.getStockAmount());

        return userStock;
    }

    /**
     *
     * @param userStock from UserStock object
     * @param userStockDto to UserStockDto entity object
     * @return UserStockDto entity object
     */
    public static UserStockDto mapToUserStockDto(UserStock userStock, UserStockDto userStockDto){
        userStockDto.setStockId(userStock.getStockId());
        userStockDto.setStockAmount(userStock.getStockAmount());

        return userStockDto;
    }
}
