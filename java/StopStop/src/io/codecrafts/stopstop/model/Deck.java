package io.codecrafts.stopstop.model;

import java.util.*;

public class Deck {

    public static int TOTAL_CARDS_IN_A_DECK = 52;
    ArrayList<Card> cardsInDeck;
    ArrayList<Card> cardsInExpiredDeck;

    public Deck() {
        init();
    }

    public void init() {
        Set<Card> cardsSet = new HashSet<>();
        for (Card.Suit suit: Card.Suit.values()) {
            for(Card.Rank rank: Card.Rank.values()) {
                cardsSet.add(new Card(suit, rank));
            }
        }
        cardsInDeck = new ArrayList<>(cardsSet);
        cardsInExpiredDeck = new ArrayList<>();
        Collections.shuffle(cardsInDeck);
    }

    // only for testing
    public void init(List<Card> cards) {
        cardsInDeck = new ArrayList<>(cards);
        cardsInExpiredDeck = new ArrayList<>();
    }

    public Card drawCard() {
        if (!hasMoreCards()) {
            cardsInDeck.clear();
            cardsInDeck.addAll(cardsInExpiredDeck);
            Collections.shuffle(cardsInDeck);
            cardsInExpiredDeck.clear();
        }
        Card card = cardsInDeck.remove(0);
        return card;
    }

    public boolean hasMoreCards() {
        if (cardsInDeck.isEmpty()) {
            return false;
        }
        return true;
    }

    public int getNumberOfCards() {
        return cardsInDeck.size();
    }

    public void addExpiredCards(Card expiredCard) {
        cardsInExpiredDeck.add(expiredCard);
    }

    @Override
    public String toString() {
        return "Deck{\n" +
                "cards=" + cardsInDeck +
                "}";
    }
}
