package org.rewera.ptak.raczynski;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa GameServer obsługująca serwer gry multiplayer.
 * Zarządza instancjami gier oraz przypisuje graczy do odpowiednich sesji.
 */
public class GameServer {

    /**
     * Port, na którym nasłuchuje serwer gry.
     */
    private static final int PORT = 12345;

    /**
     * Singleton serwera gry.
     */
    private static GameServer instance;

    /**
     * Mapa przechowująca wszystkie instancje gier.
     */
    private Map<Integer, GameInstance> gameInstances = new HashMap<>();

    /**
     * Identyfikator dla kolejnej gry, która zostanie stworzona.
     */
    private int nextGameId = 0;

    /**
     * Konstruktor uruchamia serwer gry, oczekując na połączenia klientów.
     */
    public GameServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                // Przypisz klienta do istniejącej gry lub utwórz nową
                GameInstance gameInstance = findOrCreateGameInstance();
                gameInstance.addClient(socket, out, in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wyszukuje istniejącą grę z wolnym miejscem lub tworzy nową instancję gry.
     *
     * @return Istniejąca lub nowa instancja gry.
     */
    private GameInstance findOrCreateGameInstance() {
        for (GameInstance instance : gameInstances.values()) {
            if (!instance.isFull()) {
                return instance;
            }
        }

        // Jeśli nie ma dostępnej gry, utwórz nową
        GameInstance newInstance = new GameInstance(nextGameId++);
        gameInstances.put(newInstance.getGameId(), newInstance);
        return newInstance;
    }

    /**
     * Pobiera instancję serwera gry, stosując wzorzec singleton.
     *
     * @return Unikalna instancja serwera gry.
     */
    public static synchronized GameServer getInstance() {
        if (instance == null) {
            instance = new GameServer();
        }
        return instance;
    }

    /**
     * Usuwa instancję gry z serwera.
     *
     * @param gameId Identyfikator gry do usunięcia.
     */
    public void removeGameInstance(int gameId) {
        gameInstances.remove(gameId);
    }

    /**
     * Metoda główna uruchamiająca serwer gry.
     *
     * @param args Argumenty wiersza poleceń.
     */
    public static void main(String[] args) {
        new GameServer();
    }
}