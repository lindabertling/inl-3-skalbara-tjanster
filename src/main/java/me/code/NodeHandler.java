package me.code;

import java.util.ArrayList;
import java.util.List;

public class NodeHandler {

    private int portCounter;
    private final List<Node> activeNodes;
    private final int minimumNodeCount;

    public NodeHandler() {
        this.portCounter = 8080;
        this.activeNodes = new ArrayList<>();
        this.minimumNodeCount = 2;

        this.start(2);
    }

    public void start(int amount) {
        for (int i = 0; i < amount; i++) {
            var node = new Node(portCounter++);
            activeNodes.add(node);

            try {
                node.start();
            } catch (Exception e) {
                e.printStackTrace();
                i--;
            }
        }
    }

    private int activeNode = 0;
    public Node next() {
        if(activeNodes.isEmpty()) {
            return null;
        }

        activeNode++;
        if(activeNode >= activeNodes.size()) {
            activeNode = 0;
        }

        return activeNodes.get(activeNode);

    }
}
