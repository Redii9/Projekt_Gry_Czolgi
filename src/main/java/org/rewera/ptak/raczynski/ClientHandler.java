package org.rewera.ptak.raczynski;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Obsługuje połączenie klienta z serwerem gry.
 * Implementuje interfejs Runnable, co pozwala na obsługę w oddzielnym wątku.
 */
public class ClientHandler implements Runnable {

    /**
     * Gniazdo połączenia klienta.
     */
    private Socket socket;

    /**
     * Instancja gry, do której klient jest podłączony.
     */
    private GameInstance gameInstance;

    /**
     * Strumień wyjściowy do przesyłania danych do klienta.
     */
    private ObjectOutputStream out;

    /**
     * Strumień wejściowy do odbierania danych od klienta.
     */
    private ObjectInputStream in;

    /**
     * Określa, czy klient jest graczem 1.
     */
    private boolean isPlayer1;

    /**
     * Tworzy nową instancję obsługi klienta.
     *
     * @param socket        Gniazdo połączenia klienta.
     * @param gameInstance  Instancja gry, do której klient jest podłączony.
     * @param isPlayer1     Określa, czy klient jest graczem 1.
     * @param out           Strumień wyjściowy do przesyłania danych do klienta.
     * @param in            Strumień wejściowy do odbierania danych od klienta.
     */
    public ClientHandler(Socket socket, GameInstance gameInstance, boolean isPlayer1, ObjectOutputStream out, ObjectInputStream in) {
        this.socket = socket;
        this.gameInstance = gameInstance;
        this.isPlayer1 = isPlayer1;
        this.out = out;
        this.in = in;
    }

    /**
     * Uruchamia obsługę klienta.
     * Odbiera dane od klienta i aktualizuje stan gry.
     */
    @Override
    public void run() {
        try {
            while (true) {
                GameState clientState = (GameState) in.readObject();
                gameInstance.updateGameState(clientState);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Player disconnected.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameInstance.removeClient(this);
        }
    }

    /**
     * Wysyła aktualny stan gry do klienta.
     *
     * @param gameState Stan gry do wysłania.
     */
    public void sendGameState(GameState gameState) {
        try {
            out.writeObject(gameState);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sprawdza, czy klient jest graczem 1.
     *
     * @return true, jeśli klient jest graczem 1, false w przeciwnym razie.
     */
    public boolean isPlayer1() {
        return isPlayer1;
    }
}