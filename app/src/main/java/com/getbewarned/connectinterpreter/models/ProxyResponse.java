package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class ProxyResponse {
    @Expose
    String ip;
    @Expose
    int port;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
