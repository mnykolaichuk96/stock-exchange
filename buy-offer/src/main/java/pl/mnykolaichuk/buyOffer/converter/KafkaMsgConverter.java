package pl.mnykolaichuk.buyOffer.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mnykolaichuk.buyOffer.dto.BuyOfferDto;

import java.io.IOException;
import java.util.Map;

public class KafkaMsgConverter {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMsgConverter.class);
    public static BuyOfferDto convertToSellOfferDto(String kafkaMsg, BuyOfferDto buyOfferDto) {
        Map<String, Object> buyOfferMap = jsonToMap(kafkaMsg);

        buyOfferDto.setStockId(Long.parseLong(buyOfferMap.get("stockId").toString()));

        buyOfferDto.setUserId(Long.parseLong(buyOfferMap.get("userId").toString()));

        buyOfferDto.setStartAmount(Integer.parseInt(buyOfferMap.get("startAmount").toString()));

        buyOfferDto.setAmount(Integer.parseInt(buyOfferMap.get("amount").toString()));

        buyOfferDto.setMaxPrice(Double.parseDouble(buyOfferMap.get("maxPrice").toString()));

        return buyOfferDto;
    }

    private static Map<String, Object> jsonToMap(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;

        try {
            map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
