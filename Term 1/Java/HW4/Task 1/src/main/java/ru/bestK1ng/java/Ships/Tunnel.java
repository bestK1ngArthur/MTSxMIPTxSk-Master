package ru.bestK1ng.java.Ships;

import java.util.concurrent.Semaphore;

public class Tunnel {
    private Semaphore semaphore;
    private Integer duration;

    public Tunnel(Integer capacity, Integer duration) {
        semaphore = new Semaphore(capacity);
        this.duration = duration;
    }

    /**
     * Функция прохождения корабля сквозь тунель
     * @param ship - корабль
     */
    public void goThrough(Shippable ship) {
        try {
            semaphore.acquire();
            ship.goThrough(duration);
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
