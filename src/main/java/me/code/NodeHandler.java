package me.code;

import java.util.ArrayList;
import java.util.List;

public class NodeHandler {

    private static final NodeHandler INSTANCE = new NodeHandler();
    public static NodeHandler getInstance() {
        return INSTANCE;
    }

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
                System.out.println("Starting: " + node.getProcess().pid());

            } catch (Exception e) {
                e.printStackTrace();
                i--;
            }
        }
    }

    public Node next() {
        if(activeNodes.isEmpty()) {
            return null;
        }

        var node = activeNodes.get(0);
        node.setRequests(node.getRequests() + 1);
        if (node.getRequests() > 3) {
            close(node);
            start(1);
            return next();
        }

        return node;
    }

    public void close(Node node) {
        System.out.println("Close: " + node.getProcess().pid());
        node.stop();
        activeNodes.remove(node);
    }
}
