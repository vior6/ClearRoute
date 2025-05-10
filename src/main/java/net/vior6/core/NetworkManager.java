package net.vior6.core;

import net.vior6.util.OSUtil;
import java.io.IOException;

public class NetworkManager {

    public static void restoreDefaultRoute() {
        OSUtil.OSType os = OSUtil.getOSType();
        System.out.println("🔧 Restoring network settings for OS: " + os);

        switch (os) {
            case WINDOWS -> runCommand("ipconfig", "/flushdns");
            case LINUX -> runCommand("sudo", "systemctl", "restart", "NetworkManager");
            case MAC -> runCommand("sh", "-c", "sudo dscacheutil -flushcache; sudo killall -HUP mDNSResponder");
            default -> System.out.println("Unsupported OS: manual reset may be required.");
        }
    }

    private static void runCommand(String... command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.inheritIO();
            Process process = builder.start();
            process.waitFor();
            System.out.println("Network settings restored.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to restore network settings: " + e.getMessage());
        }
    }
}