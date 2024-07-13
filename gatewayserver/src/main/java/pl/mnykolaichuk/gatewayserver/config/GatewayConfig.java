package pl.mnykolaichuk.gatewayserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.mnykolaichuk.gatewayserver.kafka.KafkaPublishGatewayFilterFactory;

@Configuration
public class GatewayConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Bean konfigurujący trasę dla bramy API. Ta metoda definiuje trasę, która przekierowuje wszystkie żądania,
     * które zaczynają się od '/users/', do usługi mikroserwisu o nazwie 'USERS'. Dodatkowo, używa filtru do
     * przeprowadzenia przepisania ścieżki, aby usunąć prefix '/users/' z końcowej ścieżki i zachować resztę
     * oryginalnej ścieżki w zmiennej 'segment'.
     *
     * @param routeLocatorBuilder Obiekt Builder do tworzenia definicji tras.
     * @return Obiekt RouteLocator reprezentujący zdefiniowaną trasę dla bramy API.
     */
    @Bean
    public RouteLocator stockExchangeRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("users_route",route -> route
                        .path("/users/**")
                        .filters(path -> path.rewritePath("/users/(?<segment>.*)", "/${segment}"))
                        .uri("lb://USERS"))
                .route("stock_route", route -> route
                        .path("/stock/**")
                        .filters(path -> path.rewritePath("/stock/(?<segment>.*)", "/${segment}"))
                        .uri("lb://STOCK"))
                .route("sell_offer_route", route -> route
                        .path("/sell-offer/api/sell-offer/create")
                        .filters(f -> f.filter(new KafkaPublishGatewayFilterFactory(bootstrapServers)
                                .apply(new KafkaPublishGatewayFilterFactory.Config("sell-offer-to-sell-offer-ms"))))
                        .uri("no://op"))  // Placeholder URI as the request is not routed to a backend service
                .build();
    }

}
