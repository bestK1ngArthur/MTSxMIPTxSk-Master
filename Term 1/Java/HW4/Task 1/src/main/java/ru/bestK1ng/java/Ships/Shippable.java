package ru.bestK1ng.java.Ships;

public interface Shippable {
    Volume getVolume();
    GoodsType getGoodsType();

    void goThrough(Integer duration);
    void load(Integer value);

    enum Volume {
        SMALL,
        MEDIUM,
        LARGE
    }
}
