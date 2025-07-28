public class Conversion {
    public static double convert(Response response, String currency, double amount) {
        return response.conversion_rates.get(currency) * amount;
    }
}
