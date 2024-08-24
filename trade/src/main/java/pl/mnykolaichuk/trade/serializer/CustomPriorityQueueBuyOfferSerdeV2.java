package pl.mnykolaichuk.trade.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import pl.mnykolaichuk.trade.dto.BuyOfferDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

public class CustomPriorityQueueBuyOfferSerdeV2 implements Serde<PriorityQueue<BuyOfferDto>> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(CustomPriorityQueueBuyOfferSerde.class);

    @Override
    public Serializer<PriorityQueue<BuyOfferDto>> serializer() {
        return new Serializer<>() {
            @Override
            public byte[] serialize(String topic, PriorityQueue<BuyOfferDto> data) {
                try {
                    logger.info("Data before serialization: {}", data);
                    return objectMapper.writeValueAsBytes(data);
                } catch (Exception e) {
                    logger.error("Error serializing PriorityQueue<BuyOfferDto>: {}", e.getMessage(), e);
                    throw new RuntimeException("Error serializing PriorityQueue<BuyOfferDto>", e);
                }
            }
        };
    }

    @Override
    public Deserializer<PriorityQueue<BuyOfferDto>> deserializer() {
        return new Deserializer<>() {
            @Override
            public PriorityQueue<BuyOfferDto> deserialize(String topic, byte[] data) {
                try {
                    logger.info("Deserializing data from topic: {}", topic);
                    BuyOfferDto[] buyOfferDtoArray = objectMapper.readValue(data, BuyOfferDto[].class);
                    logger.info("Buy offers after readValue from bytes[]: {}", Arrays.toString(buyOfferDtoArray));
                    PriorityQueue<BuyOfferDto> priorityQueue = new PriorityQueue<>(Comparator.comparing(BuyOfferDto::getMaxPrice).reversed().thenComparing(BuyOfferDto::getTimestamp));
                    for (BuyOfferDto buyOfferDto : buyOfferDtoArray) {
                        priorityQueue.add(buyOfferDto);
                    }
                    logger.info("Deserialized PriorityQueue<BuyOfferDto>: {}", priorityQueue);
                    return priorityQueue;
                } catch (Exception e) {
                    logger.error("Error deserializing PriorityQueue<BuyOfferDto>: {}", e.getMessage(), e);
                    throw new RuntimeException("Error deserializing PriorityQueue<BuyOfferDto>", e);
                }
            }
        };
    }
}
