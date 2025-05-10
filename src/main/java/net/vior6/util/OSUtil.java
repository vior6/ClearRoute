package net.vior6.util;

public class OSUtil {

    public enum OSType {
        WINDOWS, LINUX, MAC, UNKNOWN
    }

    public static OSType getOSType() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return OSType.WINDOWS;
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) return OSType.LINUX;
        if (os.contains("mac")) return OSType.MAC;
        return OSType.UNKNOWN;
    }
}