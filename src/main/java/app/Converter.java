package app;

import java.util.Map;

public class Converter {

    // Сколько единиц валюты равно 1 USD (учебные фиксированные курсы)
    private static final Map<String, Double> PER_USD = Map.of(
            "USD", 1.0,
            "EUR", 0.92,
            "KZT", 480.0,
            "RUB", 90.0,
            "GBP", 0.79
    );

    public static double convert(String from, String to, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        Double fromRate = PER_USD.get(from.toUpperCase());
        Double toRate = PER_USD.get(to.toUpperCase());
        if (fromRate == null) {
            throw new IllegalArgumentException("unknown currency: " + from);
        }
        if (toRate == null) {
            throw new IllegalArgumentException("unknown currency: " + to);
        }
        double result = amount / fromRate * toRate;
        return Math.round(result * 100.0) / 100.0;
    }
}