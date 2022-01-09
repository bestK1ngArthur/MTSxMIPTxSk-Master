package ru.bestK1ng.java;

import ru.bestK1ng.java.Ships.Tunnel;
import ru.bestK1ng.java.Ships.Pier;
import ru.bestK1ng.java.Ships.GoodsType;
import ru.bestK1ng.java.Ships.ShipsGenerator;
import ru.bestK1ng.java.Ships.Ship;

import java.util.HashMap;
import java.util.Map;

public final class App {
    private static final Integer SHIPS_COUNT = 10;
    private static final Integer TUNNEL_CAPACITY = 5;
    private static final Integer TUNNEL_DURATION = 1000;
    private static final Integer GENERATOR_SLEEP = 100;

    private App() {
    }

    public static void main(String[] args) {
        startShips();
    }

    private static void startShips() {
        Tunnel tunnel = new Tunnel(TUNNEL_CAPACITY, TUNNEL_DURATION);

        Map<GoodsType, Pier> piers = new HashMap<>();
        for (GoodsType goodsType : GoodsType.values()) {
            piers.put(goodsType, new Pier(goodsType));
        }

        ShipsGenerator shipsGenerator = new ShipsGenerator(tunnel, piers);
        try {
            for (int i = 0; i < SHIPS_COUNT; i++) {
                Ship ship = shipsGenerator.getNext();
                ship.start();

                Thread.sleep(GENERATOR_SLEEP);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
