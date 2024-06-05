package pl.mnykolaichuk.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

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
                .route(route -> route
                        .path("/users/**")
                        .filters(path -> path.rewritePath("/users/(?<segment>.*)", "/${segment}"))
                        .uri("lb://USERS"))
                .build();
    }

}
