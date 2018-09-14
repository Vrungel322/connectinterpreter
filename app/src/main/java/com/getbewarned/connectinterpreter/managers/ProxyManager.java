package com.getbewarned.connectinterpreter.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ProxyManager extends ProxySelector {
    private static final String PROXY_IP = "proxy_ip";
    private static final String PROXY_PORT = "proxy_port";
    private static final String PROXY_ENABLED = "proxy_enabled";

    private ProxySelector oldSelector;
    private SharedPreferences sharedPreferences;
    private UserManager userManager;

    private String ip = "136.144.31.26";
    private int port = 3128;

    public ProxyManager(Context context) {
        oldSelector = ProxySelector.getDefault();
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        ip = sharedPreferences.getString(PROXY_IP, ip);
        port = sharedPreferences.getInt(PROXY_PORT, port);
        userManager = new UserManager(context);
    }

    @Override
    public List<Proxy> select(URI uri) {
        List<Proxy> proxies = oldSelector.select(uri);
        if (true) {
            return proxies;
        }
        List<Proxy> newProxies = new ArrayList<>();
        InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
        newProxies.add(proxy);

        for (Proxy proxy1 : proxies) {
            if (proxy1.type() != Proxy.Type.DIRECT) {
                newProxies.add(proxy1);
            }
        }

        return newProxies;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        oldSelector.connectFailed(uri, sa, ioe);
    }

    public void updateProxyData(String ip, int port) {
        this.ip = ip;
        this.port = port;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PROXY_PORT, port);
        editor.putString(PROXY_IP, ip);
        editor.apply();
    }


}
