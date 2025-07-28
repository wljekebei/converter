package service;
import model.*;

public class Conversion {
    public static double convert(ExchangeRateResponse response, String currency, double amount) {
        return response.conversion_rates.get(currency) * amount;
    }
}
