package net.vior6.core;

import net.vior6.config.Config;

public class ProxyConnector {

    public static void connect(Config config) {
        System.setProperty("http.proxyHost", config.ip());
        System.setProperty("http.proxyPort", String.valueOf(config.port()));
        System.setProperty("https.proxyHost", config.ip());
        System.setProperty("https.proxyPort", String.valueOf(config.port()));

        if (config.username() != null && !config.username().isEmpty()) {
            java.net.Authenticator.setDefault(new java.net.Authenticator() {
                @Override
                protected java.net.PasswordAuthentication getPasswordAuthentication() {
                    return new java.net.PasswordAuthentication(
                            config.username(), config.password().toCharArray());
                }
            });
        }

        System.out.println("Routing system HTTP/HTTPS traffic via HTTP Proxy with authentication...");
    }

    public static void disconnect() {
        System.clearProperty("http.proxyHost");
        System.clearProperty("http.proxyPort");
        System.clearProperty("https.proxyHost");
        System.clearProperty("https.proxyPort");
        System.out.println("Proxy disconnected. System properties cleared.");
    }
}