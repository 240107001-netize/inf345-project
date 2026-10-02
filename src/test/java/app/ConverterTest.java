package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ConverterTest {

    @Test
    void convertsUsdToKzt() {
        assertEquals(4800.0, Converter.convert("USD", "KZT", 10), 0.001);
    }

    @Test
    void sameCurrencyKeepsAmount() {
        assertEquals(25.0, Converter.convert("EUR", "EUR", 25), 0.001);
    }

    @Test
    void currencyCodesAreCaseInsensitive() {
        assertEquals(Converter.convert("USD", "EUR", 100),
                Converter.convert("usd", "eur", 100), 0.001);
    }

    @Test
    void unknownCurrencyIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> Converter.convert("USD", "XXX", 1));
    }

    @Test
    void negativeAmountIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> Converter.convert("USD", "KZT", -5));
    }
}