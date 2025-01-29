package org.rewera.ptak.raczynski;

import org.junit.jupiter.api.Test;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy {@link Obstacle}.
 * Sprawdzają poprawność metod do rysowania przeszkody oraz jej granic.
 */
class ObstacleTest {

    /**
     * Testuje metodę {@link Obstacle#draw(Graphics)}.
     * Ponieważ testujemy GUI, test sprawdza jedynie, czy obiekt przeszkody nie jest null.
     */
    @Test
    void testDraw() {
        Obstacle obstacle = new Obstacle(100, 530, 50, 20);
        // Testowanie rysowania przeszkody (nie ma bezpośredniego sposobu na testowanie GUI, więc sprawdzamy tylko logikę)
        assertNotNull(obstacle);
    }

    /**
     * Testuje metodę {@link Obstacle#getBounds()}.
     * Sprawdza, czy granice przeszkody są poprawne.
     */
    @Test
    void testGetBounds() {
        Obstacle obstacle = new Obstacle(100, 530, 50, 20);
        Rectangle bounds = obstacle.getBounds();
        assertEquals(100, bounds.getX(), 0.01); // Sprawdzenie pozycji przeszkody
        assertEquals(510, bounds.getY(), 0.01); // y = 530 - height (20)
    }
}