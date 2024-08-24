package pl.mnykolaichuk.trade.serializer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.mnykolaichuk.trade.dto.BuyOfferDto;

import java.util.PriorityQueue;

public class CustomPriorityQueueBuyOfferSerde extends Serdes.WrapperSerde<PriorityQueue<BuyOfferDto>> {
    public CustomPriorityQueueBuyOfferSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(constructType()));
    }

    private static JavaType constructType() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.getTypeFactory().constructCollectionType(PriorityQueue.class, BuyOfferDto.class);
    }
}
