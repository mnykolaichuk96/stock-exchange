package pl.mnykolaichuk.sellOffer.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mnykolaichuk.sellOffer.dto.SellOfferDto;

import java.io.IOException;
import java.util.Map;

public class KafkaMsgConverter {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMsgConverter.class);
    public static SellOfferDto convertToSellOfferDto(String kafkaMsg, SellOfferDto sellOfferDto) {
        logger.warn("kafkaMsg: \n" + kafkaMsg);

        Map<String, Object> sellOfferMap = jsonToMap(kafkaMsg);

        sellOfferDto.setStockId(Long.parseLong(sellOfferMap.get("stockId").toString()));

        sellOfferDto.setUserId(Long.parseLong(sellOfferMap.get("userId").toString()));

        sellOfferDto.setStartAmount(Integer.parseInt(sellOfferMap.get("startAmount").toString()));

        sellOfferDto.setAmount(Integer.parseInt(sellOfferMap.get("amount").toString()));

        sellOfferDto.setMinPrice(Double.parseDouble(sellOfferMap.get("minPrice").toString()));

        return sellOfferDto;
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
