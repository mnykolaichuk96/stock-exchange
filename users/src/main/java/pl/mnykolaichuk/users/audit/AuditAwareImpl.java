package pl.mnykolaichuk.users.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * Returns the current auditor of the application. It's return information about current auditor of database,
     * user or service (service here)
     * Spring Data JPA will use this information to automatically fill fields 'CreatedBy' i 'LastModifiedBy' in db.
     *
     * @return the current auditor.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("USERS"); // "USERS" service name in eureka server (not necessary)
    }

}
