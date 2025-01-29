package org.rewera.ptak.raczynski;

import javax.swing.*;

/**
 * Główna klasa aplikacji.
 * Uruchamia grę poprzez wywołanie metody showMenu na instancji GameManager.
 */
public class Main {

    /**
     * Główna metoda uruchamiająca aplikację.
     * Tworzy instancję {@link GameManager} i wywołuje metodę {@link GameManager#showMenu()}.
     *
     * @param args Argumenty przekazane w linii komend (nie są używane w tej aplikacji).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameManager gameManager = new GameManager();
            gameManager.showMenu();
        });
    }
}