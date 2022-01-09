package ru.bestK1ng.java.Ships;

public class Pier {
    private static final int VOLUME_SMALL_VALUE = 10;
    private static final int VOLUME_MEDIUM_VALUE = 50;
    private static final int VOLUME_LARGE_VALUE = 100;

    private GoodsType goodsType;

    public Pier(GoodsType goodsType) {
        this.goodsType = goodsType;
    }

    /**
     * Функция для загрузки корабля товарами
     * @param ship - корабль
     */
    public synchronized void moor(Shippable ship) {
        if (ship.getGoodsType() != goodsType) {
            // Is not suitable ship, skip loading
            return;
        }

        ship.load(getValue(ship.getVolume()));
    }

    private Integer getValue(Shippable.Volume shipVolume) {
        switch (shipVolume) {
            case SMALL: return VOLUME_SMALL_VALUE;
            case MEDIUM: return VOLUME_MEDIUM_VALUE;
            case LARGE: return VOLUME_LARGE_VALUE;
        }

        return 0;
    }
}
