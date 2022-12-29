package me.code.balancer;

import me.code.Node;

import java.util.List;

public class RoundRobinBalancer implements LoadBalancer{

    private int index = 0;


    @Override
    public Node next(List<Node> nodes) {
        if (nodes.isEmpty()) {
            throw new RuntimeException("No active nodes.");
        }

        index++;
        if (index >= nodes.size()) {
            index = 0;
        }

        return nodes.get(index);
    }
}
