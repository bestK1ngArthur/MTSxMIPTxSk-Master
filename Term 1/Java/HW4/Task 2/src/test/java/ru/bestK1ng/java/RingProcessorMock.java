package ru.bestK1ng.java;

import ru.bestK1ng.java.ring.DataPackage;
import ru.bestK1ng.java.ring.IRingProcessor;

public class RingProcessorMock implements IRingProcessor {
    @Override
    public void startProcessing() {

    }

    @Override
    public void sendDataPackage(DataPackage dataPackage, int fromNodeId) {

    }

    @Override
    public void logTripTime(long time) {

    }

    @Override
    public void logWaitingTime(long time) {

    }

    @Override
    public void logException(Exception exception) {

    }
}
