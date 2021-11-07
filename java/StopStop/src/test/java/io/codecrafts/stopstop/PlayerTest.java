package io.codecrafts.stopstop;

import io.codecrafts.stopstop.model.Card;
import io.codecrafts.stopstop.model.Deck;
import io.codecrafts.stopstop.model.Player;
import io.codecrafts.stopstop.model.PlayerDeck;
import org.junit.Test;

import java.util.*;

public class PlayerTest {

    @Test
    public void testThrowNormalCard() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.NINE), new Card(Card.Suit.SPADE, Card.Rank.FOUR),
                                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        Card previousCard = new Card(Card.Suit.CLUB, Card.Rank.THREE);
        assert(player.canThrowAnyCard(previousCard));
        assert(player.canThrowCard(previousCard, 0));
    }

    @Test
    public void testCannotThrowNormalCardSuitMismatch() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.SPADE, Card.Rank.NINE), new Card(Card.Suit.SPADE, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        Card previousCard = new Card(Card.Suit.CLUB, Card.Rank.THREE);
        assert(player.canThrowAnyCard(previousCard));
        assert(player.canThrowCard(previousCard, 0) == false);
    }

    @Test
    public void testThrowJackCard() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.JACK), new Card(Card.Suit.SPADE, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        Card previousCard = new Card(Card.Suit.SPADE, Card.Rank.THREE);
        assert(player.canThrowAnyCard(previousCard));
        assert(player.canThrowCard(previousCard, 0));
    }

    @Test
    public void testThrowStopCardSuitMatch() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.STOP), new Card(Card.Suit.SPADE, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        Card previousCard = new Card(Card.Suit.CLUB, Card.Rank.THREE);
        assert(player.canThrowAnyCard(previousCard));
        assert(player.canThrowCard(previousCard, 0));
    }

    @Test
    public void testThrowStopCardRankMatch() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.STOP), new Card(Card.Suit.SPADE, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        Card previousCard = new Card(Card.Suit.SPADE, Card.Rank.STOP);
        assert(player.canThrowAnyCard(previousCard));
        assert(player.canThrowCard(previousCard, 0));
    }

    @Test
    public void testCannotThrowStopCardSuitMismatch() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.STOP), new Card(Card.Suit.SPADE, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        Card previousCard = new Card(Card.Suit.SPADE, Card.Rank.THREE);
        assert(player.canThrowAnyCard(previousCard));
        assert(player.canThrowCard(previousCard, 0) == false);
    }

    @Test
    public void testThrowPenaltyCardSuitMatch() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.PENALTY), new Card(Card.Suit.SPADE, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        Card previousCard = new Card(Card.Suit.CLUB, Card.Rank.THREE);
        assert(player.canThrowAnyCard(previousCard));
        assert(player.canThrowCard(previousCard, 0));
    }

    @Test
    public void testThrowPenaltyCardRankMatch() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.PENALTY), new Card(Card.Suit.SPADE, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        Card previousCard = new Card(Card.Suit.SPADE, Card.Rank.PENALTY);
        assert(player.canThrowAnyCard(previousCard));
        assert(player.canThrowCard(previousCard, 0));
    }

    @Test
    public void testCannotThrowPenaltyCardSuitMatch() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.PENALTY), new Card(Card.Suit.SPADE, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        Card previousCard = new Card(Card.Suit.SPADE, Card.Rank.THREE);
        assert(player.canThrowAnyCard(previousCard));
        assert(player.canThrowCard(previousCard, 0) == false);
    }

    @Test
    public void testCannotThrowAnyCardWhenLastCardIsANormalCard() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.TWO), new Card(Card.Suit.HEART, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        assert(player.canThrowAnyCard(new Card(Card.Suit.SPADE, Card.Rank.THREE)) == false);
    }

    @Test
    public void testCannotThrowAnyCardWhenLastCardIsPenalty() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.TWO), new Card(Card.Suit.HEART, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        assert(player.canThrowAnyCard(new Card(Card.Suit.SPADE, Card.Rank.THREE)) == false);
    }

    @Test
    public void testCannotThrowAnyCardWhenLastCardIsStop() {
        Deck deck = new Deck();
        deck.init(Arrays.asList(new Card(Card.Suit.CLUB, Card.Rank.TWO), new Card(Card.Suit.HEART, Card.Rank.FOUR),
                new Card(Card.Suit.HEART, Card.Rank.KING), new Card(Card.Suit.CLUB, Card.Rank.TEN),
                new Card(Card.Suit.DIAMOND, Card.Rank.TWO)));
        List<Card> initialCards = getInitialCards(deck, 5);
        Player player = new Player("Player 1", initialCards);
        assert(player.canThrowAnyCard(new Card(Card.Suit.SPADE, Card.Rank.STOP)) == false);
    }

    private List<Card> getInitialCards(Deck deck, int numberOfCards) {
        List<Card> initialCards = new ArrayList<>();

        for (int i = 0; i < numberOfCards; i++) {
            initialCards.add(deck.drawCard());
        }
        return initialCards;
    }
}