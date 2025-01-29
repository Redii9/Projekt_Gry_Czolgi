package org.rewera.ptak.raczynski;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Klasa reprezentująca panel gry, w którym odbywa się rozgrywka.
 */
class GamePanel extends JPanel implements ActionListener, KeyListener {

    /**
     * Timer odpowiedzialny za regularne odświeżanie gry.
     */
    private Timer timer;

    /**
     * Czołg gracza 1.
     */
    private Tank player1Tank;

    /**
     * Czołg gracza 2.
     */
    private Tank player2Tank;

    /**
     * Lista pocisków wystrzelonych w grze.
     */
    private ArrayList<Bullet> bullets;

    /**
     * Lista przeszkód na planszy.
     */
    private List<Obstacle> obstacles;

    /**
     * Flaga określająca, czy to tura gracza 1.
     */
    private boolean isPlayer1Turn;

    /**
     * Flaga mówiąca, czy gracz może strzelać.
     */
    private boolean canShoot;

    /**
     * Funkcja zwrotna wywoływana po wygranej.
     */
    private Consumer<String> onWin;

    /**
     * Flaga mówiąca, czy strzał jest w trakcie.
     */
    private boolean shotInProgress;

    /**
     * Kolor ziemi na planszy.
     */
    private final Color groundColor = new Color(139, 69, 19);


    /**
     * Flaga określająca, czy gra jest w trybie multiplayer.
     */
    private boolean isMultiplayer;

    /**
     * Ostatni stan gry, który otrzymano od serwera.
     */
    private GameState latestState;

    /**
     * Flaga mówiąca, czy zmiany pochodzą z lokalnego urządzenia.
     */
    private boolean isLocalUpdate = false;

    /**
     * Bieżący stan gry.
     */
    private GameState state;

    /**
     * Gniazdo komunikacyjne w trybie multiplayer.
     */
    private Socket socket;

    /**
     * Strumień do wysyłania danych do serwera.
     */
    private ObjectOutputStream out;

    /**
     * Strumień do odbierania danych od serwera.
     */
    private ObjectInputStream in;

    /**
     * Flaga określająca, czy gracz jest pierwszym graczem (w trybie multiplayer).
     */
    private boolean isPlayer1; // Flaga określająca rolę gracza

    /**
     * Flaga określająca, czy jest moja tura (w trybie multiplayer).
     */
    private boolean isMyTurn; // Flaga określająca, czy jest moja tura


