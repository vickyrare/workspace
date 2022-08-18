package io.codecrafts.stopstop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerDeck {
    ArrayList<Card> cardsInHand = new ArrayList<>();

    public PlayerDeck(List<Card> cardsSet) {
        this.cardsInHand.addAll(cardsSet);
    }

    public Card throwCard(int index) {
        if (!hasMoreCards()) {
            drawCardFromUserHand(index);
        }
        return cardsInHand.remove(index);
    }

    public void drawCardFromDeck(Deck deck) {
        cardsInHand.add(deck.drawCard());
    }

    public Card drawCardFromUserHand(int index) {
        if (index < cardsInHand.size()) {
            return cardsInHand.remove(index);
        }
        return null;
    }

    public Card getCard(int index) {
        if (cardsInHand.isEmpty()) {
            return null;
        }
        return cardsInHand.get(index);
    }

    public boolean hasMoreCards() {
        if (cardsInHand.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<Card> getAllCards() {
        return Collections.unmodifiableList(cardsInHand);
    }

    public int getNumberOfCards() {
        return cardsInHand.size();
    }

    public void showCardsInHand() {
        System.out.println(cardsInHand);
    }

    public boolean hasJack() {
        return cardsInHand.stream().filter(c -> c.rank.equals(Card.Rank.JACK)).count() > 0;
    }

    public boolean hasPenalty() {
        return cardsInHand.stream().filter(c -> c.rank.equals(Card.Rank.PENALTY)).count() > 0;
    }

    public boolean hasStop() {
        return cardsInHand.stream().filter(c -> c.rank.equals(Card.Rank.STOP)).count() > 0;
    }

    @Override
    public String toString() {
        return "PlayerDeck{" +
                "cards=" + cardsInHand +
                '}';
    }
}
