package ru.bestK1ng.java.Ships;

import java.util.Map;
import java.util.Random;

public class ShipsGenerator {
    private Integer number = 0;

    private Tunnel tunnel;
    private Map<GoodsType, Pier> piers;

    public ShipsGenerator(Tunnel tunnel, Map<GoodsType, Pier> piers) {
        this.tunnel = tunnel;
        this.piers = piers;
    }

    /**
     * Функция для генерации следующего корабля
     * @return возвращает следующий корабль
     */
    public synchronized Ship getNext() {
        GoodsType goodsType = GoodsType.values()[new Random().nextInt(GoodsType.values().length)];
        Ship.Volume shipVolume = Ship.Volume.values()[new Random().nextInt(Ship.Volume.values().length)];

        Ship ship = new Ship(number, goodsType, shipVolume);
        ship.setTunnel(tunnel);
        ship.setPier(piers.get(goodsType));

        number++;

        return ship;
    }
}
