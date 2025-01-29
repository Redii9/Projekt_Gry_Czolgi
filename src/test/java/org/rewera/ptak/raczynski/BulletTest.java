package org.rewera.ptak.raczynski;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy {@link Bullet}.
 * Sprawdzają poprawność funkcjonalności związanej z ruchem, widocznością i granicami pocisku.
 */
class BulletTest {

    /**
     * Instancja pocisku używana w testach.
     */
    private Bullet bullet;

    /**
     * Przygotowanie testów.
     * Tworzy instancję pocisku przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        bullet = new Bullet(100, 100, 45, Color.RED, true);
    }

    /**
     * Testuje metodę {@link Bullet#move()}.
     * Sprawdza, czy pocisk przemieszcza się poprawnie.
     */
    @Test
    void testMove() {
        bullet.move();
        assertTrue(bullet.getBounds().getX() > 100); // Pocisk powinien się przemieścić
    }

    /**
     * Testuje metodę {@link Bullet#isVisible()}.
     * Sprawdza, czy pocisk jest widoczny na początku.
     */
    @Test
    void testIsVisible() {
        assertTrue(bullet.isVisible()); // Pocisk powinien być widoczny na początku
    }

    /**
     * Testuje metodę {@link Bullet#setVisible(boolean)}.
     * Sprawdza, czy po ustawieniu widoczności na false, pocisk staje się niewidoczny.
     */
    @Test
    void testSetVisible() {
        bullet.setVisible(false);
        assertFalse(bullet.isVisible()); // Pocisk powinien być niewidoczny po ustawieniu
    }
}