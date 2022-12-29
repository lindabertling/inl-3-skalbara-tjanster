package me.code.balancer;

import me.code.Node;

import java.util.List;

public interface LoadBalancer {

    Node next(List<Node> nodes);
}
