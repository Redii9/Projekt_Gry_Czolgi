package org.rewera.ptak.raczynski;

import java.awt.*;
import java.io.Serializable;

/**
 * Klasa reprezentująca przeszkodę w grze.
 * Obiekt tej klasy jest wykorzystywany do rysowania przeszkód na ekranie.
 * Przeszkoda jest obiektem prostokątnym o określonym położeniu i rozmiarze.
 */
class Obstacle implements Serializable {

    /**
     * Unikalny identyfikator wersji klasy do serializacji.
     */
    private static final long serialVersionUID = 1L; // Dodanie wersji dla serializacji

    /**
     * Współrzędna X lewego górnego rogu przeszkody.
     */

    /**
     * Współrzędna X lewego górnego rogu przeszkody.
     */
    private final int x;

    /**
     * Współrzędna Y lewego górnego rogu przeszkody.
     */
    private final int y;

    /**
     * Szerokość przeszkody.
     */
    private final int width;

    /**
     * Wysokość przeszkody.
     */
    private final int height;

    /**
     * Konstruktor klasy Obstacle.
     *
     * @param x Współrzędna X lewego górnego rogu przeszkody.
     * @param y Współrzędna Y lewego górnego rogu przeszkody.
     * @param width Szerokość przeszkody.
     * @param height Wysokość przeszkody.
     */
    public Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Rysuje przeszkodę na ekranie.
     *
     * @param g Obiekt {@link Graphics} używany do rysowania przeszkody.
     */
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x, y - height, width, height); // Przesunięcie na poziom podłoża
    }

    /**
     * Zwraca prostokątne ograniczenie przeszkody.
     * Używane do sprawdzania kolizji z innymi obiektami.
     *
     * @return Prostokąt reprezentujący granice przeszkody.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y - height, width, height);
    }
}
