package net.vior6.core;

import net.vior6.config.Config;

public class ProxyConnector {

    public static void connect(Config config) {
        System.setProperty("socksProxyHost", config.ip());
        System.setProperty("socksProxyPort", String.valueOf(config.port()));

        java.net.Authenticator.setDefault(new java.net.Authenticator() {
            @Override
            protected java.net.PasswordAuthentication getPasswordAuthentication() {
                return new java.net.PasswordAuthentication(config.username(), config.password().toCharArray());
            }
        });

        System.out.println("🧠 Routing system HTTP/HTTPS traffic via SOCKS5 Proxy with authentication...");
    }

    public static void disconnect() {
        System.clearProperty("socksProxyHost");
        System.clearProperty("socksProxyPort");
        System.out.println("🔌 Proxy disconnected. System properties cleared.");
    }
}