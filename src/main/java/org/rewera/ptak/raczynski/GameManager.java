package org.rewera.ptak.raczynski;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.io.IOException;

/**
 * Klasa zarządzająca głównym oknem gry oraz przełączaniem między różnymi ekranami (menu, opcje, gra).
 */
class GameManager {

    /**
     * Główne okno gry.
     */
    private JFrame frame;

    /**
     * Konstruktor inicjalizujący główne okno gry.
     */
    public GameManager() {
        frame = new JFrame("Tanks Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
    }

    /**
     * Wyświetla główne menu gry.
     */
    public void showMenu() {
        frame.getContentPane().removeAll();
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1));

        JButton startButton = new JButton("Start Game");
        JButton optionsButton = new JButton("Options");
        JButton exitButton = new JButton("Exit");

        startButton.addActionListener(e -> showGameModeSelection());
        optionsButton.addActionListener(e -> showOptions());
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(startButton);
        menuPanel.add(optionsButton);
        menuPanel.add(exitButton);

        frame.add(menuPanel);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    /**
     * Wyświetla ekran wyboru trybu gry (singleplayer/multiplayer).
     */
    public void showGameModeSelection() {
        frame.getContentPane().removeAll();
        JPanel modeSelectionPanel = new JPanel();
        modeSelectionPanel.setLayout(new BorderLayout());

        JLabel modeLabel = new JLabel("Select Game Mode", SwingConstants.CENTER);
        modeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));

        JButton startGameButton = new JButton("Start Singleplayer Game");
        JButton startMultiplayerButton = new JButton("Start Multiplayer Game");
        JButton backButton = new JButton("Back to Menu");

        startGameButton.addActionListener(e -> startGame());
        startMultiplayerButton.addActionListener(e -> startMultiplayerGame());
        backButton.addActionListener(e -> showMenu());

        buttonPanel.add(startGameButton);
        buttonPanel.add(startMultiplayerButton);
        buttonPanel.add(backButton);

        modeSelectionPanel.add(modeLabel, BorderLayout.NORTH);
        modeSelectionPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(modeSelectionPanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Wyświetla ekran opcji gry.
     */
    public void showOptions() {
        frame.getContentPane().removeAll();
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(3, 1));

        JLabel volumeLabel = new JLabel("Volume: ", SwingConstants.CENTER);
        JSlider volumeSlider = new JSlider(0, 100, 50);
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> showMenu());

        optionsPanel.add(volumeLabel);
        optionsPanel.add(volumeSlider);
        optionsPanel.add(backButton);

        frame.add(optionsPanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Rozpoczyna grę w trybie singleplayer.
     */
    public void startGame() {
        frame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(this::showWinScreen, null);
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }

    /**
     * Rozpoczyna grę w trybie multiplayer, łącząc się z serwerem na podany adres IP.
     */
    public void startMultiplayerGame() {
        String serverIP = JOptionPane.showInputDialog(frame, "Enter Server IP Address:", "localhost");
        if (serverIP == null || serverIP.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Invalid IP address. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Socket socket = new Socket(serverIP, 12345);
            GamePanel gamePanel = new GamePanel(this::showWinScreen, socket);
            frame.getContentPane().removeAll();
            frame.add(gamePanel);
            frame.revalidate();
            frame.repaint();
            gamePanel.requestFocusInWindow();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to connect to the server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Wyświetla ekran zwycięstwa.
     *
     * @param winnerMessage Wiadomość wyświetlana po wygranej.
     */
    public void showWinScreen(String winnerMessage) {
        frame.getContentPane().removeAll();
        JPanel winPanel = new JPanel();
        winPanel.setLayout(new BorderLayout());

        JLabel winLabel = new JLabel(winnerMessage, SwingConstants.CENTER);
        winLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.addActionListener(e -> showMenu());

        winPanel.add(winLabel, BorderLayout.CENTER);
        winPanel.add(backToMenuButton, BorderLayout.SOUTH);

        frame.add(winPanel);
        frame.revalidate();
        frame.repaint();
    }
}