package pl.mnykolaichuk.keycloak;

import jakarta.jms.MapMessage;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;
import org.springframework.jms.core.JmsTemplate;

import java.util.HashMap;
import java.util.Map;


public class CustomEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(CustomEventListenerProvider.class);

    private final KeycloakSession session;
    private final RealmProvider model;

    public CustomEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event) {

        if (EventType.REGISTER.equals(event.getType())) {
            log.infof("## NEW %s EVENT", event.getType());
            log.info("-----------------------------------------------------------");

            RealmModel realm = this.model.getRealm(event.getRealmId());
            UserModel newRegisteredUser = this.session.users().getUserById(realm, event.getUserId());

            log.info(newRegisteredUser.getEmail());
            log.info(newRegisteredUser.getFirstName());
            log.info(newRegisteredUser.getLastName());

            Map<String, String> clientmap = new HashMap<String, String>();
            clientmap.put("userId", newRegisteredUser.getId());
            clientmap.put("email", newRegisteredUser.getEmail());
            clientmap.put("firstName", newRegisteredUser.getFirstName());
            clientmap.put("lastName", newRegisteredUser.getLastName());

            Producer.publishEvent("CLIENT_REGISTER", clientmap.toString());

            log.info("-----------------------------------------------------------");
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        if (event.getOperationType() == OperationType.CREATE &&
                "USER".equals(event.getResourceType().name())) {
            log.info("Admin created user: " + event.getResourcePath());
            log.info("-----------------------------------------------------------");

            // Uzyskanie ID użytkownika z resourcePath
            String resourcePath = event.getResourcePath();
            String userId = resourcePath.substring(resourcePath.lastIndexOf('/') + 1);

            log.warn("ResourcePath: " + resourcePath);
            log.warn("UserId: " + userId);

            // Uzyskanie modelu realm
            RealmModel realm = this.session.realms().getRealm(event.getRealmId());

            // Uzyskanie modelu użytkownika za pomocą ID użytkownika
            UserModel newRegisteredUser = this.session.users().getUserById(realm, userId);

            if (newRegisteredUser != null) {
                log.info("New registered user: " + newRegisteredUser.getUsername());
                // Dodatkowa logika dla nowo zarejestrowanego użytkownika
            } else {
                log.warn("User not found for ID: " + userId);
            }

            log.info(newRegisteredUser.getEmail());
            log.info(newRegisteredUser.getFirstName());
            log.info(newRegisteredUser.getLastName());

            Map<String, String> clientmap = new HashMap<String, String>();
            clientmap.put("userId", newRegisteredUser.getId());
            clientmap.put("email", newRegisteredUser.getEmail());
            clientmap.put("firstName", newRegisteredUser.getFirstName());
            clientmap.put("lastName", newRegisteredUser.getLastName());

            Producer.publishEvent("CLIENT_REGISTER", clientmap.toString());

            log.info("-----------------------------------------------------------");

        }
    }

    @Override
    public void close() {

    }
}
