package me.code;

public class Main {
    public static void main(String[] args) {
        NodeHandler.getInstance();
        new ReverseProxyServer(5001).start();
    }
}