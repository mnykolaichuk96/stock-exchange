package pl.mnykolaichuk.trade.config;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import pl.mnykolaichuk.trade.dto.BuyOfferDto;
import pl.mnykolaichuk.trade.dto.SellOfferDto;
import pl.mnykolaichuk.trade.dto.TransactionDto;
import pl.mnykolaichuk.trade.serializer.CustomPriorityQueueBuyOfferSerde;
import pl.mnykolaichuk.trade.serializer.CustomPriorityQueueBuyOfferSerdeV2;
import pl.mnykolaichuk.trade.serializer.CustomPriorityQueueSellOfferSerde;
import pl.mnykolaichuk.trade.serializer.CustomPriorityQueueSellOfferSerdeV2;
import pl.mnykolaichuk.trade.service.impl.TradeService;

import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@EnableKafkaStreams
public class TradeProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TradeProcessor.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private TradeService tradeService;

//    @Bean
//    public StreamsBuilderFactoryBeanConfigurer streamsBuilderFactoryBeanConfigurer() {
//        return factoryBean -> {
//            Properties props = factoryBean.getStreamsConfiguration();
//            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
////            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
//        };
//    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "trade-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "pl.mnykolaichuk.trade.dto.SellOfferDto");

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public JsonSerde<SellOfferDto> sellOfferSerde() {
        JsonSerde<SellOfferDto> serde = new JsonSerde<>(SellOfferDto.class);
        Map<String, Object> config = new HashMap<>();
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);  // Disable type headers
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");  // Trust all packages (or specify your package)
        serde.configure(config, false);  // false indicates that this configuration is for values

        return serde;
    }

    @Bean
    public KStream<Long, SellOfferDto> kStream(StreamsBuilder builder, JsonSerde<SellOfferDto> sellOfferSerde) {
        KStream<Long, SellOfferDto> stream = builder.stream(
                "sell-offers",
                Consumed.with(Serdes.Long(), sellOfferSerde)
        );

        stream.foreach((key, value) -> {
            // Processing logic here
           logger.info("INSIDE STREAM Received: " + key + " -> " + value);
        });

        // Optionally, write the stream to another topic
        stream.to("output-topic", Produced.with(Serdes.Long(), new JsonSerde<>(SellOfferDto.class)));

        return stream;
    }

    @Bean
    public Function<KStream<Long, BuyOfferDto>, KStream<Long, PriorityQueue<BuyOfferDto>>> processBuyOffers() {
        logger.info("CALL aggregation for buy-offers");

        return buyOffersStream -> buyOffersStream
                .groupByKey()  // Групуємо події за початковим ключем
                .aggregate(
                // перший аргумент є ф-цією ініціалізатором
                        PriorityQueue::new,  // Створення нової черги лише для нового ключа
                        (key, value, aggregate) -> {
                            logger.info("Aggregating Buy Offer: Key={}, Value={}", key, value);
                            aggregate.add(value);  // Додаємо нові події до вже існуючої черги
                            return aggregate;
                        },
                        Materialized.<Long, PriorityQueue<BuyOfferDto>>as(Stores.persistentKeyValueStore("buy-offers-store"))
                                .withKeySerde(Serdes.Long())
                                .withValueSerde(new CustomPriorityQueueBuyOfferSerdeV2())
                ).toStream();
    }

