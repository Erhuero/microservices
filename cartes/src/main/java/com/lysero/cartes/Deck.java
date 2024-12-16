package com.lysero.cartes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Deck {

    @Id
    @SequenceGenerator(
            name = "deck_id_sequence",
            sequenceName = "deck_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "deck_id_sequence"
    )
    private UUID deckId;
    private UUID playerId;
    private String name;
    @ElementCollection
    @CollectionTable(name = "deck_card_ids", joinColumns = @JoinColumn(name = "deck_id"))
    @Column(name = "card_id")
    private List<UUID> cardIdList;
}
