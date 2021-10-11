package io.codecrafts.stopstop.model;

import java.util.List;

public class Player {
    private String name;
    PlayerDeck playerDeck;
    Card currentCard;

    public Player(String name, List<Card> initialCards) {
        this.setName(name);
        this.playerDeck = new PlayerDeck(initialCards);
    }

    public Card throwCard(int index) {
        currentCard = playerDeck.throwCard(index);
        return currentCard;
    }

    public boolean canThrowCard(Card previousCard, int index) {
        if (index >= playerDeck.getNumberOfCards()) {
            return false;
        }
        Card playerCurrentlySelectedCard = playerDeck.getCard(index);
        if (playerCurrentlySelectedCard.getRank() == Card.Rank.JACK) {
            return true;
        }
        if (previousCard.getRank() == Card.Rank.PENALTY && playerCurrentlySelectedCard.getRank() == Card.Rank.PENALTY) {
            return true;
        }
        if (previousCard.getRank() == Card.Rank.STOP && playerCurrentlySelectedCard.getRank() == Card.Rank.STOP) {
            return true;
        }
        if (previousCard.getSuit() != playerCurrentlySelectedCard.getSuit()) {
            return false;
        }
        return true;
    }

    public void drawCardFromDeck(Deck deck) {
        playerDeck.drawCardFromDeck(deck);
    }

    public void showCardsInHand() {
        System.out.println(getName());
        playerDeck.showCardsInHand();
    }

    public boolean canThrowAnyCard(Card previousCard) {
        for(int i = 0; i < playerDeck.getNumberOfCards(); i++) {
            if (canThrowCard(previousCard, i)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerHandEmpty() {
        return !playerDeck.hasMoreCards();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
