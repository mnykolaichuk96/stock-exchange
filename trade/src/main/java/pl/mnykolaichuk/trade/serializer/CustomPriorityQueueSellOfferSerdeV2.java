package pl.mnykolaichuk.trade.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import pl.mnykolaichuk.trade.dto.SellOfferDto;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

public class CustomPriorityQueueSellOfferSerdeV2 implements Serde<PriorityQueue<SellOfferDto>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Serializer<PriorityQueue<SellOfferDto>> serializer() {
        return new Serializer<>() {
            @Override
            public byte[] serialize(String topic, PriorityQueue<SellOfferDto> data) {
                try {
                    return objectMapper.writeValueAsBytes(data);
                } catch (Exception e) {
                    throw new RuntimeException("Error serializing PriorityQueue<SellOfferDto>", e);
                }
            }
        };
    }

    @Override
    public Deserializer<PriorityQueue<SellOfferDto>> deserializer() {
        return new Deserializer<>() {
            @Override
            public PriorityQueue<SellOfferDto> deserialize(String topic, byte[] data) {
                try {
                    SellOfferDto[] sellOffers = objectMapper.readValue(data, SellOfferDto[].class);
                    PriorityQueue<SellOfferDto> priorityQueue = new PriorityQueue<>(Comparator.comparing(SellOfferDto::getMinPrice).thenComparing(SellOfferDto::getTimestamp));
                    for (SellOfferDto sellOffer : sellOffers) {
                        priorityQueue.add(sellOffer);
                    }
                    return priorityQueue;
                } catch (Exception e) {
                    throw new RuntimeException("Error deserializing PriorityQueue<SellOfferDto>", e);
                }
            }
        };
    }
}
