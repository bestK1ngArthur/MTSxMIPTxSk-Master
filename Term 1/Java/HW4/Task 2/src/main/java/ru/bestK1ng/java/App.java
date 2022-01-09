package ru.bestK1ng.java;

import ru.bestK1ng.java.ring.RingProcessor;

import java.io.File;

public final class App {
    private static final int NODES_AMOUNT = 10;
    private static final int DATA_AMOUNT = 3;

    private App() {
    }

    public static void main(String[] args) {
        RingProcessor processor = new RingProcessor(NODES_AMOUNT, DATA_AMOUNT, new File("tokenRingLog"));
        processor.startProcessing();
    }
}
