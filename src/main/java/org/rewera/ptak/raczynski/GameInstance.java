package org.rewera.ptak.raczynski;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Reprezentuje instancję gry, zarządza graczami i stanem gry.
 */
public class GameInstance {

    /**
     * Unikalny identyfikator gry.
     */
    private int gameId;

    /**
     * Lista klientów (graczy) aktualnie połączonych z grą.
     */
    private List<ClientHandler> clients = new ArrayList<>();

    /**
     * Przechowuje aktualny stan gry.
     */
    private GameState gameState;

    /**
     * Określa, czy gracz 1 został przypisany.
     */
    private boolean isPlayer1Assigned = false;

    /**
     * Określa, czy gracz 2 został przypisany.
     */
    private boolean isPlayer2Assigned = false;

    /**
     * Tworzy nową instancję gry.
     *
     * @param gameId Unikalny identyfikator gry.
     */
    public GameInstance(int gameId) {
        this.gameId = gameId;
        List<Obstacle> obstacles = generateObstacles();
        gameState = new GameState(new Tank(100, 515, Color.BLUE), new Tank(600, 515, Color.RED), new ArrayList<>(), obstacles, true);
    }

    /**
     * Dodaje nowego klienta do gry.
     *
     * @param socket Gniazdo połączenia klienta.
     * @param out    Strumień wyjściowy.
     * @param in     Strumień wejściowy.
     */
    public synchronized void addClient(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        if (isFull()) {
            try {
                out.writeObject("Game is full. No more players can join.");
                out.flush();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        boolean isPlayer1 = !isPlayer1Assigned;
        if (isPlayer1) {
            isPlayer1Assigned = true;
        } else {
            isPlayer2Assigned = true;
        }

        ClientHandler clientHandler = new ClientHandler(socket, this, isPlayer1, out, in);
        clients.add(clientHandler);
        new Thread(clientHandler).start();

        try {
            out.writeBoolean(isPlayer1);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isFull()) {
            System.out.println("Game " + gameId + " is full. Starting the game...");
            broadcastGameState();
        }
    }

    /**
     * Sprawdza, czy gra jest pełna.
     *
     * @return true, jeśli gra ma dwóch graczy, false w przeciwnym razie.
     */
    public boolean isFull() {
        return isPlayer1Assigned && isPlayer2Assigned;
    }

    /**
     * Zwraca identyfikator gry.
     *
     * @return Identyfikator gry.
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Generuje losowe przeszkody dla gry.
     *
     * @return Lista przeszkód.
     */
    private List<Obstacle> generateObstacles() {
        Random random = new Random();
        int numObstacles = random.nextInt(4) + 3;
        List<Obstacle> obstacles = new ArrayList<>();

        for (int i = 0; i < numObstacles; i++) {
            int x;
            do {
                x = random.nextInt(750);
            } while (Math.abs(x - 100) < 50 || Math.abs(x - 600) < 50);

            int width = random.nextInt(30) + 20;
            obstacles.add(new Obstacle(x, 530, width, 20));
        }

        return obstacles;
    }

    /**
     * Aktualizuje stan gry i przesyła go do wszystkich klientów.
     *
     * @param newState Nowy stan gry.
     */
    public synchronized void updateGameState(GameState newState) {
        this.gameState = newState;
        broadcastGameState();
    }

    /**
     * Wysyła aktualny stan gry do wszystkich klientów.
     */
    private void broadcastGameState() {
        for (ClientHandler client : clients) {
            client.sendGameState(gameState);
        }
    }

    /**
     * Resetuje stan gry do wartości początkowych.
     */
    private void resetGameState() {
        // Resetowanie czołgów do początkowych pozycji i kolorów
        Tank player1Tank = new Tank(100, 515, Color.BLUE);
        Tank player2Tank = new Tank(600, 515, Color.RED);

        // Usunięcie pocisków
        List<Bullet> bullets = new ArrayList<>();

        // Wygenerowanie nowych przeszkód
        List<Obstacle> obstacles = generateObstacles();

        // Ustawienie nowego stanu gry
        gameState = new GameState(player1Tank, player2Tank, bullets, obstacles, true);
        System.out.println("Game state has been reset for game " + gameId);
    }

    /**
     * Usuwa klienta z gry i aktualizuje status graczy.
     *
     * @param client Klient do usunięcia.
     */
    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);

        if (client.isPlayer1()) {
            isPlayer1Assigned = false;
            System.out.println("Player 1 has disconnected from game " + gameId);
        } else {
            isPlayer2Assigned = false;
            System.out.println("Player 2 has disconnected from game " + gameId);
        }

        if (clients.isEmpty()) {
            System.out.println("Game " + gameId + " has no players. Removing game instance.");
            GameServer.getInstance().removeGameInstance(gameId);
            resetGameState();
            broadcastGameState();
        }
    }
}