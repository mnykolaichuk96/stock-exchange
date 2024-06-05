package pl.mnykolaichuk.keycloak;

import jakarta.jms.MapMessage;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;


public class CustomEventListenerProviderFactory implements EventListenerProviderFactory {
    private static final Logger log = Logger.getLogger(CustomEventListenerProvider.class);


    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new CustomEventListenerProvider(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {

        log.warn("provider used init method in factory");
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        log.warn("provider used postInit method in factory");
    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "custom-event-listener";
    }
}
