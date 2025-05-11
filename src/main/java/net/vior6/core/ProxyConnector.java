package net.vior6.core;

import net.vior6.config.Config;

public class ProxyConnector {

    public static void connect(Config config) {
        System.setProperty("http.proxyHost", config.ip());
        System.setProperty("http.proxyPort", String.valueOf(config.port()));
        System.setProperty("https.proxyHost", config.ip());
        System.setProperty("https.proxyPort", String.valueOf(config.port()));

        if (config.username() != null && !config.username().isEmpty()) {
            System.setProperty("http.proxyUser", config.username());
            System.setProperty("http.proxyPassword", config.password());

            java.net.Authenticator.setDefault(new java.net.Authenticator() {
                @Override
                protected java.net.PasswordAuthentication getPasswordAuthentication() {
                    if (getRequestorType() == RequestorType.PROXY) {
                        return new java.net.PasswordAuthentication(
                                config.username(), config.password().toCharArray());
                    }
                    return null;
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

        System.clearProperty("http.proxyUser");
        System.clearProperty("http.proxyPassword");

        java.net.Authenticator.setDefault(null);

        System.out.println("Proxy disconnected. System properties cleared.");
    }
}