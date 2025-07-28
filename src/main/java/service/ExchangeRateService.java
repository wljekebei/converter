package service;
import model.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangeRateService {
    public static ExchangeRateResponse fetchRates(String baseCurrensy) throws IOException, InterruptedException {
        String urlStr = "https://v6.exchangerate-api.com/v6/" + Secrets.apiKey + "/latest/" + baseCurrensy;

        HttpRequest request = HttpRequest.newBuilder().GET() // 'GET' request, read-only
                .uri(URI.create(urlStr))
                .build();

        HttpClient client = HttpClient.newBuilder().build(); // object to send requests

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(body, ExchangeRateResponse.class);
    }
}
