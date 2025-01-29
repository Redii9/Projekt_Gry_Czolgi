package org.rewera.ptak.raczynski;

import java.awt.*;
import java.io.Serializable;

/**
 * Klasa reprezentująca pocisk w grze.
 * Implementuje interfejs Serializable, co umożliwia jej serializację.
 */
class Bullet implements Serializable {

    /**
     * Początkowa pozycja X pocisku.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Początkowa pozycja X pocisku.
     */
    private double x;

    /**
     * Początkowa pozycja Y pocisku.
     */
    private double y;

    /**
     * Średnica pocisku.
     */
    private final int diameter = 10;

    /**
     * Kolor pocisku.
     */
    private final Color color;

    /**
     * Określa, czy pocisk jest widoczny.
     */
    private boolean visible;

    /**
     * Prędkość pocisku.
     */
    private final double velocity = 10;

    /**
     * Kąt strzału pocisku w radianach.
     */
    private final double angleRadians;

    /**
     * Przemieszczenie pocisku w osi X.
     */
    private double dx;

    /**
     * Przemieszczenie pocisku w osi Y.
     */
    private double dy;

    /**
     * Określa, czy pocisk należy do gracza 1.
     */
    private final boolean isPlayer1;

    /**
     * Konstruktor klasy Bullet.
     *
     * @param x         Początkowa pozycja X pocisku.
     * @param y         Początkowa pozycja Y pocisku.
     * @param angle     Kąt strzału w stopniach.
     * @param color     Kolor pocisku.
     * @param isPlayer1 Określa, czy pocisk należy do gracza 1.
     */
    public Bullet(int x, int y, int angle, Color color, boolean isPlayer1) {
        this.x = x - diameter / 2.0;
        this.y = y;
        this.color = color;
        this.visible = true;
        this.angleRadians = Math.toRadians(angle);
        this.isPlayer1 = isPlayer1; // Przypisanie właściciela pocisku

        this.dx = velocity * Math.cos(angleRadians);
        this.dy = -velocity * Math.sin(angleRadians);
    }

    /**
     * Rysuje pocisk na ekranie.
     *
     * @param g Obiekt Graphics używany do rysowania.
     */
    public void draw(Graphics g) {
        if (visible) {
            g.setColor(color);
            g.fillOval((int) x, (int) y, diameter, diameter);
        }
    }

    /**
     * Przesuwa pocisk zgodnie z jego trajektorią.
     * Sprawdza, czy pocisk uderzył w podłoże lub opuścił ekran.
     */
    public void move() {
        x += dx;
        y += dy;
        dy += 0.2;

        // Sprawdzenie, czy pocisk uderzył w podłoże
        if (y + diameter >= 530) { // Używamy y + średnica pocisku
            visible = false;
        }

        // Sprawdzenie, czy pocisk wyleciał poza ekran
        if (y > 600 || x < 0 || x > 800) {
            visible = false;
        }
    }


    /**
     * Sprawdza, czy pocisk jest widoczny.
     *
     * @return true, jeśli pocisk jest widoczny, false w przeciwnym razie.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Zwraca prostokąt określający granice pocisku.
     *
     * @return Obiekt Rectangle reprezentujący granice pocisku.
     */
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, diameter, diameter);
    }

    /**
     * Ustawia widoczność pocisku.
     *
     * @param visible true, jeśli pocisk ma być widoczny, false w przeciwnym razie.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Sprawdza, czy pocisk należy do gracza 1.
     *
     * @return true, jeśli pocisk należy do gracza 1, false w przeciwnym razie.
     */
    public boolean isPlayer1() {
        return isPlayer1; // Zwraca właściciela pocisku
    }
}