
# ClearRoute

**A dead-simple Java proxy tunnel tool built for testing, learning, and routing selected traffic through a VPS-hosted HTTP proxy.**

This project came out of a need to route certain Java-layer requests through a Squid proxy server — mostly for IP testing, diagnostic tools, or making sure a VPS is behaving like it should.

ClearRoute **does not tunnel your entire system**, it only sets up a route for HTTP/HTTPS requests made from Java code.

---

## ⚙️ What It Actually Does

- Sets Java system properties for `http.proxyHost`, `https.proxyHost`, etc.
- Authenticates using HTTP Basic (if credentials are given)
- Manually opens a TLS tunnel using a `CONNECT` request
- Makes a simple HTTPS call to `api.ipify.org`
- Prints the IP — your public one, seen through the proxy

This is particularly useful if you’re renting a VPS, spinning up a Squid instance, and just want a sanity check.

---

## 🧰 Requirements

- Java **21 or higher**
- Public VPS with **Squid proxy** installed and listening on port 3128 (or another)
- A user in `/etc/squid/passwd` file for authentication

---

## 📦 How to Use

1. Clone the repo, build it (or download prebuilt `.jar`)
2. Run:

```bash
java -jar ClearRoute-*.jar -ip <proxy_ip> -port <port> -username <user> -password <pass>
```

3. You'll see output like:

```
Public IP via proxy: 123.45.67.89
Proxy is active. Type 'exit' to disconnect.
```

You can exit gracefully anytime by typing `exit`. Your DNS cache will be flushed if applicable.

---

## 💉 Setting Up Your Proxy (on VPS)

Install Squid:

```bash
sudo apt update
sudo apt install squid apache2-utils
```

Set up your user:

```bash
sudo htpasswd -c /etc/squid/passwd youruser
```

Replace the contents of `/etc/squid/squid.conf` with something like this:

```txt
auth_param basic program /usr/lib/squid/basic_ncsa_auth /etc/squid/passwd
auth_param basic realm Proxy

acl SSL_ports port 443
acl Safe_ports port 80
acl Safe_ports port 443
acl Safe_ports port 1025-65535

acl CONNECT method CONNECT

acl authenticated proxy_auth REQUIRED

http_access allow authenticated
http_access deny all

http_access deny !Safe_ports
http_access deny CONNECT !SSL_ports
```

Then restart Squid:

```bash
sudo systemctl restart squid
```

Your proxy should now be listening on `3128`, requiring basic authentication, and only allowing safe ports.

---

## 🧪 What This Is **Not**

- It’s **not** a full system VPN.
- It doesn’t change your OS-wide IP.
- It won’t tunnel browsers or other apps unless they’re running inside the same JVM.

This tool is minimal by design — for developers, testers, and anyone tinkering with proxy servers.

---
