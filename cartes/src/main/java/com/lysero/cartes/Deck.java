package com.lysero.cartes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Deck {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID deckId;
    private UUID playerId;
    private String name;
    @ElementCollection
    @CollectionTable(name = "deck_card_ids", joinColumns = @JoinColumn(name = "deck_id"))
    @Column(name = "card_id")
    private List<UUID> cardIdList;
}
