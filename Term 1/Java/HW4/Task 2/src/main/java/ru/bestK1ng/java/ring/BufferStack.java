package ru.bestK1ng.java.ring;

import java.util.concurrent.ConcurrentLinkedQueue;

public class BufferStack<Data> {
    private ConcurrentLinkedQueue<Data> buffer;

    public BufferStack() {
        buffer = new ConcurrentLinkedQueue<>();
    }

    /**
     * Returns last element in buffer
     * @return last element in buffer
     */
    public Data getData() {
        return buffer.peek();
    }

    /**
     * Removes and returns last element in buffer
     * @return last element in buffer
     */
    public Data pollData() {
        return buffer.poll();
    }

    /**
     * Add element to buffer
     * @param data element to be added
     */
    public void putData(Data data) {
        buffer.add(data);
    }

    /**
     * Returns size of buffer
     * @return size of buffer
     */
    public int getSize() {
        return buffer.size();
    }
}
