package org.rewera.ptak.raczynski;

import java.awt.*;
import java.util.List;
import java.io.Serializable;

/**
 * Klasa reprezentująca czołg w grze.
 * Czołg ma określoną pozycję, kolor, kąt, punkty życia (HP) oraz paliwo.
 * Obsługuje rysowanie czołgu, poruszanie się, zmianę kąta i kolizje z przeszkodami.
 */
class Tank implements Serializable {

    /**
     * Unikalny identyfikator wersji klasy do serializacji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Współrzędna X pozycji czołgu.
     */
    private int x;

    /**
     * Współrzędna Y pozycji czołgu.
     */
    private int y;

    /**
     * Szerokość czołgu.
     */
    private final int width = 25;

    /**
     * Wysokość czołgu.
     */
    private final int height = 15;

    /**
     * Kolor czołgu.
     */
    private final Color color;

    /**
     * Kąt nachylenia lufy czołgu w stopniach.
     */
    private int angle;

    /**
     * Punkty życia (HP) czołgu.
     */
    private int hp; // Punkty życia

    /**
     * Ilość paliwa pozostałego w czołgu.
     */
    private int fuel; // Paliwo

    /**
     * Konstruktor klasy Tank.
     *
     * @param x Współrzędna X początkowej pozycji czołgu.
     * @param y Współrzędna Y początkowej pozycji czołgu.
     * @param color Kolor czołgu.
     */
    public Tank(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.angle = 45;
        this.hp = 100; // Domyślne HP
        this.fuel = 100; // Domyślne paliwo
    }

    /**
     * Rysuje czołg na ekranie.
     * Wyświetla również kąt, punkty życia i paliwo.
     *
     * @param g Obiekt {@link Graphics} używany do rysowania czołgu na ekranie.
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString("Angle: " + angle + "\u00b0", x, y - 10);
        g.drawString("HP: " + hp, x, y - 25); // Wyświetlanie HP
        g.drawString("Fuel: " + fuel, x, y - 40); // Wyświetlanie paliwa
    }

    /**
     * Przesuwa czołg w lewo, jeżeli nie ma przeszkód i dostępne jest paliwo.
     *
     * @param obstacles Lista przeszkód, z którymi może kolidować czołg.
     */
    public void moveLeft(java.util.List<Obstacle> obstacles) {
        if (x > 0 && fuel > 0 && !isCollisionWithObstacle(x - 2, y, obstacles)) { // Sprawdzenie kolizji
            x -= 2;
            decreaseFuel(1);
        }
    }

    /**
     * Przesuwa czołg w prawo, jeżeli nie ma przeszkód i dostępne jest paliwo.
     *
     * @param obstacles Lista przeszkód, z którymi może kolidować czołg.
     */
    public void moveRight(java.util.List<Obstacle> obstacles) {
        if (x < 750 && fuel > 0 && !isCollisionWithObstacle(x + 2, y, obstacles)) { // Sprawdzenie kolizji
            x += 2;
            decreaseFuel(1);
        }
    }

    /**
     * Sprawdza, czy czołg koliduje z jakąkolwiek przeszkodą.
     *
     * @param newX Nowa współrzędna X czołgu.
     * @param newY Nowa współrzędna Y czołgu.
     * @param obstacles Lista przeszkód do sprawdzenia kolizji.
     * @return {@code true} jeśli występuje kolizja z przeszkodą, {@code false} w przeciwnym razie.
     */
    private boolean isCollisionWithObstacle(int newX, int newY, List<Obstacle> obstacles) {
        Rectangle tankBounds = new Rectangle(newX, newY, width, height);
        for (Obstacle obstacle : obstacles) {
            if (tankBounds.intersects(obstacle.getBounds())) {
                return true; // Kolizja z przeszkodą
            }
        }
        return false; // Brak kolizji
    }

    /**
     * Zmienia kąt czołgu o zadany delta.
     * Kąt czołgu jest ograniczony do zakresu [0, 180] stopni.
     *
     * @param delta Zmiana kąta (może być dodatnia lub ujemna).
     */
    public void changeAngle(int delta) {
        angle = Math.max(0, Math.min(180, angle + delta));
    }


    /**
     * Zwraca współrzędną X czołgu.
     *
     * @return Współrzędna X czołgu.
     */
    public int getX() {
        return x;
    }

    /**
     * Zwraca współrzędną Y czołgu.
     *
     * @return Współrzędna Y czołgu.
     */
    public int getY() {
        return y;
    }

    /**
     * Zwraca szerokość czołgu.
     *
     * @return Szerokość czołgu.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Zwraca kąt czołgu.
     *
     * @return Kąt czołgu w stopniach.
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Zwraca granice czołgu w postaci prostokąta.
     *
     * @return Prostokąt reprezentujący granice czołgu.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Zwraca punkty życia czołgu.
     *
     * @return Punkty życia czołgu.
     */
    public int getHp() {
        return hp;
    }

    /**
     * Zmniejsza punkty życia czołgu o zadaną wartość.
     * Zapewnia, że punkty życia nie będą poniżej 0.
     *
     * @param amount Wartość, o którą zostaną zmniejszone punkty życia.
     */
    public void decreaseHp(int amount) {
        hp = Math.max(0, hp - amount);
    }

    /**
     * Zwraca ilość paliwa pozostałego w czołgu.
     *
     * @return Ilość paliwa.
     */
    public int getFuel() {
        return fuel;
    }

    /**
     * Zmniejsza ilość paliwa w czołgu o zadaną wartość.
     * Zapewnia, że paliwo nie będzie poniżej 0.
     *
     * @param amount Wartość, o którą zostanie zmniejszone paliwo.
     */
    public void decreaseFuel(int amount) {
        fuel = Math.max(0, fuel - amount);
    }
}
