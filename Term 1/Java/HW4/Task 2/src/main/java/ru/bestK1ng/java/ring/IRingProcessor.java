package ru.bestK1ng.java.ring;

public interface IRingProcessor {
    void startProcessing();
    void sendDataPackage(DataPackage dataPackage, int fromNodeId);

    void logTripTime(long time);
    void logWaitingTime(long time);
    void logException(Exception exception);
}
