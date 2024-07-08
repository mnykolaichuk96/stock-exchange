package pl.mnykolaichuk.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Konfiguracja zabezpieczeń aplikacji Spring WebFlux.
 * Spring Cloud Gateway zbudowany na bazie Spring WebFlux (Reactive)
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    public static final String USERS = "USER"; // Stała przechowująca nazwę roli użytkowników.

    /**
     * Metoda konfigurująca łańcuch zabezpieczeń Spring Security dla aplikacji.
     *
     * @param serverHttpSecurity Obiekt ServerHttpSecurity do konfiguracji zabezpieczeń.
     * @return Konfiguracja łańcucha zabezpieczeń Spring Security.
     */
    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/users/**").hasRole(USERS)) // Ustawienie wymaganej roli dla określonej ścieżki.
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))); // Konfiguracja serwera zasobów OAuth 2.0 dla uwierzytelniania za pomocą JWT.
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable()); // Wyłączenie zabezpieczeń CSRF.

        return serverHttpSecurity.build(); // Zwrócenie skonfigurowanego łańcucha zabezpieczeń.
    }

    /**
     * Metoda tworząca konwerter JWT na obiekt Mono<AbstractAuthenticationToken> w celu ekstrakcji uprawnień.
     *
     * @return Konwerter JWT na obiekt Mono<AbstractAuthenticationToken>.
     */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter(); // Utworzenie konwertera autoryzacji JWT.
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter()); // Ustawienie konwertera ról JWT.

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter); // Zwrócenie adaptera konwertera JWT na obiekt Mono<AbstractAuthenticationToken>.
    }
}

