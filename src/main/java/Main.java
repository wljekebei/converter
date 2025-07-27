import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Response response = Rates.getRates("UAH");
        System.out.println(response.base_code);
    }
}
