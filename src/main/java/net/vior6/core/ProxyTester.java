package net.vior6.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URI;

public class ProxyTester {

    public static void testConnection() {
        try {
            URI uri = URI.create("https://api.ipify.org");
            URL url = uri.toURL();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String publicIp = reader.readLine();
                System.out.println("Public IP (via proxy): " + publicIp);
            }
        } catch (Exception e) {
            System.err.println("Failed to connect through proxy: " + e.getMessage());
        }
    }
}