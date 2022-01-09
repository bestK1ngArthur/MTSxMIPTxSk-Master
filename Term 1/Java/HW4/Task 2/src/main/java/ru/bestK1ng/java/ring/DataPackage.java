package ru.bestK1ng.java.ring;

import java.util.OptionalLong;

public class DataPackage {
    private final int destinationNode;
    private final String data;
    private boolean processed;

    private final long startTime;
    private OptionalLong bufferTime;

    public DataPackage(int destinationNode, String data) {
        this.destinationNode = destinationNode;
        this.data = data;
        this.processed = false;

        startTime = System.nanoTime();
        bufferTime = null;
    }

    /**
     * Get destination node id
     * @return destination node id
     */
    public int getDestinationNode() {
        return destinationNode;
    }

    /**
     * Get start time
     * @return start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Get data
     * @return data
     */
    public String getData() {
        return data;
    }

    /**
     * Get processed state
     * @return processed state
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Set processed state
     * @param processed processed state
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    /**
     * Get buffer time
     * @return buffer time
     */
    public OptionalLong getBufferTime() {
        return bufferTime;
    }

    /**
     * Set buffer time
     * @param bufferTime buffer time
     */
    public void setBufferTime(long bufferTime) {
        this.bufferTime = OptionalLong.of(bufferTime);
    }
}
