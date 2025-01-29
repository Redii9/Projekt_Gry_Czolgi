package org.rewera.ptak.raczynski;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy {@link GameState}.
 * Sprawdzają poprawność metod do zarządzania stanem gry, takich jak dostęp do graczy, pocisków i przeszkód.
 */
class GameStateTest {

    /**
     * Instancja stanu gry używana w testach.
     */
    private GameState gameState;

    /**
     * Czołg gracza 1 używany w testach.
     */
    private Tank player1Tank;

    /**
     * Czołg gracza 2 używany w testach.
     */
    private Tank player2Tank;

    /**
     * Lista pocisków używana w testach.
     */
    private List<Bullet> bullets;

    /**
     * Lista przeszkód używana w testach.
     */
    private List<Obstacle> obstacles;

    /**
     * Przygotowanie testów.
     * Tworzy instancje graczy, pocisków, przeszkód oraz stanu gry przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        player1Tank = new Tank(100, 515, Color.BLUE);
        player2Tank = new Tank(600, 515, Color.RED);
        bullets = new ArrayList<>();
        obstacles = new ArrayList<>();
        gameState = new GameState(player1Tank, player2Tank, bullets, obstacles, true);
    }

    /**
     * Testuje metodę {@link GameState#getPlayer1Tank()}.
     * Sprawdza, czy zwraca poprawnego gracza (czołg gracza 1).
     */
    @Test
    void testGetPlayer1Tank() {
        assertEquals(player1Tank, gameState.getPlayer1Tank());
    }

    /**
     * Testuje metodę {@link GameState#getPlayer2Tank()}.
     * Sprawdza, czy zwraca poprawnego gracza (czołg gracza 2).
     */
    @Test
    void testGetPlayer2Tank() {
        assertEquals(player2Tank, gameState.getPlayer2Tank());
    }

    /**
     * Testuje metodę {@link GameState#getBullets()}.
     * Sprawdza, czy lista pocisków w stanie gry jest poprawna.
     */
    @Test
    void testGetBullets() {
        assertEquals(bullets, gameState.getBullets());
    }

    /**
     * Testuje metodę {@link GameState#getObstacles()}.
     * Sprawdza, czy lista przeszkód w stanie gry jest poprawna.
     */
    @Test
    void testGetObstacles() {
        assertEquals(obstacles, gameState.getObstacles());
    }

    /**
     * Testuje metodę {@link GameState#isPlayer1Turn()}.
     * Sprawdza, czy początkowo tura należy do gracza 1.
     */
    @Test
    void testIsPlayer1Turn() {
        assertTrue(gameState.isPlayer1Turn());
    }
}