package ru.bestK1ng.java.Ships;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Ship
        extends Thread
        implements Shippable {
    private static final int VALUE_LOAD_TIME = 100;

    private Integer number;
    private GoodsType goodsType;
    private Volume volume;

    private Tunnel tunnel;
    private Pier pier;

    private State state;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public Ship(Integer number,
                GoodsType goodsType,
                Volume volume) {
        this.number = number;
        this.goodsType = goodsType;
        this.volume = volume;

        setState(State.SAILED_OFF);
    }

    /**
     * Функция описывающая жизненный цикл корабля
     */
    @Override
    public void run() {
        super.run();

        setState(State.WAITING_TUNNEL);

        tunnel.goThrough(this);
        pier.moor(this);
    }

    /**
     * Функция для прохождения корабля сквозь тунель
     * @param duration - длительность прохождения
     */
    public void goThrough(Integer duration) {
        if (state != State.WAITING_TUNNEL) {
            return;
        }

        setState(State.IN_TUNNEL);

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setState(State.WAITING_LOADING);
        tunnel = null;
    }

    /**
     * Функция для загрузки корабля товарами
     * @param value - количество товаров
     */
    public void load(Integer value) {
        if (state == State.LOADED || state == State.LOADING) {
            return;
        }

        setState(State.LOADING);

        try {
            Thread.sleep(VALUE_LOAD_TIME * value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setState(State.LOADED);
        pier = null;
    }

    /**
     * Получение вместительности корабля
     * @return возвращает вместительность корабля
     */
    public Volume getVolume() {
        return volume;
    }

    /**
     * Получение типа провозимых товаров на корабле
     * @return возвращает тип провозимых товаров
     */
    public GoodsType getGoodsType() {
        return goodsType;
    }

    /**
     * Функция задания тунеля, через который проходит корабль
     * @param tunnel - тунель
     */
    public void setTunnel(Tunnel tunnel) {
        this.tunnel = tunnel;
    }

    /**
     * Функция задания причала, где кораблю необходимо осуществить загрузу товарами
     * @param pier - причал для загрузки
     */
    public void setPier(Pier pier) {
        this.pier = pier;
    }

    private void setState(State state) {
        String time = dateFormat.format(Calendar.getInstance().getTime());
        String log = time + ": Ship #" + number
                + " (goods: " + goodsType
                + ", volume: " + volume
                + ") is " + state;
        System.out.println(log);
        this.state = state;
    }

    enum State {
        SAILED_OFF,
        WAITING_TUNNEL,
        IN_TUNNEL,
        WAITING_LOADING,
        LOADING,
        LOADED
    }
}
