package ru.bestK1ng.java;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import ru.bestK1ng.java.ring.DataPackage;
import ru.bestK1ng.java.ring.IRingProcessor;
import ru.bestK1ng.java.ring.Node;

public class AppTest {
    private IRingProcessor ringProcessor = new RingProcessorMock();

    @Test
    void testFilledNode() {
        Node node = new Node(0, 1, 3, ringProcessor);
        node.setData(new DataPackage(1, "DATA"));
        Assertions.assertThat(node.getBuffer().getSize()).isEqualTo(1);
    }

    @Test
    void testEmptyNode() {
        Node node = new Node(0, 1, 3, ringProcessor);
        Assertions.assertThat(node.getBuffer().getSize()).isEqualTo(0);
    }
}
