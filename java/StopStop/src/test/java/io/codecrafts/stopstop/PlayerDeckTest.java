package io.codecrafts.stopstop;

import io.codecrafts.stopstop.model.Card;
import io.codecrafts.stopstop.model.Deck;
import io.codecrafts.stopstop.model.PlayerDeck;
import org.junit.Test;

import java.util.*;

public class PlayerDeckTest {

    @Test
    public void testInit() {
        Deck deck = new Deck();
        List<Card> initialCards = getInitialCards(deck, 5);
        PlayerDeck playerDeck = new PlayerDeck(initialCards);
        assert(playerDeck.getNumberOfCards() == 5);
    }

    @Test
    public void testDrawCardFromUserHand() {
        Deck deck = new Deck();
        List<Card> initialCards = getInitialCards(deck, 5);
        PlayerDeck playerDeck = new PlayerDeck(initialCards);
        assert(deck.getNumberOfCards() == 47);
        assert(playerDeck.drawCardFromUserHand(0) != null);
    }

    @Test
    public void testDrawCardFromDeck() {
        Deck deck = new Deck();
        List<Card> initialCards = getInitialCards(deck, 5);
        PlayerDeck playerDeck = new PlayerDeck(initialCards);
        assert(deck.getNumberOfCards() == 47);
        assert(playerDeck.getNumberOfCards() == 5);
        playerDeck.drawCardFromDeck(deck);
        assert(deck.getNumberOfCards() == 46);
        assert(playerDeck.getNumberOfCards() == 6);
    }

    @Test
    public void testHasMoreCards() {
        Deck deck = new Deck();
        List<Card> initialCards = getInitialCards(deck, 5);
        PlayerDeck playerDeck = new PlayerDeck(initialCards);

        int numberOfCards = playerDeck.getNumberOfCards();
        for(int i = 0; i < numberOfCards; i++) {
            assert (playerDeck.hasMoreCards());
            playerDeck.drawCardFromUserHand(0);
        }

        assert(playerDeck.hasMoreCards() == false);
    }

    @Test
    public void testHasJack() {
        List<Card> initialCards = new ArrayList<>(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.JACK)));
        PlayerDeck playerDeck = new PlayerDeck(initialCards);
        assert(playerDeck.hasJack());
    }

    @Test
    public void testHasNoJack() {
        List<Card> initialCards = new ArrayList<>(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.ACE)));
        PlayerDeck playerDeck = new PlayerDeck(initialCards);
        assert(!playerDeck.hasJack());
    }

    @Test
    public void testHasPenalty() {
        List<Card> initialCards = new ArrayList<>(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.PENALTY)));
        PlayerDeck playerDeck = new PlayerDeck(initialCards);
        assert(playerDeck.hasPenalty());
    }

    @Test
    public void testHasNoPenalty() {
        List<Card> initialCards = new ArrayList<>(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.ACE)));
        PlayerDeck playerDeck = new PlayerDeck(initialCards);
        assert(!playerDeck.hasPenalty());
    }

    @Test
    public void testHasStop() {
        List<Card> initialCards = new ArrayList<>(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.STOP)));
        PlayerDeck playerDeck = new PlayerDeck(initialCards);
        assert(playerDeck.hasStop());
    }

    @Test
    public void testHasNoStop() {
        List<Card> initialCards = new ArrayList<>(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.ACE)));
        PlayerDeck playerDeck = new PlayerDeck(initialCards);
        assert(!playerDeck.hasStop());
    }

    private List<Card> getInitialCards(Deck deck, int numberOfCards) {
        List<Card> initialCards = new ArrayList<>();

        for (int i = 0; i < numberOfCards; i++) {
            initialCards.add(deck.drawCard());
        }
        return initialCards;
    }
}