package me.code;

import io.netty.channel.Channel;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Node {

    private final int port;
    private Process process;
    private int requests;
    private List<Channel> connections;

    public Node(int port ) {
        this.port = port;
        this.requests = 0;
        this.connections = new ArrayList<>();
    }

    public void start() throws IOException {
        var args = new String[] {
                "java",
                "-jar",
                "spring.jar",
                "--server.port=" + port
        };

        this.process = Runtime
                .getRuntime().exec(args);
    }

    public void stop() {
        if (process == null) {
            return;
        }

        process.destroy();
        System.out.println("Node destroyed: " + process.pid());
        process = null;
    }

    public void addRequest() {
        this.requests++;
    }

    public void addConnection(Channel channel) {
        this.connections.add(channel);
    }

    public void removeConnection(Channel channel) {
        this.connections.remove(channel);
    }

    public void resetRequests() {
        this.requests = 0;
    }
}