    /**
     * Konstruktor inicjalizujący panel gry.
     *
     * @param onWin Funkcja wywoływana po wygranej.
     * @param socket Gniazdo do komunikacji w trybie multiplayer.
     */
    public GamePanel(Consumer<String> onWin, Socket socket) {
        this.onWin = onWin;
        this.socket = socket;
        this.isMultiplayer = socket != null;


        setFocusable(true);
        addKeyListener(this);

        if (isMultiplayer) {
            try {
                // Tworzenie strumieni w odpowiedniej kolejności
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                // Odbierz informację od serwera o przydzielonej roli
                this.isPlayer1 = in.readBoolean();

                // Uruchom wątek nasłuchujący na aktualizacje stanu gry
                new Thread(this::listenForUpdates).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Inicjalizacja gry (dla trybu singleplayer i multiplayer)
        player1Tank = new Tank(100, 515, Color.BLUE);
        player2Tank = new Tank(600, 515, Color.RED);
        bullets = new ArrayList<>();
        obstacles = generateObstacles(); // Generowanie przeszkód
        isPlayer1Turn = true;
        canShoot = true;
        shotInProgress = false;

        // W trybie singleplayer gracz zawsze ma turę
        if (!isMultiplayer) {
            isMyTurn = true;
        }

        timer = new Timer(16, this);
        timer.start();
    }

    /**
     * Generuje przeszkody na planszy.
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
            } while (Math.abs(x - player1Tank.getX()) < 50 || Math.abs(x - player2Tank.getX()) < 50);

            int width = random.nextInt(30) + 20;
            obstacles.add(new Obstacle(x, 530, width, 20));
        }

        return obstacles;
    }

    /**
     * Wysyła dane gry do serwera w trybie multiplayer.
     */
    private synchronized void sendGameData() {
        if (!isMultiplayer || socket == null || socket.isClosed()) return;

        try {
            GameState state = new GameState(player1Tank, player2Tank, bullets, obstacles, isPlayer1Turn);
            out.writeObject(state);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeSocket();
        }
    }

    /**
     * Zamyka gniazdo połączenia z serwerem.
     */
    private void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Nasłuchuje na aktualizacje stanu gry od serwera w trybie multiplayer.
     */
    private void listenForUpdates() {
        try {
            while (!socket.isClosed()) {
                Object receivedObject = in.readObject();
                if (receivedObject instanceof GameState) {
                    GameState state = (GameState) receivedObject;
                    SwingUtilities.invokeLater(() -> updateGameFromState(state));
                } else if (receivedObject instanceof String) {
                    String message = (String) receivedObject;
                    if (message.equals("Game is full. No more players can join.")) {
                        JOptionPane.showMessageDialog(this, message, "Game Full", JOptionPane.INFORMATION_MESSAGE);
                        break; // Zakończ pętlę, jeśli gra jest pełna
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            if (!socket.isClosed()) { // Tylko loguj błąd, jeśli gniazdo nie zostało zamknięte celowo
                e.printStackTrace();
            }
        } finally {
            closeSocket(); // Zamknij gniazdo po zakończeniu pętli
        }
    }

    /**
     * Aktualizuje stan gry na podstawie danych otrzymanych od serwera.
     *
     * @param state Stan gry otrzymany od serwera.
     */
    private void updateGameFromState(GameState state) {
        this.player1Tank = state.getPlayer1Tank();
        this.player2Tank = state.getPlayer2Tank();
        this.bullets = new ArrayList<>(state.getBullets());
        this.obstacles = new ArrayList<>(state.getObstacles());
        this.isPlayer1Turn = state.isPlayer1Turn();
        this.isMyTurn = (isPlayer1 && isPlayer1Turn) || (!isPlayer1 && !isPlayer1Turn); // Ustawienie flagi tury
        this.latestState = state;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(groundColor);
        g.fillRect(0, 530, getWidth(), getHeight() - 530);

        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }

        player1Tank.draw(g);
        player2Tank.draw(g);

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(isPlayer1Turn ? "Player 1's Turn" : "Player 2's Turn", 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Synchronizacja stanu gry z serwera (tylko dla multiplayer)
        if (isMultiplayer && latestState != null && !isLocalUpdate) {
            player1Tank = latestState.getPlayer1Tank();
            player2Tank = latestState.getPlayer2Tank();
            bullets = new ArrayList<>(latestState.getBullets());
            obstacles = new ArrayList<>(latestState.getObstacles());
            isPlayer1Turn = latestState.isPlayer1Turn();
            latestState = null; // Wyczyszczenie po aktualizacji
        }

        isLocalUpdate = false; // Reset flagi lokalnych zmian

        // Aktualizacja ruchu pocisków
        for (Bullet bullet : bullets) {
            bullet.move();
        }
        bullets.removeIf(bullet -> !bullet.isVisible());

        // Sprawdzanie trafień
        checkHits();

        // Warunki wygranej
        if (player1Tank.getHp() == 0) {
            timer.stop();
            onWin.accept("Player 2 Wins!");
            if (isMultiplayer) {
                closeSocket(); // Zamknij połączenie z serwerem
            }
        } else if (player2Tank.getHp() == 0) {
            timer.stop();
            onWin.accept("Player 1 Wins!");
            if (isMultiplayer) {
                closeSocket(); // Zamknij połączenie z serwerem
            }
        }

        // Zakończenie tury, jeśli pocisk zniknął
        if (shotInProgress && bullets.isEmpty()) {
            endTurn();
            if (isMultiplayer) {
                sendGameData(); // Wyślij nowy stan gry po zakończeniu tury (tylko dla multiplayer)
            }
        }

        repaint(); // Odśwież grafikę
    }

    /**
     * Sprawdza kolizje pocisków z czołgami i przeszkodami.
     */
    private void checkHits() {
        for (Bullet bullet : bullets) {
            if (bullet.isVisible()) {
                if (bullet.isPlayer1() && bullet.getBounds().intersects(player2Tank.getBounds())) {
                    player2Tank.decreaseHp(20);
                    bullet.setVisible(false);
                } else if (!bullet.isPlayer1() && bullet.getBounds().intersects(player1Tank.getBounds())) {
                    player1Tank.decreaseHp(20);
                    bullet.setVisible(false);
                }

                for (Obstacle obstacle : obstacles) {
                    if (bullet.getBounds().intersects(obstacle.getBounds())) {
                        bullet.setVisible(false);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Kończy turę aktualnego gracza.
     */
    private void endTurn() {
        isPlayer1Turn = !isPlayer1Turn; // Zmiana tury (działa zarówno w singleplayer, jak i multiplayer)
        canShoot = true;
        shotInProgress = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (shotInProgress || !isMyTurn) return; // Blokada ruchu podczas strzału lub gdy nie jest moja tura

        // W trybie singleplayer gracz kontroluje obu graczy na zmianę
        if (!isMultiplayer) {
            if (isPlayer1Turn) {
                // Sterowanie Player1
                if (e.getKeyCode() == KeyEvent.VK_A) moveTankLeft(player1Tank);
                if (e.getKeyCode() == KeyEvent.VK_D) moveTankRight(player1Tank);
                if (e.getKeyCode() == KeyEvent.VK_W && canShoot) shoot(player1Tank);
                if (e.getKeyCode() == KeyEvent.VK_Q) player1Tank.changeAngle(-5);
                if (e.getKeyCode() == KeyEvent.VK_E) player1Tank.changeAngle(5);
            } else {
                // Sterowanie Player2
                if (e.getKeyCode() == KeyEvent.VK_A) moveTankLeft(player2Tank);
                if (e.getKeyCode() == KeyEvent.VK_D) moveTankRight(player2Tank);
                if (e.getKeyCode() == KeyEvent.VK_W && canShoot) shoot(player2Tank);
                if (e.getKeyCode() == KeyEvent.VK_Q) player2Tank.changeAngle(-5);
                if (e.getKeyCode() == KeyEvent.VK_E) player2Tank.changeAngle(5);
            }
        }
        // W trybie multiplayer gracz kontroluje przypisanego gracza
        else {
            if (isPlayer1) {
                if (e.getKeyCode() == KeyEvent.VK_A) moveTankLeft(player1Tank);
                if (e.getKeyCode() == KeyEvent.VK_D) moveTankRight(player1Tank);
                if (e.getKeyCode() == KeyEvent.VK_W && canShoot) shoot(player1Tank);
                if (e.getKeyCode() == KeyEvent.VK_Q) player1Tank.changeAngle(-5);
                if (e.getKeyCode() == KeyEvent.VK_E) player1Tank.changeAngle(5);
            } else {
                if (e.getKeyCode() == KeyEvent.VK_A) moveTankLeft(player2Tank);
                if (e.getKeyCode() == KeyEvent.VK_D) moveTankRight(player2Tank);
                if (e.getKeyCode() == KeyEvent.VK_W && canShoot) shoot(player2Tank);
                if (e.getKeyCode() == KeyEvent.VK_Q) player2Tank.changeAngle(-5);
                if (e.getKeyCode() == KeyEvent.VK_E) player2Tank.changeAngle(5);
            }
        }
    }

    /**
     * Wystrzeliwuje pocisk z czołgu.
     *
     * @param tank Czołg, z którego wystrzeliwuje się pocisk.
     */
    private void shoot(Tank tank) {
        Bullet bullet = new Bullet(
                tank.getX() + tank.getWidth() / 2,
                tank.getY(),
                tank.getAngle(),
                isPlayer1Turn ? Color.BLUE : Color.RED,
                isPlayer1Turn
        );
        bullets.add(bullet);
        canShoot = false;
        shotInProgress = true;

        sendGameData(); // Wyślij dane do serwera
    }

    /**
     * Porusza czołgiem w lewo.
     *
     * @param tank Czołg do poruszenia.
     */
    public void moveTankLeft(Tank tank) {
        isLocalUpdate = true; // Oznaczamy, że ruch pochodzi z lokalnego wejścia
        tank.moveLeft(obstacles); // Porusz czołgiem w lewo
        sendGameData(); // Wyślij zaktualizowane dane gry do serwera
    }

    /**
     * Porusza czołgiem w prawo.
     *
     * @param tank Czołg do poruszenia.
     */
    public void moveTankRight(Tank tank) {
        isLocalUpdate = true;
        tank.moveRight(obstacles);
        sendGameData();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}


}