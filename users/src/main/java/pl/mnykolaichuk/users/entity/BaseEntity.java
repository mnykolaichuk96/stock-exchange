package pl.mnykolaichuk.users.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass           // Super class for all entities
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @ToString
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false)  // Po utworzeniu rekordu w bd to pole nie będzie ulegac zmianom
                                                    // Jeśli zmiana danych w rekordzie nie dotyczy tego pola
                                                    // ORM nie będzie aktualizował tej kolumny w db
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)    // Nie wstawiane pola, nie uwzgłędniane pod czas wstawania nowego rekordu
                                                        // Jest to przydatne gdy chcemy automatycznie generować zawartość tego pola
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", insertable = false)
    private String updatedBy;
}
