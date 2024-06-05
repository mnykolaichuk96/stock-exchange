package pl.mnykolaichuk.gatewayserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Konwerter JWT na kolekcję przyznanych uprawnień (GrantedAuthority).
 * Konwertuje role z claima(klucza) "realm_access" w tokenie JWT na obiekty SimpleGrantedAuthority.
 */
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * Metoda konwertująca token JWT na kolekcję przyznanych uprawnień.
     *
     * @param source Token JWT do konwersji.
     * @return Kolekcja przyznanych uprawnień.
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {

        // Pobranie mapy "realm_access" z claimów tokena JWT
        Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");

        // Jeśli nie ma żadnych przyznanych uprawnień, zwróć pustą kolekcję
        if (realmAccess == null || realmAccess.isEmpty())
            return new ArrayList<>();

        // Konwersja listy nazw ról na obiekty SimpleGrantedAuthority
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
                .stream().map(roleName -> "ROLE_" + roleName) // Dodanie prefixu "ROLE_" do nazwy roli
                .map(SimpleGrantedAuthority::new) // Konwersja nazwy roli na obiekt SimpleGrantedAuthority (new SimpleGrantedAuthority("ROLE_"+roleName))
                .collect(Collectors.toList()); // Zebranie przekonwertowanych obiektów do kolekcji

        return returnValue; // Zwrócenie kolekcji przyznanych uprawnień
    }
}

