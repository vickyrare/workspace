package io.codecrafts.stopstop.model;

import java.util.Objects;

public class Card {
    public enum Suit {
        CLUB,
        DIAMOND,
        HEART,
        SPADE
    }

    public enum Rank {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        PENALTY,
        STOP,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    Suit suit;
    Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return suit +"[" + rank +"]\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
}
