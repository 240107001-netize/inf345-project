package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MainTest {

    private static HttpServer server;
    private static String base;

    @BeforeAll
    static void start() throws IOException {
        server = Main.create(0); // порт 0 = свободный порт, выбранный системой
        server.start();
        base = "http://127.0.0.1:" + server.getAddress().getPort();
    }

    @AfterAll
    static void stop() {
        server.stop(0);
    }

    private HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(base + path)).build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void healthzReturns200() throws Exception {
        HttpResponse<String> r = get("/healthz");
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("ok"));
    }

    @Test
    void convertReturnsResult() throws Exception {
        HttpResponse<String> r = get("/convert?from=USD&to=KZT&amount=10");
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("4800.0"));
    }

    @Test
    void unknownCurrencyReturns400() throws Exception {
        assertEquals(400, get("/convert?from=USD&to=XXX&amount=10").statusCode());
    }
}