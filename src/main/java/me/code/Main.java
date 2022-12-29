package me.code;

import me.code.proxy.ReverseProxyServer;

public class Main {
    public static void main(String[] args) {
        new ReverseProxyServer(5001).start();
    }
}