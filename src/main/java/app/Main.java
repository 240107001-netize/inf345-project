package app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = create(port);
        server.start();
        System.out.println("Listening on port " + port);
    }

    static HttpServer create(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/healthz", ex -> send(ex, 200, "{\"status\":\"ok\"}"));
        server.createContext("/convert", Main::handleConvert);
        server.createContext("/", ex -> {
            if (ex.getRequestURI().getPath().equals("/")) {
                send(ex, 200, "{\"service\":\"currency-converter\","
                        + "\"usage\":\"/convert?from=USD&to=KZT&amount=10\"}");
            } else {
                send(ex, 404, "{\"error\":\"not found\"}");
            }
        });
        return server;
    }

    private static void handleConvert(HttpExchange ex) throws IOException {
        try {
            Map<String, String> q = parseQuery(ex.getRequestURI().getRawQuery());
            String from = q.get("from");
            String to = q.get("to");
            String amount = q.get("amount");
            if (from == null || to == null || amount == null) {
                send(ex, 400, "{\"error\":\"from, to and amount are required\"}");
                return;
            }
            double result = Converter.convert(from, to, Double.parseDouble(amount));
            send(ex, 200, "{\"from\":\"" + from.toUpperCase() + "\",\"to\":\""
                    + to.toUpperCase() + "\",\"amount\":" + Double.parseDouble(amount)
                    + ",\"result\":" + result + "}");
        } catch (IllegalArgumentException e) {
            // сюда попадают и NumberFormatException, и ошибки из Converter
            send(ex, 400, "{\"error\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    private static Map<String, String> parseQuery(String raw) {
        Map<String, String> result = new HashMap<>();
        if (raw == null) {
            return result;
        }
        for (String pair : raw.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            result.put(key, value);
        }
        return result;
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", " ").replace("\r", " ");
    }

    private static void send(HttpExchange ex, int code, String body) throws IOException {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, data.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(data);
        }
    }
}