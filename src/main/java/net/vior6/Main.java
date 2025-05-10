package net.vior6;

import net.vior6.config.*;
import net.vior6.core.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Config config = ConfigParser.parseArgs(args);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutdown detected. Cleaning up...");
                ProxyConnector.disconnect();
                NetworkManager.restoreDefaultRoute();
            }));

            System.out.println("Configuration loaded:");
            System.out.println("IP: " + config.ip());
            System.out.println("Port: " + config.port());

            ProxyConnector.connect(config);
            ProxyTester.testConnection();

            System.out.println("Proxy is active. Type 'exit' to disconnect.");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().trim().equalsIgnoreCase("exit")) {
                    break;
                }
            }

            System.out.println("Exiting...");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.out.println("Usage: java -jar ProxyClient.jar -ip <ip> -port <port> -username <username> -password <password>");
        }
    }
}