//    @Bean
//    public Function<KStream<Long, BuyOfferDto>, KStream<Long, PriorityQueue<BuyOfferDto>>> processBuyOffers() {
//        logger.info("CALL aggregation for buy-offers");
//        return buyOffersStream -> buyOffersStream
//                .selectKey((key, value) -> value.getStockId())
//                .groupByKey()
//                .aggregate(
//                        () -> new PriorityQueue<>(Comparator.comparing(BuyOfferDto::getMaxPrice).reversed().thenComparing(BuyOfferDto::getTimestamp)),
//                        (key, value, aggregate) -> {
//                            logger.info("Aggregating Buy Offer: Key={}, Value={}", key, value);
//                            aggregate.add(value);
//                            return aggregate;
//                        },
//                        Materialized.<Long, PriorityQueue<BuyOfferDto>>as(Stores.persistentKeyValueStore("buy-offers-store"))
//                                .withKeySerde(Serdes.Long())
//                                .withValueSerde(new CustomPriorityQueueBuyOfferSerdeV2())
//                ).toStream();
//    }

    @Bean
    public Function<KStream<Long, SellOfferDto>, KStream<Long, PriorityQueue<SellOfferDto>>> processSellOffers() {
        logger.info("CALL aggregation for sell-offers");

        return sellOffersStream -> sellOffersStream
                .selectKey((key, value) -> key)
                .groupByKey()
                .aggregate(
                        () -> new PriorityQueue<>(Comparator.comparing(SellOfferDto::getMinPrice).thenComparing(SellOfferDto::getTimestamp)),
                        (key, value, aggregate) -> {
                            logger.info("Aggregating Sell Offer: Key={}, Value={}", key, value);
                            aggregate.add(value);
                            return aggregate;
                        },
                        Materialized.<Long, PriorityQueue<SellOfferDto>>as(Stores.persistentKeyValueStore("sell-offers-store"))
                                .withKeySerde(Serdes.Long())
                                .withValueSerde(new CustomPriorityQueueSellOfferSerdeV2())
                ).toStream();
    }

    /**
     * Bean definiujący funkcję przetwarzania transakcji.
     * Łączy strumienie ofert kupna i sprzedaży, a następnie generuje transakcje na podstawie dopasowanych ofert.
     * Dzięki użyciu KTable, funkcja join zawsze otrzymuje najbardziej aktualne dane, które obejmują nowe wpisy
     * oraz poprzednie oferty, które nie zostały połączone.
     *
     * @return BiFunction, która przyjmuje dwie tabele KTable (z kolejkami priorytetowymi ofert kupna i sprzedaży)
     *         i zwraca strumień KStream z transakcjami.
     */

    @Bean
    public BiFunction<KStream<Long, PriorityQueue<BuyOfferDto>>, KStream<Long, PriorityQueue<SellOfferDto>>, KStream<Long, TransactionDto>> processTransactions() {
        return (buyOffersTable, sellOffersTable) -> buyOffersTable.join(
                sellOffersTable,
                this::matchAndCombineOffers,
                JoinWindows.of(Duration.ofSeconds(30)),
//                StreamJoined.with(Serdes.Long(), new JsonSerde<>(new TypeReference<List<BuyOfferDto>>() {}), new JsonSerde<>(new TypeReference<List<SellOfferDto>>() {}))
                StreamJoined.with(Serdes.Long(), new CustomPriorityQueueBuyOfferSerdeV2(), new CustomPriorityQueueSellOfferSerdeV2())
//                StreamJoined.with(Serdes.Long(), new JsonSerde<>(PriorityQueue.class), new JsonSerde<>(PriorityQueue.class))
        ).flatMap((key, value) -> {
            value.forEach(tradeService::saveTransactionToDBAsync);
            return value.stream().map(tx -> new KeyValue<>(key, tx)).collect(Collectors.toList());
        });
    }


    /**
     * Dopasowuje i łączy oferty kupna i sprzedaży, generując listę transakcji.
     * Oferty kupna są sortowane według najwyższej ceny i następnie według starszych ofert.
     * Oferty sprzedaży są sortowane według najniższej ceny i następnie według starszych ofert.
     *
     * @param buyOffers kolejka priorytetowa ofert kupna.
     * @param sellOffers kolejka priorytetowa ofert sprzedaży.
     * @return lista transakcji wygenerowanych na podstawie dopasowanych ofert kupna i sprzedaży.
     */
    private List<TransactionDto> matchAndCombineOffers(PriorityQueue<BuyOfferDto> buyOffers, PriorityQueue<SellOfferDto> sellOffers) {
        List<TransactionDto> results = new ArrayList<>();

        while (!buyOffers.isEmpty() && !sellOffers.isEmpty()) {
            BuyOfferDto buyOffer = buyOffers.peek();
            SellOfferDto sellOffer = sellOffers.peek();

            if (buyOffer.getMaxPrice() >= sellOffer.getMinPrice()) {
                int tradeAmount = Math.min(buyOffer.getAmount(), sellOffer.getAmount());
                double tradePrice = (buyOffer.getMaxPrice() + sellOffer.getMinPrice()) / 2;
                Long tradeTimestamp = System.currentTimeMillis();

                results.add(TransactionDto.builder()
                                .buyOfferId(buyOffer.getBuyOfferId())
                                .sellOfferId(sellOffer.getSellOfferId())
                                .amount(tradeAmount)
                                .price(tradePrice)
                                .timestamp(tradeTimestamp)
                        .build());

                buyOffer.setAmount(buyOffer.getAmount() - tradeAmount);
                sellOffer.setAmount(sellOffer.getAmount() - tradeAmount);

                if (buyOffer.getAmount() == 0) {
                    buyOffers.poll(); // Usunięcie oferty kupna
                }

                if (sellOffer.getAmount() == 0) {
                    sellOffers.poll(); // Usunięcie oferty sprzedaży
                }
            } else {
                break;
            }
        }

        return results;
    }
}

