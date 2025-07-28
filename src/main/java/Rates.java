import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Rates {
    public static Response getRates(String baseCurrensy) throws IOException, InterruptedException {
        String urlStr = "https://v6.exchangerate-api.com/v6/41770748e08f2df918168333/latest/" + baseCurrensy;

        HttpRequest request = HttpRequest.newBuilder().GET() // 'GET' request, read-only
                .uri(URI.create(urlStr))
                .build();

        HttpClient client = HttpClient.newBuilder().build(); // object to send requests

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(body, Response.class);
    }
}
