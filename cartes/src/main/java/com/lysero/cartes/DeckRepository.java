package com.lysero.cartes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeckRepository extends JpaRepository<Deck, UUID> {
}