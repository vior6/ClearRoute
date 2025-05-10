package net.vior6.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigParser {

    public static Config parseArgs(String[] args) {
        Map<String, String> argMap = new HashMap<>();

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].startsWith("-")) {
                argMap.put(args[i], args[i + 1]);
                i++;
            }
        }

        String ip = argMap.get("-ip");
        int port = Integer.parseInt(argMap.get("-port"));
        String username = argMap.get("-username");
        String password = argMap.get("-password");

        if (ip == null || password == null || port == 0) {
            throw new IllegalArgumentException("Missing required arguments. Use -ip <ip> -port <port> -password <pwd>");
        }

        return new Config(ip, port, username, password);
    }
}
