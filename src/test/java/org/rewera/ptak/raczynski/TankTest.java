package org.rewera.ptak.raczynski;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy {@link Tank}.
 * Sprawdzają poprawność funkcjonalności związanej z ruchem, zmianą kąta, HP i paliwa czołgu.
 */
class TankTest {

    /**
     * Instancja czołgu używana w testach.
     */
    private Tank tank;

    /**
     * Lista przeszkód używana w testach.
     */
    private List<Obstacle> obstacles;

    /**
     * Przygotowanie testów.
     * Tworzy instancję czołgu oraz listę przeszkód przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        tank = new Tank(100, 515, Color.BLUE);
        obstacles = new ArrayList<>();
    }

    /**
     * Testuje metodę {@link Tank#moveLeft(List)}.
     * Sprawdza, czy czołg porusza się w lewo o 2 piksele.
     */
    @Test
    void testMoveLeft() {
        tank.moveLeft(obstacles);
        assertEquals(98, tank.getX()); // Przesunięcie w lewo o 2 piksele
    }

    /**
     * Testuje metodę {@link Tank#moveRight(List)}.
     * Sprawdza, czy czołg porusza się w prawo o 2 piksele.
     */
    @Test
    void testMoveRight() {
        tank.moveRight(obstacles);
        assertEquals(102, tank.getX()); // Przesunięcie w prawo o 2 piksele
    }

    /**
     * Testuje metodę {@link Tank#changeAngle(int)}.
     * Sprawdza, czy kąt czołgu zmienia się poprawnie.
     */
    @Test
    void testChangeAngle() {
        tank.changeAngle(10);
        assertEquals(55, tank.getAngle()); // Zmiana kąta o 10 stopni
    }

    /**
     * Testuje metodę {@link Tank#decreaseHp(int)}.
     * Sprawdza, czy punkty życia czołgu są zmniejszane poprawnie.
     */
    @Test
    void testDecreaseHp() {
        tank.decreaseHp(20);
        assertEquals(80, tank.getHp()); // Zmniejszenie HP o 20
    }

    /**
     * Testuje metodę {@link Tank#decreaseFuel(int)}.
     * Sprawdza, czy paliwo czołgu jest zmniejszane poprawnie.
     */
    @Test
    void testDecreaseFuel() {
        tank.decreaseFuel(10);
        assertEquals(90, tank.getFuel()); // Zmniejszenie paliwa o 10
    }

    /**
     * Testuje metodę {@link Tank#moveLeft(List)} z kolizją.
     * Sprawdza, czy czołg nie porusza się, gdy napotka przeszkodę.
     */
    @Test
    void testCollisionWithObstacle() {
        obstacles.add(new Obstacle(98, 530, 20, 20)); // Dodanie przeszkody na drodze czołgu
        tank.moveLeft(obstacles);
        assertEquals(100, tank.getX()); // Czołg nie powinien się poruszyć, bo jest przeszkoda
    }
}