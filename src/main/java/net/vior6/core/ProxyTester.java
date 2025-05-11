package net.vior6.core;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ProxyTester {

    public static void testConnection() {
        String proxyHost = System.getProperty("http.proxyHost");
        int proxyPort = Integer.parseInt(System.getProperty("http.proxyPort"));
        String username = System.getProperty("http.proxyUser");
        String password = System.getProperty("http.proxyPassword");

        try (Socket proxySocket = new Socket(proxyHost, proxyPort)) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(proxySocket.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader in = new BufferedReader(new InputStreamReader(proxySocket.getInputStream(), StandardCharsets.UTF_8));

            String credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

            out.write("CONNECT " + "api.ipify.org" + ":" + 443 + " HTTP/1.1\r\n");
            out.write("Host: " + "api.ipify.org" + "\r\n");
            out.write("Proxy-Authorization: Basic " + credentials + "\r\n");
            out.write("\r\n");
            out.flush();

            String responseLine = in.readLine();
            if (responseLine == null || !responseLine.contains("200")) {
                throw new IOException("Proxy CONNECT failed: " + responseLine);
            }

            List<String> responseHeaders = new ArrayList<>();
            String headerLine;
            while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                responseHeaders.add(headerLine);
            }

            SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslFactory.createSocket(proxySocket, "api.ipify.org", 443, true);
            sslSocket.startHandshake();

            BufferedWriter sslOut = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader sslIn = new BufferedReader(new InputStreamReader(sslSocket.getInputStream(), StandardCharsets.UTF_8));

            sslOut.write("GET / HTTP/1.1\r\n");
            sslOut.write("Host: " + "api.ipify.org" + "\r\n");
            sslOut.write("Connection: close\r\n");
            sslOut.write("\r\n");
            sslOut.flush();

            String line;
            boolean bodyStarted = false;
            System.out.print("Public IP via proxy: ");
            while ((line = sslIn.readLine()) != null) {
                if (line.isEmpty()) {
                    bodyStarted = true;
                    continue;
                }
                if (bodyStarted) {
                    System.out.println(line);
                    break;
                }
            }

            sslOut.close();
            sslIn.close();

        } catch (IOException e) {
            System.err.println("Proxy tunnel failed: " + e.getMessage());
        }
    }
}