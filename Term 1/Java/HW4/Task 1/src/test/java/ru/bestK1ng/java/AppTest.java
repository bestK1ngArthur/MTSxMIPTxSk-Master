package ru.bestK1ng.java;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import ru.bestK1ng.java.Ships.GoodsType;
import ru.bestK1ng.java.Ships.Pier;
import ru.bestK1ng.java.Ships.Shippable;
import ru.bestK1ng.java.Ships.Tunnel;

public class AppTest {

    @Test
    void testPierVolumeValue() {
        GoodsType goodsType = GoodsType.BANANA;
        Pier pier = new Pier(goodsType);

        ShipMock smallShip = new ShipMock(goodsType, Shippable.Volume.SMALL);
        ShipMock mediumShip = new ShipMock(goodsType, Shippable.Volume.MEDIUM);
        ShipMock largeShip = new ShipMock(goodsType, Shippable.Volume.LARGE);

        pier.moor(smallShip);
        pier.moor(mediumShip);
        pier.moor(largeShip);

        Assertions.assertThat(smallShip.value).isEqualTo(10);
        Assertions.assertThat(mediumShip.value).isEqualTo(50);
        Assertions.assertThat(largeShip.value).isEqualTo(100);
    }

    @Test
    void testPierGoodsType() {
        GoodsType goodsType = GoodsType.BANANA;
        Pier pier = new Pier(goodsType);

        ShipMock rightShip = new ShipMock(goodsType, Shippable.Volume.SMALL);
        ShipMock wrongShip = new ShipMock(GoodsType.CLOTHES, Shippable.Volume.SMALL);

        pier.moor(rightShip);
        pier.moor(wrongShip);

        Assertions.assertThat(rightShip.value).isEqualTo(10);
        Assertions.assertThat(wrongShip.value).isEqualTo(null);
    }

    @Test
    void testTunnel() {
        Integer duration = 1000;
        Tunnel tunnel = new Tunnel(1, duration);
        ShipMock ship = new ShipMock(GoodsType.CLOTHES, Shippable.Volume.SMALL);

        tunnel.goThrough(ship);

        Assertions.assertThat(ship.duration).isEqualTo(duration);
    }
}
