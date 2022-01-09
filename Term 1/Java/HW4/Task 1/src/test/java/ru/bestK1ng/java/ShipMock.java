package ru.bestK1ng.java;

import ru.bestK1ng.java.Ships.GoodsType;
import ru.bestK1ng.java.Ships.Shippable;

public class ShipMock implements Shippable {
    private GoodsType goodsType;
    private Volume volume;

    public Integer value;
    public Integer duration;

    public ShipMock(GoodsType goodsType,
                    Volume volume) {
        this.goodsType = goodsType;
        this.volume = volume;
    }

    public Volume getVolume() {
        return volume;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void goThrough(Integer duration) {
        this.duration = duration;
    }

    public void load(Integer value) {
        this.value = value;
    }
}
