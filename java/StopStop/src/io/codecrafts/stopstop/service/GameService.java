package io.codecrafts.stopstop.service;

import io.codecrafts.stopstop.model.Card;
import io.codecrafts.stopstop.model.Deck;
import io.codecrafts.stopstop.model.Player;

import java.io.InputStream;
import java.util.*;

public class GameService {
    int numPlayers = 2;
    int numberOfCardsGivenAtTheStart = 2;
    int whichPlayerTurn = 1;
    Deck deck = new Deck();
    Card currentCardFromTheGame;
    boolean running = true;
    List<Player> players = new ArrayList<>();
    InputStream in = System.in;
    Scanner scanner = new Scanner(in);

    public GameService() {
        //System.out.println(deck);
    }

    private void initializePlayers() {
        for(int i = 0; i < numPlayers; i++) {
            Player player = new Player("Player " + (i + 1), getInitialCards(deck));
            players.add(player);
        }
    }

    private List<Card> getInitialCards(Deck deck) {
        List<Card> initialCards = new ArrayList<>();
        for (int i = 0; i < numberOfCardsGivenAtTheStart; i++) {
            initialCards.add(deck.drawCard());
        }
        return initialCards;
    }

    private Card drawFirstCard() {
        return deck.drawCard();
    }

    public void start() {
        initializePlayers();
        currentCardFromTheGame = drawFirstCard();
        if (currentCardFromTheGame.getRank() == Card.Rank.STOP) {
            switchPlayer();
        }

        while (running) {
            System.out.println("Current Card: " + currentCardFromTheGame);
            Player player = players.get(whichPlayerTurn - 1);
            processPlayerTurn(player);
        }
    }

    private int getInput() {
        String line = scanner.nextLine();
        if (line != null) {
            try {
                int index = Integer.parseInt(line);
                return index - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid card selected. Please select a valid card.");
                return getInput() - 1;
            }
        } else {
            return getInput() - 1;
        }
    }

    private void processPlayerTurn(Player player) {
        int cardIndex;
        player.showCardsInHand();
        if (player.canThrowAnyCard(currentCardFromTheGame)) {
            cardIndex = getInput();
            if (player.canThrowCard(currentCardFromTheGame, cardIndex)) {
                currentCardFromTheGame = player.throwCard(cardIndex);
                deck.addExpiredCards(currentCardFromTheGame);
                if (player.isPlayerHandEmpty()) {
                    System.out.println(player.getName() + " won.");
                    running = false;
                }
                processCard();
            } else {
                System.out.println(player.getName());
                System.out.println("Invalid card selected. Please select a valid card.");
            }
        } else {
            System.out.println(player.getName());
            System.out.println("Don't have a card that can be thrown. Drawing a card from the deck.");
            player.drawCardFromDeck(deck);
            player.showCardsInHand();
            if (player.canThrowAnyCard(currentCardFromTheGame)) {
                cardIndex = getInput();
                if (player.canThrowCard(currentCardFromTheGame, cardIndex)) {
                    currentCardFromTheGame = player.throwCard(cardIndex);
                    deck.addExpiredCards(currentCardFromTheGame);
                    if (player.isPlayerHandEmpty()) {
                        System.out.println(player.getName() + " won.");
                    }
                    processCard();
                } else {
                    System.out.println(player.getName());
                    System.out.println("Invalid card selected. Please select a valid card.");
                }
            } else {
                System.out.println(player.getName());
                System.out.println("Don't have a card that can be thrown. Switching turn to the next player.");
                switchPlayer();
            }
        }
    }

    private void switchPlayer() {
        whichPlayerTurn++;
        if (whichPlayerTurn > numPlayers) {
            whichPlayerTurn = 1;
        }
    }

    private void processCard() {
        if (currentCardFromTheGame.getRank() == Card.Rank.JACK) {
            letPlayerDecideTheSuit();
        }
        if (currentCardFromTheGame.getRank() != Card.Rank.STOP) {
            switchPlayer();
        }
    }

    private void letPlayerDecideTheSuit() {
        System.out.println("Press 1 for Club.\nPress 2 for Diamond.\nPress 3 for Heart.\nPress 4 for Spade.\n");

        int index = getInput();

        switch (index) {
            case 0:
                currentCardFromTheGame = new Card(Card.Suit.CLUB, Card.Rank.ACE);
                break;
            case 1:
                currentCardFromTheGame = new Card(Card.Suit.DIAMOND, Card.Rank.ACE);
                break;
            case 2:
                currentCardFromTheGame = new Card(Card.Suit.HEART, Card.Rank.ACE);
                break;
            case 3:
                currentCardFromTheGame = new Card(Card.Suit.SPADE, Card.Rank.ACE);
                break;
            default:
                letPlayerDecideTheSuit();
                break;
        }
    }
}
