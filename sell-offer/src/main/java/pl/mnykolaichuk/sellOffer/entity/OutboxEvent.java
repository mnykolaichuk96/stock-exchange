package pl.mnykolaichuk.sellOffer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "outbox_event")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payload")
    private String payload;

    @Column(name = "topic")
    private String topic;

}
