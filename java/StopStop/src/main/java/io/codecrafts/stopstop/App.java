package io.codecrafts.stopstop;

import io.codecrafts.stopstop.service.GameService;

public class App {

    public static void main(String[] args) {
        GameService gameService = new GameService();
        gameService.start();
    }
}
