package org.rewera.ptak.raczynski;

import java.io.Serializable;
import java.util.List;

/**
 * Klasa przechowująca stan gry, w tym informacje o czołgach, pociskach i przeszkodach.
 * Implementuje interfejs Serializable, aby umożliwić przesyłanie obiektów przez sieć.
 */
class GameState implements Serializable {

    /**
     * Unikalny identyfikator wersji klasy do serializacji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Flaga określająca, czy jest tura gracza 1.
     */
    private final boolean isPlayer1Turn;

    /**
     * Czołg gracza 1.
     */
    private final Tank player1Tank;

    /**
     * Czołg gracza 2.
     */
    private final Tank player2Tank;

    /**
     * Lista aktywnych pocisków na planszy.
     */
    private final List<Bullet> bullets;

    /**
     * Lista przeszkód na planszy.
     */
    private final List<Obstacle> obstacles;

    /**
     * Tworzy nowy stan gry.
     *
     * @param player1Tank Czołg gracza 1
     * @param player2Tank Czołg gracza 2
     * @param bullets Lista aktywnych pocisków
     * @param obstacles Lista przeszkód na mapie
     * @param isPlayer1Turn Flaga określająca, czy jest tura gracza 1
     */
    public GameState(Tank player1Tank, Tank player2Tank, List<Bullet> bullets, List<Obstacle> obstacles, boolean isPlayer1Turn) {
        this.player1Tank = player1Tank;
        this.player2Tank = player2Tank;
        this.bullets = bullets;
        this.obstacles = obstacles;
        this.isPlayer1Turn = isPlayer1Turn;
    }

    /**
     * Zwraca czołg gracza 1.
     *
     * @return Czołg gracza 1
     */
    public Tank getPlayer1Tank() {
        return player1Tank;
    }

    /**
     * Zwraca czołg gracza 2.
     *
     * @return Czołg gracza 2
     */
    public Tank getPlayer2Tank() {
        return player2Tank;
    }

    /**
     * Zwraca listę aktywnych pocisków.
     *
     * @return Lista pocisków
     */
    public List<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Zwraca listę przeszkód na mapie.
     *
     * @return Lista przeszkód
     */
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    /**
     * Sprawdza, czy jest tura gracza 1.
     *
     * @return true, jeśli jest tura gracza 1, false w przeciwnym razie
     */
    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }
}
