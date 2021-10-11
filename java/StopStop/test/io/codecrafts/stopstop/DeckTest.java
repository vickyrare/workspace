package io.codecrafts.stopstop;

import io.codecrafts.stopstop.model.Deck;
import org.junit.Test;

import static io.codecrafts.stopstop.model.Deck.TOTAL_CARDS_IN_A_DECK;

public class DeckTest {

    @Test
    public void testInit() {
        Deck deck = new Deck();
        assert(deck.getNumberOfCards() == TOTAL_CARDS_IN_A_DECK);
    }

    @Test
    public void testDrawCard() {
        Deck deck = new Deck();
        assert(deck.drawCard() != null);
    }

    @Test
    public void testHasMoreCards() {
        Deck deck = new Deck();
        int numberOfCards = deck.getNumberOfCards();
        for(int i = 0; i < numberOfCards; i++) {
            assert(deck.hasMoreCards());
            deck.drawCard();
        }
        assert(deck.hasMoreCards() == false);
    }

    @Test
    public void testDrawCardWhenAllCardsAreDrawnFromTheDeck() {
        Deck deck = new Deck();
        int numberOfCards = deck.getNumberOfCards();
        for(int i = 0; i < numberOfCards; i++) {
            deck.addExpiredCards(deck.drawCard());
        }
        assert(deck.drawCard() != null);
        assert(deck.hasMoreCards() == true);
        assert(deck.getNumberOfCards() == 51);
    }
}