package ru.bestK1ng.java.ring;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Node implements Runnable {
    private final int nodeId;
    private final int coordinatorId;
    private Semaphore semaphore;
    private IRingProcessor ringProcessor;
    private List<DataPackage> allData;

    private BufferStack<DataPackage> bufferStack = new BufferStack<>();

    public Node(int nodeId, int coordinatorId, int threadsPerNode, IRingProcessor ringProcessor) {
        this.nodeId = nodeId;
        this.coordinatorId = coordinatorId;
        this.semaphore = new Semaphore(threadsPerNode);
        this.ringProcessor = ringProcessor;

        if (isCoordinator()) {
            allData = new ArrayList<>();
        }
    }

    /**
     * Returns node id
     * @return node id
     */
    public long getId() {
        return nodeId;
    }

    /**
     * Returns last data package in buffer
     * @return last data package in buffer
     */
    public DataPackage getData() {
        return bufferStack.getData();
    }

    /**
     * Returns buffer
     * @return buffer
     */
    public BufferStack<DataPackage> getBuffer() {
        return bufferStack;
    }

    /**
     * Set data package in buffer
     * @param dataPackage data package
     */
    public void setData(DataPackage dataPackage) {
        dataPackage.setBufferTime(System.nanoTime());
        bufferStack.putData(dataPackage);
    }

    /**
     * Run logic
     */
    @Override
    public void run() {
        while (true) {
            try {
                semaphore.acquire();
                processData();
                semaphore.release();
            } catch (InterruptedException e) {
                ringProcessor.logException(e);
            }
        }
    }

    private boolean isCoordinator() {
        return nodeId == coordinatorId;
    }

    private void processData() {
        DataPackage dataPackage = bufferStack.pollData();

        if (dataPackage == null) {
            return;
        }

        if (dataPackage.getBufferTime().isPresent()) {
            long bufferTime = dataPackage.getBufferTime().getAsLong();
            ringProcessor.logWaitingTime(System.nanoTime() - bufferTime);
        }

        if (isCoordinator() && dataPackage.isProcessed()) {
            allData.add(dataPackage);
        } else if (dataPackage.getDestinationNode() == nodeId) {
            ringProcessor.logTripTime(System.nanoTime() - dataPackage.getStartTime());

            try {
                Thread.sleep(1);
                dataPackage.setProcessed(true);
                ringProcessor.sendDataPackage(dataPackage, nodeId);
            } catch (InterruptedException e) {
                ringProcessor.logException(e);
            }
        } else {
            ringProcessor.sendDataPackage(dataPackage, nodeId);
        }
    }
}
