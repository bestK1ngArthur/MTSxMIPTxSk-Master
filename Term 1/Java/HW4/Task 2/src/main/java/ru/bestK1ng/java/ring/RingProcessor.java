package ru.bestK1ng.java.ring;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class RingProcessor implements IRingProcessor {
    private final int nodesAmount;
    private final int dataAmount;

    private final int coordinatorId = 0;
    private final int threadsPerNode = 3;

    private final File logs;
    private final Logger logger;

    private List<Long> tripTimeList;
    private List<Long> waitingTimeList;

    private List<Node> nodeList;

    public RingProcessor(int nodesAmount, int dataAmount, File logs) {
        this.nodesAmount = nodesAmount;
        this.dataAmount = dataAmount;
        this.logs = logs;

        logger = Logger.getLogger("ringLogger");

        tripTimeList = Collections.synchronizedList(new ArrayList());
        waitingTimeList = Collections.synchronizedList(new ArrayList());

        initNodes();
        initData();

        logInitialInfo();
    }

    /**
     * Starts processing data packages along nodes
     */
    @Override
    public void startProcessing() {
        for (int i = 0; i < nodesAmount; i++) {
            Node node = nodeList.get(i);

            for (int j = 0; j < threadsPerNode; j++) {
                Thread thread = new Thread(node);
                thread.start();
            }
        }
    }

    /**
     * Send data package from one node to next node
     * @param dataPackage data package
     * @param fromNodeId from node id
     */
    @Override
    public void sendDataPackage(DataPackage dataPackage, int fromNodeId) {
        int toNodeIndex = (fromNodeId + 1) % nodesAmount;
        logger.info("Send package '" + dataPackage.getData()  + "' from #" + fromNodeId + " to #" + toNodeIndex);
        nodeList.get(toNodeIndex).setData(dataPackage);
    }

    /**
     * Log trip time
     * @param time time to log
     */
    @Override
    public void logTripTime(long time) {
        tripTimeList.add(time);
        logger.info("Data Package reach destination with average time " + averageTripTime());
    }

    /**
     * Log package waiting time
     * @param time time to log
     */
    @Override
    public void logWaitingTime(long time) {
        waitingTimeList.add(time);
        logger.info("Data Package wating in buffer with average time " + averageWaitingTime());
    }

    /**
     * Log handled exception
     * @param exception exception to log
     */
    @Override
    public void logException(Exception exception) {
        logger.warning("Catch exception " + exception.getMessage());
    }

    private void logInitialInfo() {
        logger.info("Ring created with " + nodeList.size() + " nodes");
        logger.info("Coordinator is node #" + coordinatorId);

        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            logger.info("Node #" + i + " has " + node.getBuffer().getSize() + " messages");
        }
    }

    private void initNodes() {
        nodeList = Collections.synchronizedList(new ArrayList());

        for (int i = 0; i < nodesAmount; i++) {
            nodeList.add(new Node(i, coordinatorId, threadsPerNode, this));
        }
    }

    private void initData() {
        for (int i = 0; i < dataAmount; i++) {
            int from = (int) (Math.random() * nodesAmount);
            int to = (int) (Math.random() * nodesAmount);
            DataPackage dataPackage = new DataPackage(to, "Data Package #" + i);
            nodeList.get(from).setData(dataPackage);
        }
    }

    private long averageTripTime() {
        return getAverageTime(tripTimeList);
    }

    private long averageWaitingTime() {
        return getAverageTime(waitingTimeList);
    }

    private long getAverageTime(List<Long> list) {
        Double time = list
                .stream()
                .mapToDouble(a -> a)
                .average()
                .orElse(0);
        return Math.round(time);
    }
}
